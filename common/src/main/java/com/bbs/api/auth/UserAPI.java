package com.bbs.api.auth;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface UserAPI {

    User getUserByToken(String token);

    /**
     * 根据邀请码返回用户
     * @param inviteCode 邀请码
     */
    Long getUidByInvite(String inviteCode);

    List<User> getUserList(List<Long> ids);

    List<User> getUserList(Set<Long> ids);

    List<User> searchByUserOrSave(Long companyId, List<User> userList);

    User getUserByName(String userName);

    Map<Long, User> getUserIdMap(Set<Long> ids);

    void removeUserById(Long userId);
}
