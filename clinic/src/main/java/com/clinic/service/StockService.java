package com.clinic.service;

import cn.hutool.db.DbRuntimeException;
import com.baomidou.mybatisplus.extension.service.IService;
import com.clinic.dto.param.PutStockList;
import com.clinic.entity.Settings;
import com.clinic.entity.Stock;
import com.clinic.entity.StockBatch;
import com.clinic.enums.StockStateCountTypeEnum;
import com.clinic.service.impl.StockServiceImpl;

/**
* @author 路晨霖
* @description 针对表【stock(库存)】的数据库操作Service
* @createDate 2023-09-20 08:28:13
*/
public interface StockService extends IService<Stock> {

    void saveBatch(PutStockList param) throws DbRuntimeException;

    void countStockState(StockBatch stock, Settings settings);

    Stock searchByName(Stock stockParam);

    /**
     * 库存数量是否异常
     * @param stockBatch 库存批次
     * @return 库存数量是否异常
     */
    Boolean stockNumberStateIsNotNormal(StockBatch stockBatch);

    /**
     * 库存数量是否正常
     * @param stockNumber 库存数量
     * @param countType 统计方式
     * @param totalNumber 总数
     * @param contVal 统计值
     * @return 库存数量是否正常
     */
    Boolean stockNumberStateIsNormal(Long stockNumber, StockStateCountTypeEnum countType, Long totalNumber, Integer contVal);


    /**
     * 统计并更更新库存药品状态
     */
    StockServiceImpl.DrugExpiryGroup countAndUpdateDrugExpiryState();
}
