package com.clinic.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.clinic.dto.param.AddRetailParams;
import com.clinic.entity.Settings;
import com.clinic.entity.Stock;
import com.clinic.entity.StockBatch;
import com.clinic.entity.StockUnit;
import com.clinic.entity.Unit;
import com.clinic.mapper.StockBatchMapper;
import com.clinic.service.SettingsService;
import com.clinic.service.StockBatchService;
import com.clinic.util.LoginUser;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static java.util.Objects.nonNull;

/**
* @author 路晨霖
* @description 针对表【stock_batch】的数据库操作Service实现
* @createDate 2023-09-27 12:44:37
*/
@Service
public class StockBatchServiceImpl extends MPJBaseServiceImpl<StockBatchMapper, StockBatch>
    implements StockBatchService{

    private final SettingsService settingService;
    
    @Override
    public List<StockBatch> searchByApprovalNumbers(List<String> approvalNumbers, List<String> batchNumbers) {
        return lambdaQuery()
                .eq(StockBatch::getUserId, LoginUser.getId())
                .in(StockBatch::getApprovalNumber, approvalNumbers)
                .in(StockBatch::getBatchNumber, batchNumbers)
                .list();
    }

    @Override
    public StockBatch searchByApprovalNumber(String approvalNumber) {
        return lambdaQuery()
                .eq(StockBatch::getUserId, LoginUser.getId())
                .eq(StockBatch::getApprovalNumber, approvalNumber)
                .one();
    }

    @Override
    public StockBatch getByApprovalNumberAndBatchNumber(String approvalNumber, String batchNumber) {
        return lambdaQuery()
                .eq(StockBatch::getUserId, LoginUser.getId())
                .eq(StockBatch::getApprovalNumber, approvalNumber)
                .eq(StockBatch::getBatchNumber, batchNumber)
                .one();
    }

    @Override
    public StockBatch searchBatchAndUnitByID(Long id) {
        MPJLambdaWrapper<StockBatch> wrapper = new MPJLambdaWrapper<>();
        return wrapper
                .selectAll(StockBatch.class)
                .selectCollection(StockUnit.class, StockBatch::getStockUnitList)
                .leftJoin(StockUnit.class, StockUnit::getBatchId, StockBatch::getId)
                .selectAssociation(Unit.class, StockUnit::getUnit)
                .leftJoin(Unit.class, Unit::getId, StockUnit::getUnitId)
                .eq(StockBatch::getId, id)
                .one();
    }

    @Override
    public Boolean update(List<StockBatch> stockBatches) {
        return updateBatchById(stockBatches);
    }

    @Override
    public List<StockBatch> searchWaitUpdateStocks(AddRetailParams params) {
        MPJLambdaWrapper<StockBatch> wrapper = new MPJLambdaWrapper<>(StockBatch.class);
        wrapper
                .selectAll(StockBatch.class)
                .selectAssociation("t1", Stock.class, StockBatch::getStock)
                .leftJoin(Stock.class, Stock::getId, StockBatch::getStockId)
                .in(StockBatch::getId, params.getStockIDs())
                .eq(StockBatch::getUserId, LoginUser.getId())
        ;
        return baseMapper.selectJoinList(StockBatch.class, wrapper);
    }

    @Override
    public Boolean updateCost(Long id, BigDecimal cost, Integer unitId) {
        return lambdaUpdate()
                .set(StockBatch::getCost, cost)
                .set(StockBatch::getCostUnitId, unitId)
                .eq(StockBatch::getId, id)
        .update();
    }

    @Override
    public Boolean stockIsExpiry(StockBatch stockBatch) {
        Integer expiryAlertMonth = 3;
        if(nonNull(stockBatch.getExpiryDate())) {
            Settings settings = settingService.getByUserId();
            if(nonNull(settings)) {
                if(nonNull(settings.getExpiryAlertMonth())) {
                    expiryAlertMonth = settings.getExpiryAlertMonth();
                }
            }
            DateTime warningTime = DateUtil.offsetMonth(stockBatch.getExpiryDate(), - expiryAlertMonth);
            long between = DateUtil.between(warningTime, new Date(), DateUnit.DAY, false);
            return between > 0;
        }
        return false;
    }

    @Override
    public List<StockBatch> search(String val) {
        return search(val, null);
    }

    @Override
    public List<StockBatch> search(String val, Long userId) {
        return selectJoinList(StockBatch.class, new MPJLambdaWrapper<StockBatch>()
                .selectAll(StockBatch.class)
                .innerJoin(Stock.class, Stock::getId, StockBatch::getStockId)
                .eq(StockBatch::getUserId, nonNull(userId) ? userId : LoginUser.getId())
                .like(Stock::getName, val)
                .or()
                .like(Stock::getAlias, val)
                .or()
                .like(StockBatch::getApprovalNumber, val)
                .or()
                .like(StockBatch::getManufacturer, val)
                .or()
                .eq(StockBatch::getApprovalNumber, val)
        );
    }

    @Autowired
    public StockBatchServiceImpl(SettingsService settingService) {
        this.settingService = settingService;
    }
}




