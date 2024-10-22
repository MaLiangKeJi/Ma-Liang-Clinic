package com.bbs.auth.service.impl;

import com.bbs.auth.entity.Group;
import com.bbs.auth.entity.UserGroup;
import com.bbs.auth.mapper.UserGroupMapper;
import com.bbs.auth.service.PermissionService;
import com.bbs.auth.enums.RoleCodeEnum;
import com.bbs.auth.service.UserService;
import com.bbs.vo.UserVO;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

import static java.util.Objects.nonNull;

@Service
@Slf4j
public class PermissionServiceImpl implements PermissionService {

    @Resource
    private UserGroupMapper userGroupMapper;

    @Override
    public List<UserGroup> query(Long uid) {
        return userGroupMapper.selectJoinList(UserGroup.class, new MPJLambdaWrapper<UserGroup>()
                .selectAll(UserGroup.class)
                .selectAssociation(Group.class, UserGroup::getGroup)
                .leftJoin(Group.class, Group::getId, UserGroup::getGroupId)

                .eq(UserGroup::getUserId, uid)
        );
    }

    @Override
    public Boolean permissionIsAdmin(List<UserGroup> userGroups) {
        return userGroups.stream().anyMatch(userGroup -> RoleCodeEnum.ADMIN.getCode().equals(userGroup.getGroup().getCode()));
    }

    @Override
    public Boolean userIsAdmin(Long uid) {
        List<UserGroup> userGroups = query(uid);
        return permissionIsAdmin(userGroups);
    }

    @Resource
    private UserService userService;

    @Override
    public Boolean loginUserIsAdmin() {
        UserVO userVO = userService.loginUser();
        if(nonNull(userVO)) {
            return userIsAdmin(userVO.getId());
        }
        return null;
    }
}
