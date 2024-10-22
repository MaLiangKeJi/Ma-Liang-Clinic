package com.bbs.auth.app.login;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.bbs.Result;
import com.bbs.auth.app.login.param.Param;
import com.bbs.auth.app.login.vo.VO;
import com.bbs.auth.cache.code.PhoneCodeCache;
import com.bbs.auth.cache.user.UserCache;
import com.bbs.auth.dao.UserDao;
import com.bbs.auth.entity.*;
import com.bbs.auth.service.*;
import com.bbs.auth.util.RedisUtil;
import com.bbs.auth.util.WxUtil;
import com.bbs.enums.LoginType;
import com.bbs.enums.UserStateEnum;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RDeque;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.bbs.Result.failed;
import static com.bbs.Result.success;
import static com.bbs.auth.enums.RedisKeys.USER_LOGIN_PHONE;
import static com.bbs.enums.CodeEnum.*;
import static com.bbs.util.PhoneUtil.*;
import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;

@Slf4j
@RestController
@RequestMapping
public class Login {

    @Resource
    private UserCache userCache;
    @Resource
    private UserService service;
    @Resource
    private PhoneCodeCache phoneCodeCache;

    @Resource
    private TokenService tokenService;

    @Resource
    private RedisUtil.Redisson redissonUtil;

    @Resource
    private RedissonClient redisson;
    @Resource
    private UserDao db;
    @Resource
    private CompanyService companyService;

    @Resource
    private LoginLogService logService;

    @Resource
    private WxUtil wxUtil;

    @Value("${wx.oa.token}")
    private String token;

    @Resource
    private InviteService inviteService;

    @Resource
    private InviteUserService inviteUserService;

    @PostMapping("/login")
    public Result<VO> login(@Valid @RequestBody Param param) throws InterruptedException, IllegalArgumentException {
        String loginTime = DateUtil.now();
        String phone = param.getPhone();
        String loginType = param.getLoginType();
        String paramCode = param.getCode();
        return redissonUtil.lockExec(
            () -> {
                try {
                    User user;
                    Integer code;
                    if(LoginType.PHONE.getCode().equals(loginType)) {
                        checkPhoneFormatThrows(phone);
                        checkPhoneCodeFormat(paramCode);
                        code = phoneCodeCache.getCode(phone);
                        checkArgument(nonNull(code) && code.equals(Integer.valueOf(paramCode)), FAILED_AUTH_PHONE_CODE_NOT_AVAILABLE);
                        phoneCodeCache.delCode(phone);
                        user = userCache.searchByPhoneNoLockNoLoad(phone);

                        if(nonNull(user)) {
                            // 用户已注册
                            checkUserState(user);
                        } else {
                            // 用户未注册
                            user = new User(phone, Long.valueOf(phone));
                            service.save(user);
                        }

                    } else if (LoginType.WX.getCode().equals(loginType)) {
                        // 场景1：未注册（手机号未注册，且微信未绑定）
                        // 场景2：手机号已注册，但微信未绑定
                        // PS：不需要【响应用户未绑定手机号，需要绑定手机号】步骤，已在上个步骤【轮询扫码状态】中判断并响应

                        // 1. 校验手机号 & 验证码格式
                        checkPhoneFormatThrows(phone);
                        checkPhoneCodeFormat(paramCode);
                        // 2. 查询验证码，并比较
                        code = phoneCodeCache.getCode(phone);
                        checkArgument(nonNull(code) && code.equals(Integer.valueOf(paramCode)), FAILED_AUTH_PHONE_CODE_NOT_AVAILABLE);
                        // 3. 删除验证码
                        phoneCodeCache.delCode(phone);
                        // 4. 解码 VXOpenId
                        String openId = decryptOpenId(param);
                        // 5. 根据手机号查询用户
                        user = userCache.searchByPhoneNoLockNoLoad(phone);
                        if(nonNull(user)) {
                            // 存在用户，直接绑定 VX
                            service.bindUser(user.getId(), openId);
                            //公众号下发绑定成功
                            wxUtil.sendBindingMassage(openId, user);
                        } else {
                            // 不存在则注册用户
                            user = new User(phone, Long.valueOf(phone), openId);
                            service.save(user);
                            //公众号下发注册成功
                            wxUtil.sendRegisterMassage(openId, user);
                        }

                    } else if(LoginType.PASSWORD.getCode().equals(loginType)) {
                        checkPhoneAndPWDFormat(param);
                        user = searchUser(phone);
                        checkArgument(nonNull(user), FAILED_LOGIN_USER_NEED_REGISTER);
                        checkArgument(nonNull(user.getPassword()), FAILED_LOGIN_USER_NOT_SET_PWD);
                        checkUserState(user);
                        checkUserPWD(param, user);
                    } else if(LoginType.PASSWORD_CREATE.getCode().equals(loginType)) {
                        checkPhoneAndPWDFormat(param);
                        user = searchUser(phone);
                        if(nonNull(user)) {
                            // 用户已注册
                            checkUserState(user);
                            checkUserPWD(param, user);
                        } else {
                            // 用户未注册
                            throw new IllegalArgumentException(FAILED_LOGIN_USER_NEED_REGISTER.getMsg());
                        }
                    } else {
                        throw new IllegalArgumentException(FAILED_LOGIN_TYPE_NOT_AVAILABLE.getMsg());
                    }
                    Date expirationTime = user.getExpirationTime();
                    if(nonNull(expirationTime)) {
                        long between = DateUtil.between(expirationTime, new Date(), DateUnit.DAY);
                        if(between >= INTEGER_ZERO) {
                            // 正常
                            if(between <= 3) {
                                // 即将过期
                                // TODO 向用户发送通知
                            }
                        } else {
                            // 过期
                            throw new IllegalArgumentException(FAILED_ACCOUNT_EXPIRED.getMsg());
                        }
                    }

                    List<UserCompany> userCompanyList = searchUserCompany(user.getId());
                    searchIsSetCompanyStructure(param.getCheckCompanyStructure(), userCompanyList);

                    String token = tokenService.createToken(user);
                    tokenService.setLoginFlag(user.getId(), param.getExpireNumber(), TimeUnit.DAYS);
                    userCache.expireUserAndPhoneMap(user);

                    // 邀请相关
                    String inviteCode = param.getInviteCode();
                    if(StringUtils.isNoneBlank(inviteCode)) {
                        Invite invite = inviteService.lambdaQuery().eq(Invite::getInviteCode, inviteCode).one();
                        if(nonNull(invite)) {
                            inviteUserService.save(new InviteUser(invite.getUserId(), user.getId(), inviteCode));
                        }
                    }
                    recordLoginSuccessLog(param, token, loginTime);
                    return success(new VO(user.getId(), user.getName(), token, userCompanyList));
                } catch (IllegalArgumentException e) {
                    recordLoginFailLog(param, e.getMessage(), loginTime);
                    throw e;
                }
            },
            () -> failed(500, new VO(), "无法获取登录锁，详情请联系客服"),
                redisson.getSpinLock(USER_LOGIN_PHONE.LOCK.key(phone)),
                50000,
                50000,
                MILLISECONDS
        );
    }

