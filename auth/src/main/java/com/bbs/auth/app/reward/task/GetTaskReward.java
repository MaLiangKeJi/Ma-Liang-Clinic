package com.bbs.auth.app.reward.task;

import cn.hutool.core.date.DateUtil;
import com.bbs.Result;
import com.bbs.auth.cache.user.UserCache;
import com.bbs.auth.entity.InviteUser;
import com.bbs.auth.entity.User;
import com.bbs.auth.service.InviteUserService;
import com.bbs.auth.service.UserService;
import com.google.common.base.Preconditions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ONE;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;

@RestController
@RequestMapping
public class GetTaskReward {

    @Resource
    private InviteUserService inviteUserService;

    @Resource
    private UserService userService;

    @Resource
    private UserCache userCache;

    @Transactional
    @GetMapping("/reward/task/invite/first")
    public Result<Boolean> getInviteFirstTaskReward() {
        User loginUser = userService.loginEntityUser();
        Long loginUserID = loginUser.getId();
        InviteUser inviteUser = inviteFirstTaskIsComplete(loginUserID);
        Preconditions.checkArgument(nonNull(inviteUser), "您的奖励任务未完成：邀请 1 家诊所使用");
        extensionExpirationTime(loginUser);
        updateRewardClaimStatus(inviteUser);
        return Result.success();
    }

    private void extensionExpirationTime(User loginUser) {
        Date expirationTime = loginUser.getExpirationTime();
        if(nonNull(expirationTime)) {
            loginUser.setExpirationTime(DateUtil.offsetMonth(expirationTime, INTEGER_ONE));
            userCache.updateByID(loginUser);
        }
    }

    private void updateRewardClaimStatus(InviteUser inviteUser) {
        inviteUser.setReward(INTEGER_ONE);
        inviteUserService.updateById(inviteUser);
    }

    private InviteUser inviteFirstTaskIsComplete(Long loginUserID) {
        List<InviteUser> inviteUserList = inviteUserService.lambdaQuery()
                .eq(InviteUser::getInitiatorUserId, loginUserID)
                .and(ext -> ext
                        .eq(InviteUser::getReward, INTEGER_ZERO)
                        .or()
                        .isNull(InviteUser::getReward)
                )
                .list();
        return nonNull(inviteUserList) && inviteUserList.size() > INTEGER_ZERO ? inviteUserList.get(INTEGER_ZERO) : null;
    }
}
