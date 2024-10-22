package com.bbs.auth.service;

import com.bbs.auth.entity.User;
import com.bbs.auth.entity.param.UserParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bbs.Result;
import com.bbs.vo.UserVO;
import com.bbs.exception.ReLoginException;
import com.github.yulichang.base.MPJBaseService;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
* @author Lenovo
* @description 针对表【user】的数据库操作Service
* @createDate 2023-07-11 14:46:11
*/
public interface UserService extends MPJBaseService<User> {

    Result<Page<User>> search(UserParam param);

    User search(String email);

    /**
     * 查询
     * @param email 邮箱
     * @return 用户信息
     * @throws IllegalArgumentException 用户 ID 不存在
     */
    User searchByEmailElseThrow(String email) throws IllegalArgumentException;

    /**
     * 用户账号状态是否正常
     * @param user 用户信息
     * @return 账号状态正常
     */
    Boolean userStateIsNormal(User user);

    /**
     * 用户账号是否在有效期内（无有效期返回 true）
     * @param user 用户信息
     * @return 用户账号在有效期内（无有效期返回 true）
     */
    Boolean userIsValidity(User user);

    /**
     * 用户账号是否可用（状态验证/过期验证）
     * @param user 用户信息
     * @return 可用
     */
    Boolean userIsUsable(User user);

    /**
     * 获取登录用户
     * @return 登录用户（未登录 or 登录信息校验失败 return null）
     * @throws ReLoginException 用户未登录
     */
    UserVO loginUser() throws ReLoginException;

    Boolean checkRegister(Long phone);

    User loginEntityUser() throws ReLoginException;

    /**
     * 通过手机号注册用户（无锁、无事务）
     * @param phone 手机号
     * @return UID
     * @throws IllegalArgumentException 手机号已被注册
     */
    User registerByPhoneNoLockNoLoad(Long phone) throws IllegalArgumentException;

    User registerByPhoneNoLockNoLoad(String phone) throws IllegalArgumentException;

    /**
     * 通过手机号注册用户（无锁、无事务、未加载缓存）
     * @param phone 手机号
     * @return UID
     * @throws IllegalArgumentException 手机号已被注册
     */
    User registerByPhoneNoLockAndNoLoadCache(Long phone) throws IllegalArgumentException;


    String encryptPassword(User user);

    String encryptPassword(String pwd, Integer salt);

    Boolean updatePasswordByID(String password, Long id);

    User searchByPhone(String phone);

    User search(Long id);

    /**
     * 批量查询
     * @param ids ID
     * @return 存在，则占位为 null
     */
    List<User> search(List<Long> ids);

    List<User> search(Set<Long> ids);

    Map<Long, User> searchMap(Set<Long> ids);

    Boolean isLogin();

    List<User> searchByUserOrSave(Long companyId, List<com.bbs.api.auth.User> userList);

    void checkLoginUserIsAdmin() throws IllegalArgumentException, ReLoginException;

    Boolean loginUserIsAdmin() throws IllegalArgumentException, ReLoginException;

    void checkPhoneCodeThrow(Long phone, Integer code) throws IllegalArgumentException;

    Integer countByOpenId(String fromUserName);

    User searchIdByOpenId(String openId);

    void bindUser(Long uid, String openId);

    void checkLoginUserIsSales() throws IllegalArgumentException, ReLoginException;
}
