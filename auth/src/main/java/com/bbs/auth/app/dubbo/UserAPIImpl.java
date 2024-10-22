package com.bbs.auth.app.dubbo;

import com.bbs.api.auth.User;
import com.bbs.api.auth.UserAPI;
import com.bbs.auth.app.user.delete.UserDelete;
import com.bbs.auth.cache.BindLoginCompanyCache;
import com.bbs.auth.converter.UserConverter;
import com.bbs.auth.entity.Invite;
import com.bbs.auth.service.InviteService;
import com.bbs.auth.service.TokenService;
import com.bbs.auth.service.UserService;
import com.bbs.exception.FailInviteException;
import com.bbs.exception.ReLoginException;
import com.bbs.vo.UserVO;
import com.bbs.util.BeanUtils;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@DubboService
public class UserAPIImpl implements UserAPI {

    @Resource
    private UserService userService;

    @Resource
    private UserConverter converter;

    @Resource
    private TokenService tokenService;

    @Resource
    private InviteService inviteService;

    @Resource
    private BindLoginCompanyCache bindLoginCompanyCache;


    @Resource
    private UserDelete userDelete;

    @Override
    public User getUserByToken(String token) {
        try {
            UserVO vo = tokenService.verify(token);
            vo.setCompanyId(bindLoginCompanyCache.get(vo.getId()));
            return converter.toAPIUser(vo);
        } catch (ReLoginException ignored) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Long getUidByInvite(String inviteCode) {
        try {
            if (StringUtils.isBlank(inviteCode))//无邀请码注册
                return null;

            Long userId = inviteService.selectJoinOne(Long.class, new MPJLambdaWrapper<Invite>()
                    .select(Invite::getUserId)
                    .eq(Invite::getInviteCode,inviteCode)
                    .ge(Invite::getValidEndTime,new Date()));

            if (Objects.nonNull(userId))
                return userId;
            else//邀请码过期
                throw new FailInviteException();
        } catch (FailInviteException ignored) {
            return NumberUtils.LONG_MINUS_ONE;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<User> getUserList(List<Long> ids) {
        return converter.toAPIUser(userService.search(ids));
    }

    @Override
    public List<User> getUserList(Set<Long> ids) {
        return converter.toAPIUser(userService.search(ids));
    }

    @Override
    public List<User>  searchByUserOrSave(Long companyId, List<User> userList) {
        List<com.bbs.auth.entity.User> users = userService.searchByUserOrSave(companyId, userList);
        return BeanUtils.toBean(users, User.class);
    }

    @Override
    public User getUserByName(String userName) {
        return converter.toAPIUser(userService.lambdaQuery().eq(com.bbs.auth.entity.User::getName, userName).one());
    }

    @Override
    public Map<Long, User> getUserIdMap(Set<Long> ids) {
        return getUserList(ids).stream().collect(Collectors.toMap(User::getId, user -> user));
    }

    @Override
    public void removeUserById(Long userId) {
        userDelete.delete(userId);
    }
}
