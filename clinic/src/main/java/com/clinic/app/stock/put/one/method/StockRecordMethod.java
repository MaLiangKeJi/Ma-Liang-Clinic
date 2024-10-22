package com.clinic.app.stock.put.one.method;

import com.clinic.dto.param.PutStockParam;
import com.clinic.service.StockInDrugService;
import com.clinic.service.StockInService;
import com.clinic.util.LoginUser;

import static cn.hutool.extra.spring.SpringUtil.getBean;

public class StockRecordMethod {

    public static Long saveInStockRecord(String inStockNO, PutStockParam param) {
        StockInService service = getBean(StockInService.class);

        return service.save(inStockNO, param, LoginUser.getId());
    }

    public static void saveInStockDrugRecord(String inStockNO, PutStockParam param, Long stockInID, Long drugNumber,Long stockBatchID) {
        StockInDrugService service = getBean(StockInDrugService.class);

        service.save(inStockNO, param, stockInID, drugNumber, stockBatchID);
    }
}
