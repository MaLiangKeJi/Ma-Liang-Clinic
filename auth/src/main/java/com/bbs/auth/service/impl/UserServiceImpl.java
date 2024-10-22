package com.bbs.auth.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bbs.Result;
import com.bbs.auth.cache.BindLoginCompanyCache;
import com.bbs.auth.cache.code.PhoneCodeCache;
import com.bbs.auth.cache.user.PhoneCache;
import com.bbs.auth.cache.user.UserCache;
import com.bbs.auth.dao.UserDao;
import com.bbs.auth.entity.CompanyStructure;
import com.bbs.auth.entity.User;
import com.bbs.auth.entity.UserCompany;
import com.bbs.auth.entity.param.UserParam;
import com.bbs.auth.enums.UserRole;
import com.bbs.auth.mapper.UserMapper;
import com.bbs.auth.service.CompanyStructureService;
import com.bbs.auth.service.TokenService;
import com.bbs.auth.service.UserCompanyService;
import com.bbs.auth.service.UserService;
import com.bbs.vo.UserVO;
import com.bbs.enums.UserStateEnum;
import com.bbs.exception.BusinessException;
import com.bbs.exception.ReLoginException;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static com.bbs.Result.success;
import static com.bbs.auth.cache.user.UserCache.cacheIsExists;
import static com.bbs.enums.CodeEnum.FAILED_LOGIN_USER_NEED_REGISTER;
import static com.bbs.enums.CodeEnum.FAILED_USER_CODE_NOT_AVAILABLE;
import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ONE;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;

