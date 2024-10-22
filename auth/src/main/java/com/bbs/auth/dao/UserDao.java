package com.bbs.auth.dao;

import com.bbs.auth.app.reset.ChangePhone;
import com.bbs.auth.entity.User;
import com.bbs.auth.mapper.UserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

import static java.util.Objects.nonNull;

@Component
public class UserDao extends ServiceImpl<UserMapper, User> {

    /**
     * 验证手机号是否注册过
     * @param user 验证参数
     * @return 手机号是否注册过
     * 手机号校验方式：nonNull(phone) && phone.toString().length() <= 11, Info::getPhone, phone)
     */
    public boolean exists(User user){
        return lambdaQuery()
                .eq(User::getPhone, user.getPhone())
                .exists();
    }
    public boolean notExists(User user) {
        return !exists(user);
    }

    /**
     * 根据手机号查询加过密的密码
     * @param phone 入参
     * @return Info
     */
    public User selectByPhone(String phone){
        return lambdaQuery().eq(nonNull(phone),User::getPhone,phone).one();
    }

    public User selectByPhone(Long phone){
        return selectByPhone(phone.toString());
    }

    public User searchByID(Long uid) throws IllegalArgumentException {
        return getOptById(uid).orElseThrow(() -> new IllegalArgumentException("对应 ID 用户不存在！"));
    }

    public User search(String phone) {
        return lambdaQuery().eq(User::getPhone, phone).one();
    }

    public void update(ChangePhone.Param param) {
        lambdaUpdate().set(User::getPhone, param.getPhone()).eq(User::getId, param.getUid()).update();
    }

    public User search(Long uid) {
        return getById(uid);
    }
}
