package com.bbs.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bbs.auth.app.user.Search;
import com.bbs.auth.entity.Fan;
import com.bbs.auth.mapper.FanMapper;
import com.bbs.auth.service.FanService;
import com.bbs.auth.service.UserService;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

/**
 *
 */
@Service
public class FanServiceImpl extends ServiceImpl<FanMapper, Fan> implements FanService {

    @Resource
    private TransactionDefinition transactionDefinition;
    @Resource
    private DataSourceTransactionManager transactionManager;

    @Lazy
    @Resource
    private UserService userService;

    @Override
    public Boolean create(Fan fan) {
        TransactionStatus transaction = transactionManager.getTransaction(transactionDefinition);
        try {
            List<Fan> list = lambdaQuery().eq(Fan::getUserId, fan.getUserId()).eq(Fan::getFollowUserId, fan.getFollowUserId()).list();
            if(list == null || list.size() == 0) {
                return save(fan);
            } else {
                if(list.size() == 1) {
                    Long id = list.get(NumberUtils.INTEGER_ZERO).getId();
                    return lambdaUpdate().set(Fan::getDeleteFlag, NumberUtils.INTEGER_ZERO).eq(Fan::getId, id).update();
                } else {
                    // 删除存量数据
                    List<Long> needRemoveIds = list.subList(NumberUtils.INTEGER_ZERO, list.size() - 1)
                            .stream().map(Fan::getId).collect(Collectors.toList());
                    Fan last = list.get(list.size() - NumberUtils.INTEGER_ONE);
                    if(removeByIds(needRemoveIds) && lambdaUpdate().set(Fan::getDeleteFlag, NumberUtils.INTEGER_ZERO).eq(Fan::getId, last.getId()).update()) {
                        transactionManager.commit(transaction);
                        return true;
                    } else {
                        transactionManager.rollback(transaction);
                        return false;
                    }
                }
            }
        } catch (TransactionException e) {
            transactionManager.rollback(transaction);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delFollow(Long uid, Long followUserID) {
        lambdaUpdate().set(Fan::getDeleteFlag, 1).eq(Fan::getUserId, uid).eq(Fan::getFollowUserId, followUserID).update();
    }



    @Override
    public List<Fan> getFollow(Long userId) {
        return lambdaQuery().eq(Fan::getDeleteFlag,0).eq(Fan::getUserId,userId).list();
    }

    @Override
    public List<Fan> getFan(Long userId) {
        return lambdaQuery().eq(Fan::getDeleteFlag,0).eq(Fan::getFollowUserId,userId).list();
    }

    @Override
    public Boolean isFollow(Long followUserID) {
        return lambdaQuery()
                .eq(Fan::getDeleteFlag,0)
                .eq(Fan::getUserId, userService.loginUser().getId())
                .eq(Fan::getFollowUserId, followUserID).exists();
    }

    @Override
    public void fillFollowStatus(List<Long> ids, List<Search.VO> fillObjs) {
        List<Fan> fans = lambdaQuery()
                .eq(Fan::getDeleteFlag, NumberUtils.INTEGER_ZERO)
                .eq(Fan::getUserId, userService.loginUser().getId())
                .in(Fan::getFollowUserId, ids)
                .list();
        if(nonNull(fans) && fans.size() > NumberUtils.INTEGER_ZERO && nonNull(fillObjs) && fillObjs.size() > NumberUtils.INTEGER_ZERO) {
            Map<Long, Fan> idAndFanMap = fans.stream().collect(Collectors.toMap(Fan::getUserId, fan -> fan));
            for (Search.VO vo: fillObjs) {
                if(nonNull(vo)) {
                    Fan fan = idAndFanMap.get(vo.getId());
                    vo.setIsFollow(nonNull(fan));
                }
            }
        }
    }

    @Override
    public Long count(Long userId) {
        return lambdaQuery().eq(Fan::getDeleteFlag,0).eq(Fan::getFollowUserId,userId).count();
    }
}