    private String decryptOpenId(Param param) {
        String openId = param.getOpenId();
        Preconditions.checkArgument(StringUtils.isNotBlank(param.getOpenId()), "缺少微信用户ID");
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, token.getBytes());
        return new String(aes.decrypt(openId));
    }

    private void checkPhoneAndPWDFormat(Param param) {
        checkPhoneFormatThrows(param.getPhone());
        checkArgument(StringUtils.isNoneBlank(param.getPassword()));
    }

    private void checkUserPWD(Param param, User user) throws IllegalArgumentException {
        String encryptPassword = service.encryptPassword(param.getPassword(), user.getSalt());
        checkArgument(user.getPassword().equals(encryptPassword), FAILED_LOGIN_PWD_ERROR);
    }

    private void checkUserState(User user) throws IllegalArgumentException {
        checkArgument(UserStateEnum.STATUS_NORMAL.getCode().equals(user.getState()), FAILED_LOGIN_USER_STATUS_ERROR);
    }

    private User searchUser(String phone) {
        User user = userCache.searchByPhoneNoLockNoLoad(phone);
        if(isNull(user)) user = db.selectByPhone(phone);
        return user;
    }

    private static final String LOG_DEQUE_KEY = "LOG:LOGIN";

    private List<UserCompany> searchUserCompany(Long uid) {
        return companyService.searchCompany(uid);
    }

    private RDeque<String> deque() {
        return redisson.getDeque(LOG_DEQUE_KEY);
    }

    private void searchIsSetCompanyStructure(boolean checkCompanyStructure, List<UserCompany> userCompanyList) {
        if(checkCompanyStructure) {
            for (UserCompany userCompany : userCompanyList) {
                Company company = userCompany.getCompany();
                company.setIsSetCompanyStructure(companyService.searchIsSetCompanyStructure(userCompany.getCompanyId()));
            }
        }
    }

    public void recordLoginSuccessLog(Param param, String newToken, String loginTime) {
        LoginLog log = new LoginLog(INTEGER_ZERO, param, loginTime);
        log.setNewToken(newToken);
        logService.save(log);
    }

    public void recordLoginFailLog(Param param, String errorMsg, String loginTime) {
        LoginLog log = new LoginLog(INTEGER_ZERO, param, loginTime);
        log.setErrorMsg(errorMsg);
        logService.save(log);
    }
}
