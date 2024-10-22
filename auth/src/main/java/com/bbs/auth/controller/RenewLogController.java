package com.bbs.auth.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bbs.Result;
import com.bbs.auth.entity.InviteUser;
import com.bbs.auth.entity.RenewLog;
import com.bbs.auth.entity.User;
import com.bbs.auth.service.InviteUserService;
import com.bbs.auth.service.RenewLogService;
import com.bbs.auth.service.UserService;
import com.bbs.auth.util.LoginUser;
import com.bbs.auth.util.PageUtil;
import com.bbs.exception.ReLoginException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ONE;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;

/**
 * 支付记录
 */
@RestController
public class RenewLogController {

    @Resource
    private RenewLogService renewLogService;

    @Resource
    private UserService userService;

    @Resource
    private InviteUserService inviteUserService;

    @GetMapping("/back/renew/log")
    public Result<Page<RenewLog>> backSearch(@RequestParam(required = false) Long userId, Integer current, Integer size) throws IllegalArgumentException, ReLoginException {
        userService.checkLoginUserIsAdmin();
        Page<RenewLog> result = renewLogService.lambdaQuery()
                .eq(nonNull(userId), RenewLog::getCreateBy, userId)
                .page(new Page<>(current, size));
        List<RenewLog> records = result.getRecords();
        if(result.getRecords().size() > INTEGER_ZERO) {
            Map<Long, User> uidMap = userService.searchMap(result.getRecords().stream().map(RenewLog::getCreateBy).collect(Collectors.toSet()));
            records.forEach(log -> {
                log.setUser(uidMap.get(log.getCreateBy()));
                log.setCreateTimeStr(DateUtil.formatDateTime(log.getCreateTime()));
            });
        }
        return Result.success(result);
    }

    /**
     * 客户累计充值列表
     */
    @GetMapping("/back/renew/count/log")
    public Result<Page<RenewLog>> searchCumulativePaymentRecord(
            @RequestParam(required = false, defaultValue = "1") Integer current,
            @RequestParam(required = false, defaultValue = "10")Integer size
    ) throws IllegalArgumentException, ReLoginException {
        userService.checkLoginUserIsAdmin();
        List<RenewLog> all = renewLogService.list();
        if(nonNull(all) && all.size() > INTEGER_ZERO) {

            Map<Long, BigDecimal> countMap = new HashMap<>();
            Map<Long, Integer> payNumberMap = new HashMap<>();
            all.forEach(log -> {
                BigDecimal countMoney = countMap.getOrDefault(log.getCreateBy(), new BigDecimal(INTEGER_ZERO));
                countMap.put(log.getCreateBy(), countMoney.add(log.getMoney()));
                payNumberMap.put(log.getCreateBy(), payNumberMap.getOrDefault(log.getCreateBy(), INTEGER_ZERO) + INTEGER_ONE);
            });
            Set<Long> userIds = countMap.keySet();
            Map<Long, User> uidMap = userService.searchMap(userIds);
            Set<RenewLog> logs = userIds.stream().map(id -> {
                RenewLog renewLog = new RenewLog();
                renewLog.setMoney(countMap.get(id));
                renewLog.setUser(uidMap.get(id));
                renewLog.setPayNumber(payNumberMap.get(id));
                return renewLog;
            }).collect(Collectors.toSet());
            return Result.success(PageUtil.execPage(current, size, new ArrayList<>(logs)));
        }
        return Result.success(new Page<>());
    }


    /**
     * 客户充值记录
     */
    @GetMapping("/renew/log/list")
    public Result<Page<RenewLog>> search(Integer current, Integer size) throws IllegalArgumentException, ReLoginException {
        Long id = userService.loginUser().getId();
        Page<RenewLog> result = renewLogService.lambdaQuery()
                .eq(nonNull(id), RenewLog::getCreateBy, id)
                .page(new Page<>(current, size));
        List<RenewLog> records = result.getRecords();
        if(result.getRecords().size() > INTEGER_ZERO) {
            Map<Long, User> uidMap = userService.searchMap(result.getRecords().stream().map(RenewLog::getCreateBy).collect(Collectors.toSet()));
            records.forEach(log -> {
                log.setUser(uidMap.get(log.getCreateBy()));
                log.setCreateTimeStr(DateUtil.formatDateTime(log.getCreateTime()));
            });
        }
        return Result.success(result);
    }


