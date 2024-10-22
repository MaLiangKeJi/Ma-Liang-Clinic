package com.clinic.app.retail;

import com.clinic.dto.param.AddRetailParams;
import com.clinic.entity.RetailDrugRecord;
import com.clinic.entity.RetailRecord;
import com.clinic.entity.StockBatch;

import java.util.List;

public class Method {


    /**
     * 购买的药品，库存是否都有
     * @param params AddRetailParams
     * @param stockBatches search DB result
     * @return 是否都有
     */
    public static Boolean updateTargetStockIsPresent(AddRetailParams params, List<StockBatch> stockBatches) {
        return stockBatches.size() == params.getStockIDs().size();
    }

    /**
     *
     * 计算售卖后的库存数量
     * @param retailDrugRecord RetailDrugRecord::number
     * @param stockBatch StockBatch::number
     * @return 售卖后的库存数量
     */
    public static Long computeSellAfterStockNumber(RetailDrugRecord retailDrugRecord, StockBatch stockBatch) {
        return stockBatch.getNumber() - retailDrugRecord.getNumber();
    }

    /**
     * 校验售卖后的库存数量是否正常
     * @param stockNumber 库存数量
     * @return 售卖后的库存数量是否正常
     */
    public static Boolean sellAfterStockNumberIsNormal(Long stockNumber) {
        return stockNumber >= 0;
    }

    /**
     * 填充 RetailId
     * @param retailDrugRecords RetailDrugRecords
     * @param retailRecord RetailRecord
     */
    public static void fillRetailIdToDrugRecords(List<RetailDrugRecord> retailDrugRecords, RetailRecord retailRecord) {
        retailDrugRecords.forEach(drug -> drug.setRetailId(retailRecord.getId()));
    }
}
