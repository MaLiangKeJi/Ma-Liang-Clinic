package com.bbs.auth.app.user;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bbs.Result;
import com.bbs.auth.entity.Invite;
import com.bbs.auth.entity.InviteUser;
import com.bbs.auth.entity.User;
import com.bbs.auth.enums.InviteClaimStatus;
import com.bbs.auth.service.InviteService;
import com.bbs.auth.service.InviteUserService;
import com.bbs.auth.service.UserService;
import com.bbs.auth.util.LoginUser;
import com.bbs.vo.UserVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.bbs.auth.enums.InviteClaimStatus.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;

/**
 * 获取邀请码
 */
@RestController
@RequestMapping
public class GetInvite {

    @Value(value = "${user.register.inviteValidDay}")
    private Integer invValidDay;

    @Resource
    private InviteService orm;

    @Resource
    private InviteUserService inviteUserService;
    @Resource
    private UserService userService;

    private static final class StringConstant {
        private static final String SHI_QU = "+8";
    }

    /**
     * 获取邀请码
     */
    @Transactional
    @GetMapping("/invite")
    public Result<String> getInviteCode() {
        Invite invite = orm.search();
        if(isNull(invite)) {
            UserVO loginUser = userService.loginUser();
            invite = new Invite(loginUser.getId(), IdUtil.randomUUID(), getValidDate());
            orm.save(invite);
        }
        return Result.success(invite.getInviteCode());
    }

    /**
     * 获取邀请码失效时间
     */
    private Date getValidDate() {
        LocalDateTime validLDT = DateTime.now().toLocalDateTime().plusDays(invValidDay);
        Long milliSecond = validLDT.toInstant(ZoneOffset.of(StringConstant.SHI_QU)).toEpochMilli();
        return new Date(milliSecond);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GenerateInviteCodeParam {

        private String code;
    }

    @PutMapping("/invite")
    public Result<Boolean> generateInviteCode(@RequestBody GenerateInviteCodeParam param) {
        if(orm.lambdaQuery().eq(Invite::getInviteCode, param.getCode()).exists()) {
            throw new IllegalArgumentException("邀请码已存在（重复），请更换");
        }
        UserVO loginUser = userService.loginUser();
        Invite invite = new Invite(loginUser.getId(), param.getCode());
        return Result.success(orm.save(invite));
    }

    @GetMapping("/invite/code/list")
    public Result<Page<Invite>> getInviteCodeList(
            @RequestParam(required = false, defaultValue = "1") Integer current,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        return Result.success(orm.lambdaQuery().eq(Invite::getUserId, LoginUser.getId()).page(new Page<>(current, size)));
    }

    /**
     * 获取已邀请用户
     */
    @GetMapping("/invite/list")
    public Result<Page<InviteUser>> getInviteCodeList(
            @RequestParam(required = false) String val,
            @RequestParam(required = false) Long createTime,
            @RequestParam(required = false, defaultValue = "1") Integer current,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        Date createTimeDate = nonNull(createTime) ? new Date(createTime) : null;

        Page<InviteUser> inviteUserPage = inviteUserService.lambdaQuery()
                .and(nonNull(createTimeDate), ext -> ext
                        .ge(InviteUser::getCreateTime, nonNull(createTimeDate) ? DateUtil.beginOfDay(createTimeDate) : null)
                        .lt(InviteUser::getCreateTime, nonNull(createTimeDate) ? DateUtil.endOfDay(createTimeDate) : null)
                )
                .eq(InviteUser::getInitiatorUserId, LoginUser.getId())
                .page(new Page<>(current, size));
        List<InviteUser> inviteUserList = inviteUserPage.getRecords();
        Set<Long> allInviteUserIds = inviteUserList.stream().map(InviteUser::getInvitedUserId).collect(Collectors.toSet());

        if(StringUtils.isNoneBlank(val) && allInviteUserIds.size() > INTEGER_ZERO) {
            Set<Long> filterIds = userService.search(allInviteUserIds).stream()
                    .filter(user -> Pattern.matches("%" + val + "%", user.getName()) || Pattern.matches("%" + val + "%", user.getPhone().toString()))
                    .map(User::getId)
                    .collect(Collectors.toSet());

            if(filterIds.size() > INTEGER_ZERO) {
                inviteUserList = inviteUserList.stream().filter(inviteUser -> !filterIds.contains(inviteUser.getInvitedUserId())).collect(Collectors.toList());
            } else {
                inviteUserList = new ArrayList<>();
            }
        }


        if(inviteUserList.size() > INTEGER_ZERO) {
            Set<Long> userIds = new HashSet<>();
            inviteUserList.forEach(inviteUser -> {
                userIds.add(inviteUser.getInvitedUserId());
                userIds.add(inviteUser.getInitiatorUserId());
                inviteUser.setCreateTimeStr(DateUtil.formatDateTime(inviteUser.getCreateTime()));
            });
            Map<Long, User> userMap = userService.searchMap(userIds);
            if(nonNull(userMap) && userMap.size() > INTEGER_ZERO) {
                inviteUserList.forEach(inviteUser -> {
                    inviteUser.setInvitedUser(userMap.get(inviteUser.getInvitedUserId()));
                    inviteUser.setInitiatorUser(userMap.get(inviteUser.getInitiatorUserId()));
                });
            }
        }
        inviteUserPage.setRecords(inviteUserList);
        return Result.success(inviteUserPage);
    }

    /**
     * 检查初次邀请任务的完成、奖励领取状态
     */
    @GetMapping("/invite/check/is/invite")
    public Result<Integer> checkIsInvite() {
        InviteClaimStatus result = UNFINISHED;
        List<InviteUser> inviteUserList = searchInviteList();
        if(nonNull(inviteUserList)) {
            // 判断是否完成任务
            if(isCompleteTask(inviteUserList)) {
                result = UNCLAIMED_AWARD;
            }
            // 判断是否已领取奖励
            for (InviteUser inviteUser : inviteUserList) {
                if(isAwardReceived(inviteUser)) {
                    result = AWARD_RECEIVED;
                    break;
                }
            }
        }
        return Result.success(result.getCode());
    }

    private Boolean isCompleteTask(List<InviteUser> inviteUserList) {
        return inviteUserList.size() > INTEGER_ZERO;
    }

    private Boolean isAwardReceived(InviteUser inviteUser) {
        Integer rewardState = inviteUser.getReward();
        return nonNull(rewardState) && AWARD_RECEIVED.getCode().equals(rewardState);
    }

    private List<InviteUser> searchInviteList() {
        User loginUser = userService.loginEntityUser();
        Long loginUserID = loginUser.getId();
        return inviteUserService.lambdaQuery()
                .eq(InviteUser::getInitiatorUserId, loginUserID)
                .list();
    }
}