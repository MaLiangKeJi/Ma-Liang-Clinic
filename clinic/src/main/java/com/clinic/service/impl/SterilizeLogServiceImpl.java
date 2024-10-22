package com.clinic.service.impl;

import cn.hutool.db.DbRuntimeException;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bbs.Result;
import com.clinic.converter.LogConverter;
import com.clinic.dto.param.AddSterilizeLogParam;
import com.clinic.dto.param.SearchSterilizeLogParam;
import com.clinic.entity.SterilizeLog;
import com.clinic.mapper.SterilizeLogMapper;
import com.clinic.service.SterilizeLogService;
import com.clinic.util.log.LogUtil;
import com.clinic.util.LoginUser;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import javax.annotation.Resource;

import static java.util.Objects.nonNull;



/**
 * 消毒记录
 */
@Service
public class SterilizeLogServiceImpl extends ServiceImpl<SterilizeLogMapper, SterilizeLog>
    implements SterilizeLogService{

    private LogConverter converter;
    @Resource
    private DataSourceTransactionManager transactionManager;
    @Resource
    private TransactionDefinition transactionDefinition;

    @Override
    public Result<Boolean> add(AddSterilizeLogParam param) {
        SterilizeLog sterilizeLog = converter.toSterilizeEntity(param);
        sterilizeLog.setUserId(LoginUser.getId());
        TransactionStatus transaction = transactionManager.getTransaction(transactionDefinition);
        try {
            if(!save(sterilizeLog))throw new DbRuntimeException("添加失败！");
            LogUtil.Operation.addSterilize(sterilizeLog.getId(), "{}新增一条消毒记录：记录id={}", LoginUser.get().getName(), sterilizeLog.getId());
            transactionManager.commit(transaction);
            return Result.success();
        } catch (RuntimeException e) {
            transactionManager.rollback(transaction);
            return Result.failed(400, e.getMessage());
        }
    }

    @Override
    public Result<Page<SterilizeLog>> search(SearchSterilizeLogParam param) {
        LambdaQueryChainWrapper<SterilizeLog> wrapper = lambdaQuery();
        if(nonNull(param.getId())) {
            wrapper.eq(SterilizeLog::getId, param.getId());
        } else {
            if(nonNull(param.getCreateTime())) {
                wrapper.eq(SterilizeLog::getSterilizeTime, param.getCreateTime());
            } else {
                wrapper.between(
                        nonNull(param.getStartSterilizeTime()) && nonNull(param.getEndSterilizeTime()),
                        SterilizeLog::getSterilizeTime,
                        param.getStartSterilizeTime(), param.getEndSterilizeTime()
                );
            }
        }
        wrapper
                .eq(SterilizeLog::getUserId, LoginUser.get().getId())
                .eq(SterilizeLog::getIsDelete, 0);
        return Result.success(wrapper.page(param.toPage()));
    }

    @Resource
    public void setConverter(LogConverter converter) {
        this.converter = converter;
    }
}




