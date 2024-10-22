package com.clinic.app.stock.put.one.action.impl;

import com.clinic.app.stock.put.one.action.AddStockRecordAction;
import com.clinic.dto.param.PutStockParam;
import org.springframework.stereotype.Component;

import static com.clinic.app.stock.put.one.method.StockRecordMethod.*;

@Component
public class StockRecordAction implements AddStockRecordAction {

    @Override
    public void add(String inStockNO, PutStockParam param, Long drugNumber, Long stockBatchID) {
        Long stockInID = saveInStockRecord(inStockNO, param);
        saveInStockDrugRecord(inStockNO, param, stockInID, drugNumber,stockBatchID);
    }
}
