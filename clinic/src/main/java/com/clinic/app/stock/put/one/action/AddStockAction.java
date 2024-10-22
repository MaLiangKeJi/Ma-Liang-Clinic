package com.clinic.app.stock.put.one.action;

import com.clinic.dto.param.PutStockParam;

@FunctionalInterface
public interface AddStockAction {

    Long add(PutStockParam param, String stockNo, Long drugNumber);
}