/**
* @author Lenovo
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-07-11 14:46:11
*/
@Slf4j
@Service
public class UserServiceImpl extends MPJBaseServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserDao db;

    @Lazy
    @Resource
    private PhoneCache phoneCache;

    @Lazy
    @Resource
    private UserCache cache;

    @Lazy
    @Resource
    private UserCompanyService userCompanyService;

    @Lazy
    @Resource
    private CompanyStructureService structureService;

    @Resource
    private PhoneCodeCache phoneCodeCache;


    @Override
    public Boolean userStateIsNormal(User user) { return UserStateEnum.STATUS_NORMAL.getCode().equals(user.getState()); }

    @Override
    public Boolean userIsValidity(User user) {
        Date expirationTime = user.getExpirationTime();
        if(nonNull(expirationTime)) {
            return DateUtil.between(
                    DateUtil.beginOfDay(expirationTime),
                    DateUtil.beginOfDay(new Date()),
                    DateUnit.DAY,
                    false
            ) < 0;
        }
        return true;
    }

    @Override
    public Boolean userIsUsable(User user) {
        return userIsValidity(user) && userStateIsNormal(user);
    }

    @Resource
    private HttpServletRequest request;

    @Resource
    @Lazy
    private TokenService tokenService;

    @Resource
    private BindLoginCompanyCache bindLoginCompanyCache;

    @Override
    public UserVO loginUser() throws ReLoginException {

        String token = tokenService.getToken(request);
        if(StringUtils.isNotBlank(token)) {
            try {
                UserVO userVO = tokenService.parseToken(token);
                userVO.setCompanyId(bindLoginCompanyCache.get(userVO.getId()));
                return userVO;
            } catch (Exception e) {
                log.debug("[UserServiceImpl::loginUser] token 解析异常! ", e);
            }
        }
        throw new ReLoginException();
    }

    @Override
    public Boolean checkRegister(Long phone) {
        try {
            Long uid = phoneCache.get(phone);
            if(nonNull(uid)) {
                return true;
            } else {
                return nonNull(db.selectByPhone(phone));
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public User loginEntityUser() throws ReLoginException {
        UserVO loginUser = loginUser();
        return getById(loginUser.getId());
    }

    @Override
    public User registerByPhoneNoLockNoLoad(Long phone) throws IllegalArgumentException {
        return registerByPhoneNoLockAndNoLoadCache(phone);
    }

    @Override
    public User registerByPhoneNoLockNoLoad(String phone) throws IllegalArgumentException {
        return registerByPhoneNoLockNoLoad(Long.valueOf(phone));
    }

    @Override
    public User registerByPhoneNoLockAndNoLoadCache(Long phone) throws IllegalArgumentException {
        User user = new User();
        user.setPhone(phone);
        user.setName(phone.toString());
        if(!save(user)) throw new BusinessException("通过手机号注册用户失败");
        return user;
    }

    @Override
    public String encryptPassword(User user) {
        return encryptPassword(user.getPassword(), user.getSalt());
    }

    @Override
    public String encryptPassword(String pwd, Integer salt) {
        return SecureUtil.md5(pwd + salt);
    }

    @Override
    public Boolean updatePasswordByID(String password, Long id) {
        return lambdaUpdate().set(User::getPassword, password).eq(User::getId, id).update();
    }

    @Override
    public User searchByPhone(String phone) {
        User user;
        Long uid = phoneCache.get(Long.valueOf(phone));
        if(cacheIsExists(uid)) {
            user = cache.get(uid);
            if(isNull(user)) {
                user = db.searchByID(uid);
            }
        } else {
            user = db.selectByPhone(phone);
        }
        return user;
    }

    @Override
    public User search(Long id) {
        User user = cache.get(id);
        if(isNull(user)) {
            user = db.searchByID(id);
            cache.set(user);
        }
        return user;
    }

    @Override
    public List<User> search(List<Long> ids) {
        Map<Long, User> cacheUserMaps = cache.get(ids).stream().collect(Collectors.toMap(User::getId, user -> user));
        User[] result = new User[ids.size()];
        Set<Long> cacheIsEmptyUserIds = new HashSet<>(ids.size());
        for (int index = 0; index < ids.size(); index++) {
            User cacheUser = cacheUserMaps.get(ids.get(index));
            if(nonNull(cacheUser)) {
                result[index] = cacheUser;
                continue;
            }
            cacheIsEmptyUserIds.add(ids.get(index));
        }
        if(CollectionUtils.isNotEmpty(cacheIsEmptyUserIds)) {
            Map<Long, User> idMaps = listByIds(cacheIsEmptyUserIds).stream().collect(Collectors.toMap(User::getId, user -> user));
            for (int index = 0; index < ids.size(); index++) {
                User user = result[index];
                if(isNull(user)) result[index] = idMaps.get(ids.get(index));
            }
        }
        return new ArrayList<>(Arrays.asList(result));
    }

    @Override
    public List<User> search(Set<Long> ids) {
        return search(new ArrayList<>(ids));
    }

    @Override
    public Map<Long, User> searchMap(Set<Long> ids) {
        List<User> users = search(ids);
        if(nonNull(users) && users.size() > INTEGER_ZERO) {
            return users.stream().filter(Objects::nonNull).collect(Collectors.toMap(User::getId, user -> user));
        }
        return new HashMap<>();
    }

    @Override
    public Boolean isLogin() {
        try {
            loginUser();
            return true;
        } catch (ReLoginException e) {
            return false;
        }
    }

    @Transactional
    @Override
    public List<User> searchByUserOrSave(Long companyId, List<com.bbs.api.auth.User> userList) {
        List<User> result = new ArrayList<>();
        //userList循环
        for (com.bbs.api.auth.User user : userList) {
             //循环根据用户名，身份证号，手机号，工号查询用户
            User user1 = selectJoinOne(User.class,new MPJLambdaWrapper<User>()
                    .selectAll(User.class)
                    .selectAssociation(UserCompany.class,User::getJobCard,o->o.result(UserCompany::getJobCard))
                    .leftJoin(UserCompany.class,UserCompany::getUserId, User::getId)
                    .eq(StringUtils.isNotBlank(user.getIdCard()),User::getIdCard, user.getIdCard())
                    .eq(StringUtils.isNotBlank(user.getJobCard()),UserCompany::getJobCard, user.getJobCard())
                    .eq(StringUtils.isNotBlank(user.getName()),User::getName, user.getName())
                    .eq(Objects.nonNull(user.getPhone()),User::getPhone, user.getPhone())
                    .eq(UserCompany::getCompanyId,companyId)
            );
            //查不到就添加一条
            if(isNull(user1)) {
                user1 = new User();
                user1.setName(user.getName());
                user1.setPhone(user.getPhone());
                user1.setIdCard(user.getIdCard());
                user1.setJobCard(user.getJobCard());
                save(user1);
                CompanyStructure structure = structureService.search(companyId,user.getStructureName());
                userCompanyService.save(new UserCompany(user1.getId(),companyId,user.getJobCard(),structure.getId()));
            }
            result.add(user1);
        }
        return result;
    }

    @Override
    public Boolean loginUserIsAdmin() throws IllegalArgumentException, ReLoginException {
        User user = loginEntityUser();
        if(isNull(user)) throw new ReLoginException(FAILED_LOGIN_USER_NEED_REGISTER);
        return user.isSupperAdmin();
    }

    @Override
    public void checkLoginUserIsAdmin() throws IllegalArgumentException, ReLoginException {
        User user = loginEntityUser();
        Preconditions.checkArgument(user.isSupperAdmin(), "当前用户非超级管理员");
    }

    @Override
    public void checkPhoneCodeThrow(Long phone, Integer code) throws IllegalArgumentException {
        checkArgument(phoneCodeCache.checkCode(phone, code), FAILED_USER_CODE_NOT_AVAILABLE);
    }

    @Override
    public Integer countByOpenId(String fromUserName) {
        return lambdaQuery().eq(User::getOpenId, fromUserName).count().intValue();
    }

    @Override
    public User searchIdByOpenId(String openId) {
        return lambdaQuery().eq(User::getOpenId, openId).one();
    }

    @Override
    public void bindUser(Long uid, String openId) {
        lambdaUpdate()
                .eq(User::getId, uid)
                .set(User::getOpenId, openId)
                .update();
        cache.load(uid);
    }

    @Override
    public void checkLoginUserIsSales() throws IllegalArgumentException, ReLoginException {
        User user = loginEntityUser();
        Preconditions.checkArgument(UserRole.SALES.getCode().equals(user.getRole()), "您无权限查看当前页面");
    }

    @Override
    public Result<Page<User>> search(UserParam param) {
        MPJLambdaWrapper<User> wrapper = new MPJLambdaWrapper<>(User.class);
        if(nonNull(param.getId())) {
            wrapper.eq(User::getId, param.getId());
        } else {
            wrapper
                    .like(StringUtils.isNotBlank(param.getEmail()), User::getEmail, param.getEmail())
                    .likeRight(StringUtils.isNotBlank(param.getName()), User::getName, param.getName())
                    .eq(nonNull(param.getPhone()), User::getPhone, param.getPhone())
                    .between(nonNull(param.getCreateStartTime()) && nonNull(param.getCreateEndTime()), User::getCreateTime, param.getCreateStartTime(), param.getCreateEndTime())
                    .eq(nonNull(param.getStatus()), User::getState, param.getStatus())
                    .orderByDesc(isNull(param.getSortType()), User::getCreateTime)

                    .orderByDesc(nonNull(param.getSortType()) && param.getSortType() == 0, User::getExpirationTime)
                    .orderByDesc(nonNull(param.getSortType()) && param.getSortType() == 1, User::getCreateTime)
            ;
        }
        Page<User> result = wrapper.page(param.toPage());
        result.getRecords().forEach(user -> user.setStateStr(UserStateEnum.enumMap.get(user.getState()).getMsg()));
        return success(result);
    }

    @Override
    public User search(String email) {
        return lambdaQuery().eq(User::getEmail, email).one();
    }

    @Override
    public User searchByEmailElseThrow(String email) throws IllegalArgumentException {
        return Opt.ofNullable(search(email)).orElseThrow(() -> new IllegalArgumentException("对应邮箱用户不存在！"));
    }
}