    /**
     * 我的邀请 > 累计付款
     */
    @GetMapping("/back/renew/log/me/count")
    public Result<Page<RenewLog>> searchMeInviteClientCumulativePaymentRecord(
            @RequestParam(required = false) String val,
            @RequestParam(required = false) Long createTime,
            @RequestParam(required = false, defaultValue = "1") Integer current,
            @RequestParam(required = false, defaultValue = "10")Integer size
    ) throws IllegalArgumentException, ReLoginException {
        userService.checkLoginUserIsSales();
        Date createTimeDate = nonNull(createTime) ? new Date(createTime) : null;

        //查询全部邀请的用户
        List<InviteUser> inviteUserList = inviteUserService.lambdaQuery().eq(InviteUser::getInitiatorUserId, LoginUser.getId()).list();
        Set<Long> allInviteUserIds = inviteUserList.stream().map(InviteUser::getInvitedUserId).collect(Collectors.toSet());

        // 如果有搜索条件，则先筛选邀请的用户
        Set<Long> filterUserIds = new HashSet<>();
        if(StringUtils.isNoneBlank(val)) {
            filterUserIds = userService.lambdaQuery().in(User::getId, allInviteUserIds).and(exe -> exe
                    .like(User::getName, val)
                    .or()
                    .like(User::getPhone, val)
            ).list().stream().map(User::getId).collect(Collectors.toSet());
            if(filterUserIds.size() == INTEGER_ZERO) return Result.success(new Page<>());
        }

        if(inviteUserList.size() > INTEGER_ZERO) {
            List<RenewLog> all = renewLogService.lambdaQuery()
                    .and(nonNull(createTimeDate), ext -> ext
                            .ge(RenewLog::getCreateTime, nonNull(createTimeDate) ? DateUtil.beginOfDay(createTimeDate) : null)
                            .lt(RenewLog::getCreateTime, nonNull(createTimeDate) ? DateUtil.endOfDay(createTimeDate) : null)
                    )
                    .in(allInviteUserIds.size() > INTEGER_ZERO, RenewLog::getCreateBy, StringUtils.isNotBlank(val) ? filterUserIds : allInviteUserIds)
                    .list();
            if(nonNull(all) && all.size() > INTEGER_ZERO) {

                Map<Long, BigDecimal> countMap = new HashMap<>();
                Map<Long, Integer> payNumberMap = new HashMap<>();
                all.forEach(log -> {
                    BigDecimal countMoney = countMap.getOrDefault(log.getCreateBy(), new BigDecimal(INTEGER_ZERO));
                    countMap.put(log.getCreateBy(), countMoney.add(log.getMoney()));
                    payNumberMap.put(log.getCreateBy(), payNumberMap.getOrDefault(log.getCreateBy(), INTEGER_ZERO) + INTEGER_ONE);
                });
                Set<Long> userIds = countMap.keySet();
                Map<Long, User> uidMap = userService.searchMap(userIds);
                Set<RenewLog> logs = userIds.stream().map(id -> {
                    RenewLog renewLog = new RenewLog();
                    renewLog.setMoney(countMap.get(id));
                    renewLog.setUser(uidMap.get(id));
                    renewLog.setPayNumber(payNumberMap.get(id));
                    return renewLog;
                }).collect(Collectors.toSet());
                return Result.success(PageUtil.execPage(current, size, new ArrayList<>(logs)));
            }
        }
        return Result.success(new Page<>());
    }

    /**
     * 我的邀请 > 付款记录
     */
    @GetMapping("/back/renew/log/me/list")
    public Result<Page<RenewLog>> searchMeInviteClientPaymentRecord(
            @RequestParam(required = false) String val,
            @RequestParam(required = false) Long createTime,
            @RequestParam(required = false, defaultValue = "1") Integer current,
            @RequestParam(required = false, defaultValue = "10")Integer size
    ) throws IllegalArgumentException, ReLoginException {
        userService.checkLoginUserIsSales();
        Long id = userService.loginUser().getId();
        Page<RenewLog> result = new Page<>(current, size);
        result.setRecords(new ArrayList<>());
        Date createTimeDate = nonNull(createTime) ? new Date(createTime) : null;

        // 查询当前用户的邀请记录
        List<InviteUser> inviteUserList = inviteUserService.lambdaQuery().eq(InviteUser::getInitiatorUserId, id).list();
        if(inviteUserList.size() > INTEGER_ZERO) {
            Set<Long> allInviteUserIds = inviteUserList.stream().map(InviteUser::getInvitedUserId).collect(Collectors.toSet());

            // 如果有搜索条件，则先筛选邀请的用户
            Set<Long> filterUserIds = new HashSet<>();
            if(StringUtils.isNoneBlank(val)) {
                filterUserIds = userService.lambdaQuery().in(User::getId, allInviteUserIds).and(exe -> exe
                        .like(User::getName, val)
                        .or()
                        .like(User::getPhone, val)
                ).list().stream().map(User::getId).collect(Collectors.toSet());
                if(filterUserIds.size() == INTEGER_ZERO) return Result.success(new Page<>());
            }

            if(inviteUserList.size() > INTEGER_ZERO) {
                result = renewLogService.lambdaQuery()
                        .and(nonNull(createTimeDate), ext -> ext
                                .ge(RenewLog::getCreateTime, nonNull(createTimeDate) ? DateUtil.beginOfDay(createTimeDate) : null)
                                .lt(RenewLog::getCreateTime, nonNull(createTimeDate) ? DateUtil.endOfDay(createTimeDate) : null)
                        )
                        .in(allInviteUserIds.size() > INTEGER_ZERO, RenewLog::getCreateBy, StringUtils.isNotBlank(val) ? filterUserIds : allInviteUserIds)
                        .orderByDesc(RenewLog::getCreateTime)
                        .page(new Page<>(current, size));
                List<RenewLog> records = result.getRecords();
                if(result.getRecords().size() > INTEGER_ZERO) {
                    Map<Long, User> uidMap = userService.searchMap(result.getRecords().stream().map(RenewLog::getCreateBy).collect(Collectors.toSet()));
                    records.forEach(log -> {
                        log.setUser(uidMap.get(log.getCreateBy()));
                        log.setCreateTimeStr(DateUtil.formatDateTime(log.getCreateTime()));
                    });
                }
            }
        }
        return Result.success(result);
    }
}
