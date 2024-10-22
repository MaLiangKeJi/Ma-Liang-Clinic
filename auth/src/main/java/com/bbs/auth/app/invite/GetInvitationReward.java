package com.bbs.auth.app.invite;

import com.bbs.Result;
import com.bbs.auth.entity.InvitationReward;
import com.bbs.auth.entity.Invite;
import com.bbs.auth.entity.InviteUser;
import com.bbs.auth.service.InvitationRewardService;
import com.bbs.auth.service.InviteService;
import com.bbs.auth.service.InviteUserService;
import com.bbs.auth.service.UserService;
import com.google.common.base.Preconditions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import java.util.Objects;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ONE;

/**
 * 领取受邀奖励
 */
@RestController
@RequestMapping
public class GetInvitationReward {

    @Resource
    private InvitationRewardService invitationRewardService;
    @Resource
    private UserService userService;
    @Resource
    private InviteService inviteService;

    @Resource
    private InviteUserService inviteUserService;

    @Transactional
    @GetMapping("/reward/invitation")
    public Result<Boolean> get(@RequestParam String code) throws IllegalAccessError {
        Long loginUserId = userService.loginUser().getId();
        InvitationReward invitationReward = invitationRewardService.lambdaQuery().eq(InvitationReward::getUserId, loginUserId).one();
        boolean isNotGet = isNull(invitationReward) || !Objects.equals(invitationReward.getState(), INTEGER_ONE);
        Preconditions.checkArgument(isNotGet, "奖励已领取");
        Invite invite = inviteService.lambdaQuery().eq(Invite::getInviteCode, code).one();
        Preconditions.checkArgument(nonNull(invite), "邀请码不存在");
        if(isNull(invitationReward)) {
            invitationRewardService.save(new InvitationReward(loginUserId, code, INTEGER_ONE));
        } else {
            invitationReward.setInviteCode(code);
            invitationReward.setState(INTEGER_ONE);
            invitationRewardService.updateById(invitationReward);
        }
        InviteUser inviteUser = inviteUserService.lambdaQuery()
                .eq(InviteUser::getInitiatorUserId, invite.getUserId())
                .eq(InviteUser::getInvitedUserId, loginUserId)
                .one();
        if(isNull(inviteUser)) {
            inviteUserService.save(new InviteUser(invite.getUserId(), loginUserId, code));
        }
        return Result.success();
    }
}
