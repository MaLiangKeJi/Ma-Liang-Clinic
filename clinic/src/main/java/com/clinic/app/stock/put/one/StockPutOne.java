package com.clinic.app.stock.put.one;

import com.bbs.Result;
import com.clinic.app.stock.put.one.action.AddStockAction;
import com.clinic.app.stock.put.one.action.AddStockRecordAction;
import com.clinic.app.stock.put.one.action.UpdateStockAction;
import com.clinic.dto.param.PutStockParam;
import com.clinic.entity.StockBatch;
import com.clinic.service.StockBatchService;
import com.clinic.util.log.LogUtil;
import com.clinic.util.LoginUser;
import com.clinic.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.bbs.Result.success;
import static com.clinic.app.stock.put.one.method.StockMethod.computeMinUnitDrugNumber;
import static java.util.Objects.nonNull;

@RestController
@RequestMapping
public class StockPutOne {
    private static final String STOCK_NO_GENERATE = "STOCK_NO_GENERATE";

    private final StockBatchService batchService;
    private final RedisUtil redis;

    private final UpdateStockAction updateStockAction;

    private final AddStockAction addStockAction;

    private final AddStockRecordAction addStockRecordAction;

    @Transactional
    @PutMapping("/stock")
    public Result<String> put(@RequestBody PutStockParam param) {
        String stockNo = generateStockInNO();
        putStock(param, stockNo);
        LogUtil.Operation.addStock(stockNo, "{}新增一条药品库存：库存批次编号={}", LoginUser.get().getName(), stockNo);
        return success(stockNo);
    }

    private String generateStockInNO() {
        return String.valueOf(
                LoginUser.getId()) +
                System.currentTimeMillis() +
                redis.increment(STOCK_NO_GENERATE)
                ;
    }

    private StockBatch search(PutStockParam param) {
        return batchService.getByApprovalNumberAndBatchNumber(param.getApprovalNumber(), param.getBatchNumber());
    }

    private Boolean exists(StockBatch stock) {
        return nonNull(stock);
    }

    private void putStock(PutStockParam param, String stockNo) {
        StockBatch stockBatch = search(param);

        Long addDrugNumber = computeMinUnitDrugNumber(param);   //计算需要增加的药品数量（转换为最小单位）
        Long stockBatchID;
        if(exists(stockBatch)) {
            Long drugNumber = getUpdateDrugNumber(addDrugNumber, stockBatch);
            stockBatchID = updateStockAction.update(param, stockBatch, drugNumber);
        } else {
            stockBatchID = addStockAction.add(param, stockNo, addDrugNumber);
        }
        putStockRecord(param, stockNo, addDrugNumber, stockBatchID);
    }

    private Long getUpdateDrugNumber(Long stockDrugNumber, StockBatch stockBatch) {
        return getUpdateDrugNumber(stockDrugNumber, stockBatch.getNumber());
    }

    private Long getUpdateDrugNumber(Long stockDrugNumber, Long addDrugNumber) {
        return stockDrugNumber + addDrugNumber;
    }

    private void putStockRecord(PutStockParam param, String stockNo, Long drugNumber, Long stockBatchID) {
        addStockRecordAction.add(stockNo, param, drugNumber,stockBatchID);
    }

    @Autowired
    public StockPutOne(StockBatchService batchService, RedisUtil redis, UpdateStockAction updateStockAction, AddStockAction addStockAction, AddStockRecordAction addStockRecordAction) {
        this.batchService = batchService;
        this.redis = redis;
        this.updateStockAction = updateStockAction;
        this.addStockAction = addStockAction;
        this.addStockRecordAction = addStockRecordAction;
    }
}
