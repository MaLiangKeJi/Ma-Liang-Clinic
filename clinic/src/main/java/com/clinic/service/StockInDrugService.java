package com.clinic.service;

import cn.hutool.db.DbRuntimeException;
import com.baomidou.mybatisplus.extension.service.IService;
import com.clinic.dto.param.PutStockList;
import com.clinic.dto.param.PutStockParam;
import com.clinic.entity.StockIn;
import com.clinic.entity.StockInDrug;

/**
* @author 路晨霖
* @description 针对表【stock_in_drug(库存：入库 & 药品关联)】的数据库操作Service
* @createDate 2023-09-20 08:29:57
*/
public interface StockInDrugService extends IService<StockInDrug> {

    void saveBatch(String no, PutStockList param, StockIn stockIn) throws DbRuntimeException;


    Boolean save(String no, PutStockParam param, Long stockInID, Long drugNumber, Long stockBatchID);
}
