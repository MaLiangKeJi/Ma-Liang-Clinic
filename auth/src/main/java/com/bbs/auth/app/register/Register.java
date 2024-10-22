package com.bbs.auth.app.register;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bbs.Result;
import com.bbs.auth.app.login.vo.VO;
import com.bbs.auth.converter.UserConverter;
import com.bbs.auth.dao.UserDao;
import com.bbs.auth.entity.User;
import com.bbs.auth.mapper.UserMapper;
import com.bbs.auth.service.TokenService;
import com.bbs.auth.service.UserService;
import com.bbs.enums.UserStateEnum;
import lombok.Data;
import net.sf.jsqlparser.util.validation.metadata.DatabaseException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.util.Date;
import java.util.Random;

import static com.bbs.Result.failed;
import static com.bbs.Result.success;
import static com.bbs.enums.CodeEnum.FAILED_USER_INFO_DUPLICATION;
import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.math.NumberUtils.LONG_ZERO;

@RestController
@RequestMapping
public class Register extends ServiceImpl<UserMapper, User> {

    /**
     * 新用户注册试用天数
     */
    @Value("${user.register.try.day}")
    private Integer tryDay;

    @Resource
    private DataSourceTransactionManager transactionManager;
    @Resource
    private TransactionDefinition transactionDefinition;

    @Resource
    private UserConverter converter;
    @Resource
    private UserDao dao;
    @Resource
    private UserService service;
    @Resource
    private TokenService tokenService;
    @Data
    public static class Param {

        private String userName;

        private String clinicName;

        private String password;

        @NotNull(message = "手机号不能为空")
        private Long phone;

        @NotNull(message = "验证码不能为空")
        private Integer code;

        private String email;

        private Long group;

        /**
         * 平台角色
         */
        private Integer paasRole;
    }


    /**
     * 用户注册
     * @param param 注册用户入参
     * @return 注册是否成功
     */
    @PutMapping("/user")
    public Result<VO> register(@Valid @RequestBody Param param) throws IllegalArgumentException{
        TransactionStatus transaction = transactionManager.getTransaction(transactionDefinition);
        User user = converter.toEntity(param);
        service.checkPhoneCodeThrow(param.phone, param.code);
        checkArgument(dao.notExists(user), FAILED_USER_INFO_DUPLICATION);
        try {
            if(StringUtils.isNotBlank(user.getPassword())) {
                user.setSalt(createSalt());
                user.setPassword(service.encryptPassword(user));
                user.setState(UserStateEnum.STATUS_NORMAL.getCode());
            }

            if(StringUtils.isNotBlank(user.getName())) {
                user.setName(user.getPhone().toString());
            }

            user.setCreateBy(LONG_ZERO);

            user.setExpirationTime(DateUtil.offsetDay(new Date(), tryDay));

            saveUser(user);

            transactionManager.commit(transaction);
            return success(new VO(user.getId(), user.getName(), tokenService.createToken(user), null));
        } catch (Exception e) {
            e.printStackTrace();
            transactionManager.rollback(transaction);
            return failed(e.getMessage());
        }
    }

    private Integer createSalt() {
        return (new Random().nextInt(5) + 7) * 9;
    }

    private void saveUser(User user) throws DatabaseException {
        if(!save(user)) throw new DatabaseException("保存用户信息失败");
    }
}
