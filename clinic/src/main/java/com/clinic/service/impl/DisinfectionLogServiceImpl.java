package com.clinic.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.DbRuntimeException;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bbs.Result;
import com.clinic.converter.LogConverter;
import com.clinic.dto.param.AddDisinfectionLogParam;
import com.clinic.dto.param.SearchDisinfectionLogParam;
import com.clinic.entity.DisinfectionLog;
import com.clinic.mapper.DisinfectionLogMapper;
import com.clinic.service.DisinfectionLogService;
import com.clinic.util.LoginUser;
import com.clinic.util.log.LogUtil;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 消杀记录
 */
@Service
public class DisinfectionLogServiceImpl extends ServiceImpl<DisinfectionLogMapper, DisinfectionLog>
    implements DisinfectionLogService{

    private LogConverter converter;
    @Resource
    private DataSourceTransactionManager transactionManager;
    @Resource
    private TransactionDefinition transactionDefinition;

    @Override
    public Result<Boolean> add(@RequestBody @Valid  AddDisinfectionLogParam param) {
        DisinfectionLog log = converter.toDisinfectionEntity(param);
        log.setUserId(LoginUser.getId());
        log.setTimeRange(DateUtil.format(param.getStartTimeRange(), "HH:mm:ss") + "~" + DateUtil.format(param.getEndTimeRange(), "HH:mm:ss"));
        TransactionStatus transaction = transactionManager.getTransaction(transactionDefinition);
        try {
            if(!save(log))throw new DbRuntimeException("添加失败！");
            LogUtil.Operation.addDisinfection(log.getId(), "{}新增一条消杀记录：记录id={}", LoginUser.get().getName(), log.getId());
            transactionManager.commit(transaction);
            return Result.success(true);
        } catch (RuntimeException e) {
            transactionManager.rollback(transaction);
            return Result.failed(400, e.getMessage());
        }
    }

    @Override
    public Result<Page<DisinfectionLog>> search(SearchDisinfectionLogParam param) {
        LambdaQueryChainWrapper<DisinfectionLog> wrapper = lambdaQuery();
        wrapper.eq(DisinfectionLog::getUserId, LoginUser.getId());
        wrapper.eq(StrUtil.isNotBlank(param.getCreateTime()),DisinfectionLog::getCreateTime, param.getCreateTime());
        return Result.success(wrapper.page(param.toPage()));
    }

    @Resource
    public void setConverter(LogConverter converter) {
        this.converter = converter;
    }
}




