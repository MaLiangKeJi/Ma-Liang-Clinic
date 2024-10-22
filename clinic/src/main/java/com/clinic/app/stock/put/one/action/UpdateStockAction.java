package com.clinic.app.stock.put.one.action;

import com.clinic.dto.param.PutStockParam;
import com.clinic.entity.StockBatch;

@FunctionalInterface
public interface UpdateStockAction {

    Long update(PutStockParam param, StockBatch stock , Long drugNumber);
}
