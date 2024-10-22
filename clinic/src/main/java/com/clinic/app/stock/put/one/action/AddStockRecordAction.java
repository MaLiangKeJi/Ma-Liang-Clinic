package com.clinic.app.stock.put.one.action;

import com.clinic.dto.param.PutStockParam;

@FunctionalInterface
public interface AddStockRecordAction {

    void add(String inStockNO, PutStockParam param, Long drugNumber, Long stockBatchID);
}
