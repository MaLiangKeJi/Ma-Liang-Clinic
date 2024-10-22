package com.clinic.service.impl;

import cn.hutool.db.DbRuntimeException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bbs.Result;
import com.clinic.dto.param.QueryDiagnosisProofParam;
import com.clinic.dto.vo.DiagnosisProofFileVo;
import com.clinic.entity.Clinic;
import com.clinic.entity.DiagnosisProof;
import com.clinic.mapper.DiagnosisProofMapper;
import com.clinic.service.DiagnosisProofService;
import com.clinic.util.log.LogUtil;
import com.clinic.util.LoginUser;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import javax.annotation.Resource;

/**
 *
 */
@Service
public class DiagnosisProofServiceImpl extends MPJBaseServiceImpl<DiagnosisProofMapper, DiagnosisProof>
    implements DiagnosisProofService{

    @Resource
    private DataSourceTransactionManager transactionManager;
    @Resource
    private TransactionDefinition transactionDefinition;
    @Override
    public Result<Long> add(DiagnosisProof param) {
        param.setUserId(LoginUser.getId());
        TransactionStatus transaction = transactionManager.getTransaction(transactionDefinition);
        try {
            if(!save(param))throw new DbRuntimeException("添加失败！");
            LogUtil.Operation.addDiagnosisProof(null, param.getId(), "{}新增一条诊断证明：证明id={}", LoginUser.get().getName(), param.getId());
            transactionManager.commit(transaction);
            return Result.success(param.getId());
        } catch (RuntimeException e) {
            transactionManager.rollback(transaction);
            return Result.failed(400, e.getMessage());
        }
    }

    @Override
    public Result<Page<DiagnosisProof>> queryList(QueryDiagnosisProofParam param) {
        return Result.success(lambdaQuery()
                .eq(DiagnosisProof::getUserId,LoginUser.getId())
                .likeRight(StringUtils.isNotBlank(param.getVal()),DiagnosisProof::getName,param.getVal())
                .eq(StringUtils.isNotBlank(param.getCreateTime()),DiagnosisProof::getVisitDate, param.getCreateTime())
                .page(param.toPage()));
    }

    @Override
    public DiagnosisProofFileVo getFileInfo(Long id) {
        return selectJoinOne(DiagnosisProofFileVo.class,
                new MPJLambdaWrapper<DiagnosisProof>()
                        .selectAll(DiagnosisProof.class)
                        .selectAs(Clinic::getName,DiagnosisProofFileVo::getClinicName)
                        .leftJoin(Clinic.class,Clinic::getUserId,DiagnosisProof::getUserId)
                        .eq(DiagnosisProof::getId,id));
    }
}




