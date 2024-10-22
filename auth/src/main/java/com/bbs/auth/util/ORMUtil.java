package com.bbs.auth.util;

import com.bbs.Result;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

public final class ORMUtil {
    private ORMUtil() {
        throw new RuntimeException("no create obj");
    }

    /**
     * 简洁事务
     *
     * @param funcBody 方法体
     */
    public static Result<Boolean> fastTran(Runnable funcBody, DataSourceTransactionManager tranManager, TransactionDefinition tranDef) {
        TransactionStatus transaction = tranManager.getTransaction(tranDef);
        try {
            funcBody.run();
            tranManager.commit(transaction);
            return Result.success();
        } catch (Exception e) {
            tranManager.rollback(transaction);
            return Result.failed(e.getCause().getMessage());
        }
    }
}