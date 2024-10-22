package com.clinic.app.retail;

import cn.hutool.core.collection.CollUtil;
import com.bbs.Result;
import com.clinic.converter.PatientConverter;
import com.clinic.converter.RetailConverter;
import com.clinic.dto.param.AddRetailParams;
import com.clinic.entity.Drug;
import com.clinic.entity.Patient;
import com.clinic.entity.RetailDrugRecord;
import com.clinic.entity.RetailRecord;
import com.clinic.entity.StockBatch;
import com.clinic.service.DrugService;
import com.clinic.service.PatientService;
import com.clinic.service.RetailDrugRecordService;
import com.clinic.service.RetailRecordService;
import com.clinic.service.StockBatchService;
import com.clinic.util.LoginUser;
import com.clinic.util.log.LogUtil;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.util.validation.metadata.DatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.clinic.app.retail.Method.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Slf4j
@RestController
@RequestMapping
public class Add {

    private RetailConverter converter;

    private final RetailRecordService recordService;

    private final RetailDrugRecordService drugRecordService;

    private final StockBatchService stockService;

    private final DataSourceTransactionManager transactionManager;

    private final TransactionDefinition transactionDefinition;

    @Resource
    private PatientService patientService;

    @Resource
    private PatientConverter patientConverter;

    @Resource(name = "protoStuffTemplate")
    private RedisTemplate<String, String> redis;

    @Resource
    private DrugService drugService;

    private static final String ADD_RETAIL_KEY = "lock_add_retail_lock";

    @Transactional
    @PutMapping("/retail")
    public Result<Boolean> add(@RequestBody @Valid AddRetailParams params) {
        exec(params);
        return Result.success();
    }

    private Boolean exec(AddRetailParams params) throws DatabaseException {
        RetailRecord retailRecord = converter.toEntity(params);
        retailRecord.setUserId(LoginUser.getId());

        List<RetailDrugRecord> retailDrugRecords = converter.toEntity(params.getDrugList());

        Patient patient = savePatient(params, retailRecord);     // 保存顾客 ID（病人）

        Map<Long, StockBatch> stockBatcheMap = myStockIdMap(params);

        List<StockBatch> waitUpdateStockBatch = new ArrayList<>();
        for (RetailDrugRecord retailDrugRecord : retailDrugRecords) {
            // 判断【售卖的药品】是否属于库存中
            if (sellDrugIsStock(retailDrugRecord)) {
                StockBatch stockBatch = stockBatcheMap.get(retailDrugRecord.getStockBatchId());
                retailDrugRecord.fillStockBatchInfo(stockBatch);

                computeAndFillStockDrugNumber(retailDrugRecord, stockBatch);    // 计算并填充销售后的药品数量

                waitUpdateStockBatch.add(stockBatch);
            } else {
                // 填充药品信息到零售药品记录
                fillDrugInfoToRetailDrugRecord(retailDrugRecord);
            }
        }
        // 更新库存药品数量
        if(isNeedUpdateStock(waitUpdateStockBatch)) updateStock(waitUpdateStockBatch);

        if(!recordService.save(retailRecord)) throw new DatabaseException("零售记录入库失败");

        fillRetailIdToDrugRecords(retailDrugRecords, retailRecord);

        if(!drugRecordService.saveBatch(retailDrugRecords))  throw new DatabaseException("零售药品记录入库失败");
        LogUtil.Operation.retailDrug(nonNull(patient) ? patient.getId() : null, retailRecord.getTotalPrice(), "{}新增一条零售记录：零售记录id={}", LoginUser.get().getName(), retailRecord.getId());
        return true;
    }

    private void computeAndFillStockDrugNumber(RetailDrugRecord retailDrugRecord, StockBatch stockBatch) {
        long number = computeSellAfterStockNumber(retailDrugRecord, stockBatch);    // 计算药品更新后的数量

        Preconditions.checkArgument(sellAfterStockNumberIsNormal(number), "库存数量不足，无法执行操作");

        stockBatch.setNumber(number);
    }

    private void fillDrugInfoToRetailDrugRecord(RetailDrugRecord retailDrugRecord) {
        Drug drug = drugService.getById(retailDrugRecord.getStockBatchId());
        retailDrugRecord.fillDrugInfo(drug);
    }

    private Boolean isNeedUpdateStock(List<StockBatch> waitUpdateStockBatch) {
        return CollUtil.isNotEmpty(waitUpdateStockBatch);
    }

    private void updateStock(List<StockBatch> waitUpdateStockBatch) throws DatabaseException {
        if(!stockService.update(waitUpdateStockBatch)) throw new DatabaseException("库存数量更新失败");
    }

    private Patient savePatient(AddRetailParams params, RetailRecord retailRecord) {
        Patient patient = null;
        Long phone = params.getPhone();
        if(nonNull(phone)) {
            patient = patientService.selectByPhone(String.valueOf(phone));
            if(isNull(patient)) {
                patient = patientConverter.toEntity(retailRecord);
                patientService.save(patient);
            }
            retailRecord.setPatientId(patient.getId());
        }
        return patient;
    }

    private Map<Long, StockBatch> myStockIdMap(AddRetailParams params) {
        List<StockBatch> stockBatches = stockService.searchWaitUpdateStocks(params);
        return stockBatches.stream().collect(Collectors.toMap(StockBatch::getId, stockBatch -> stockBatch));
    }

    private Boolean sellDrugIsStock(RetailDrugRecord retailDrugRecord) {
        Boolean isStock = retailDrugRecord.getIsStock();
        return nonNull(isStock) && isStock;
    }

    private Boolean tryAcquireLock(String lockKey) {
        return redis.opsForValue().setIfAbsent(lockKey, LoginUser.getId().toString());
    }

    private void removeLock(String lockKey) {
        redis.delete(lockKey);
    }

    @Resource
    public void setConverter(RetailConverter converter) {
        this.converter = converter;
    }

    @Autowired
    public Add(RetailRecordService recordService, RetailDrugRecordService drugRecordService, StockBatchService stockService, DataSourceTransactionManager transactionManager, TransactionDefinition transactionDefinition) {
        this.recordService = recordService;
        this.drugRecordService = drugRecordService;
        this.stockService = stockService;
        this.transactionManager = transactionManager;
        this.transactionDefinition = transactionDefinition;
    }
}
