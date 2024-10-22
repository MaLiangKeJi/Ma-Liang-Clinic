package com.clinic.service.impl;

import cn.hutool.db.DbRuntimeException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clinic.app.AppStockService;
import com.clinic.dto.param.PutStockList;
import com.clinic.entity.Settings;
import com.clinic.entity.Stock;
import com.clinic.entity.StockBatch;
import com.clinic.entity.StockUnit;
import com.clinic.enums.DrugExpiryStateEnum;
import com.clinic.enums.DrugStockRule;
import com.clinic.enums.DrugTypeEnum;
import com.clinic.enums.StockStateCountTypeEnum;
import com.clinic.enums.StockStateEnum;
import com.clinic.mapper.StockMapper;
import com.clinic.service.SettingsService;
import com.clinic.service.StockBatchService;
import com.clinic.service.StockService;
import com.clinic.service.StockUnitService;
import com.clinic.util.LoginUser;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.clinic.enums.DrugExpiryStateEnum.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;

/**
* @author 路晨霖
* @description 针对表【stock(库存)】的数据库操作Service实现
* @createDate 2023-09-20 08:28:13
*/
@Slf4j
@Service
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock>
    implements StockService{

    private final StockUnitService stockUnitService;

    private final StockBatchService batchService;

    private final SettingsService settingsService;


    @Override
    public void saveBatch(PutStockList param) throws DbRuntimeException {
        Long uid = LoginUser.getId();

        Map<Stock, List<StockBatch>> oldStock = new HashMap<>(), newStock = new HashMap<>();

        List<StockBatch> stockBatches = batchService.searchByApprovalNumbers(param.getApprovalNumbers(), param.getBatchNumbers());
        Map<String, StockBatch> approvalNumberMap = new HashMap<>(stockBatches.size());
        stockBatches.forEach(stockBatch -> approvalNumberMap.put(stockBatch.getApprovalNumber(), stockBatch));

        // 增、存量
        param.getDrugs().forEach(inputDrug -> inputDrug.getBatchList().forEach(inputBatch -> {
            inputBatch.setType(DrugTypeEnum.map.get(inputBatch.getTypeName()).getCode());
            StockBatch stockBatch = approvalNumberMap.get(inputBatch.getApprovalNumber());
            List<StockBatch> stockBatchList = new ArrayList<>();
            if(nonNull(stockBatch)) {
                oldStock.putIfAbsent(inputDrug, stockBatchList);
                stockBatchList = oldStock.get(inputDrug);
                Long stockNumber = inputBatch.getNumber() + stockBatch.getNumber();

                inputBatch
                        .setId(stockBatch.getId())
                        .setNumber(stockNumber)
                        .setTotalNumber(stockNumber)
                        .setUserId(uid)
                ;

                stockBatchList.add(inputBatch);
            } else {
                newStock.putIfAbsent(inputDrug, stockBatchList);
                stockBatchList = newStock.get(inputDrug);
                inputBatch
                        .setTotalNumber(inputBatch.getNumber())
                        .setUserId(uid);
                stockBatchList.add(inputBatch);
            }
        }));
        // 存量处理
        boolean oldStockResult = true, newStockResult = true;
        if(MapUtils.isNotEmpty(oldStock)) {
            List<StockBatch> needUpdateOldBatch = oldStock.values().stream().flatMap(Collection::stream).distinct().collect(Collectors.toList());
            batchService.updateBatchById(needUpdateOldBatch);
        }

        // 增量处理
        if(MapUtils.isNotEmpty(newStock) && saveBatch(newStock.keySet())) {
            // 将 Stock 自增 ID 设置到每个 StockBatch，合并为一个大 List
            List<StockBatch> needSaveNewBatchList = newStock.entrySet().stream().map(entry ->
                    entry.getValue().stream().map(
                            stockBatch -> stockBatch.setStockId(entry.getKey().getId())
                    ).collect(Collectors.toList())
            ).flatMap(Collection::stream).distinct().collect(Collectors.toList());
            newStockResult = batchService.saveBatch(needSaveNewBatchList) && stockUnitService.saveBatch(
                    needSaveNewBatchList.stream().map(newBatch -> {
                        List<StockUnit> unitList = newBatch.getStockUnitList();
                        unitList.forEach(unit -> unit.setBatchId(newBatch.getId()));
                        return unitList;
                    }).flatMap(Collection::stream).distinct().collect(Collectors.toList())
            );
        }

        if(!oldStockResult || !newStockResult) {
            throw new DbRuntimeException("药品入库【库存修改 & 新增】异常！");
        }
    }


    @Override
    public void countStockState(StockBatch batch, Settings settings){
        if(isNull(settings) || isNull(settings.getCountVal())||isNull(settings.getStateCountRule())){
            batch.setState(StockStateEnum.UNDEFINED);
        } else {
            batch.setState(computeStockNumberState(batch, settings));
        }
    }

    public StockStateEnum computeStockNumberState(StockBatch batch, Settings settings) {
        return stockNumberStateIsNormal(
                batch.getNumber(),
                StockStateCountTypeEnum.map.get(settings.getStateCountRule()),
                batch.getTotalNumber(),
                settings.getCountVal()) ? StockStateEnum.NORMAL : StockStateEnum.SHORTAGE;
    }

    /**
     * 库存数量是否正常
     * @return 是否异常
     */
    @Override
    public Boolean stockNumberStateIsNormal(Long stockNumber, StockStateCountTypeEnum countType, Long totalNumber, Integer contVal) {
        boolean flag = false;
        switch (countType){
            case PERCENTAGE:
                if(stockNumber > 0) flag = stockNumber.equals(totalNumber) || ((double) stockNumber % totalNumber) * 100 > contVal;
                break;
            case NUMBER:
                flag = stockNumber > contVal;
                break;
        }
        return flag;
    }

    @Override
    public Stock searchByName(Stock stockParam) {
        return lambdaQuery().eq(Stock::getName, stockParam.getName()).one();
    }

    @Override
    public Boolean stockNumberStateIsNotNormal(StockBatch stockBatch) {
        CountSetting countSetting = CountSetting.create(stockBatch);

        if(trySetCountSettingIfAbsent(countSetting)) initCountSettingIfAbsent(countSetting);

        return !stockNumberStateIsNormal(
                stockBatch.getNumber(),
                StockStateCountTypeEnum.map.get(countSetting.countRule),
                stockBatch.getTotalNumber(),
                countSetting.countVal
        );
    }

    /**
     * 当统计设置为空时，尝试设置
     * @param countSetting 统计设置（统计类型， 统计值）
     * @return 设置是否失败
     */
    private Boolean trySetCountSettingIfAbsent(CountSetting countSetting) {
        if(isNull(countSetting.countRule) || isNull(countSetting.countVal) ) {
            Settings settings = settingsService.getByUserId();
            if(nonNull(settings)) {
                countSetting.countRule = settings.getStateCountRule();
                countSetting.countVal = settings.getCountVal();
                return false;
            }
        }
        return true;
    }

    /**
     * 当统计设置为空时，尝试初始化
     * @param countSetting 统计设置（统计类型， 统计值）
     */
    private void initCountSettingIfAbsent(CountSetting countSetting) {
        if(isNull(countSetting.countRule) || isNull(countSetting.countVal) ) {
            countSetting.countRule = StockStateCountTypeEnum.PERCENTAGE.getCode();
            countSetting.countVal = 20;
        }
    }

    @AllArgsConstructor
    private static class CountSetting {
        private Integer countRule;
        private Integer countVal;

        public static CountSetting create(StockBatch stockBatch) {
            return new CountSetting(stockBatch.getStateCountRule().getCode(), stockBatch.getCountVal());
        }
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DrugExpiryGroup {
        /**
         * 正常
         */
        private List<StockBatch> normal;
        /**
         * 即将到期
         */
        private List<StockBatch> aboutExpires;
        /**
         *过期
         */
        private List<StockBatch> expires;
        /**
         *库存不足
         */
        private List<StockBatch> stockShortage;
    }

    public DrugExpiryGroup countAndUpdateDrugExpiryState() {
        Settings settings = settingsService.getByUserId();
        Integer stockExpiryAlertMonth = settingsService.getUserSettingStockExpiryAlertMonth(settings);  //用户设置的库存药品过期提醒时间'

        List<StockBatch> allDrugBatch = batchService.selectJoinList(StockBatch.class, new MPJLambdaWrapper<StockBatch>()
                        .selectAll(StockBatch.class)
                        .selectAssociation(Stock.class, StockBatch::getName,ext->ext.result(Stock::getName))
                        .leftJoin(Stock.class, Stock::getId, StockBatch::getStockId)
                        .eq(StockBatch::getUserId, LoginUser.getId())
        );

        List<StockBatch> shortageStockDrugs = new ArrayList<>();                // 库存状态短缺
        List<StockBatch> expiresStateNormalStockDrugs = new ArrayList<>();      // 正常
        List<StockBatch> expiresStateStockDrugs = new ArrayList<>();            // 过期
        List<StockBatch> aboutExpiresStateStockDrugs = new ArrayList<>();       // 即将过期

        List<StockBatch> needUpdateStockStateToShortage = new ArrayList<>();    // 库存状态短缺（需要 update DB）
        List<StockBatch> needUpdateExpiresStateToNormal = new ArrayList<>();    // 正常（需要 update DB）
        List<StockBatch> needUpdateExpiresStateToExpires = new ArrayList<>();   // 过期（需要 update DB）
        List<StockBatch> needUpdateExpiresStateToAbout = new ArrayList<>();     // 即将过期（需要 update DB）

        allDrugBatch.forEach(drugBatch -> {
            Long stockNumber = drugBatch.getNumber();
            Long totalNumber = drugBatch.getTotalNumber();
            DrugStockRule stateCountRule = drugBatch.getStateCountRule();
            StockStateEnum stockState = drugBatch.getState();
            // 筛选出【库存状态】正常，且需要统计【库存预警状态】的库存批次
            if(StockStateEnum.NORMAL.equals(stockState) && !DrugStockRule.NOT_COUNT.equals(stateCountRule)) {
                Integer countVal = drugBatch.getCountVal();
                // 当库存数量不足【自定义阈值】时，提醒库存不足（库存数量按最小单位统计，如粒/片/袋等）
                if(DrugStockRule.MIN_UNIT_PERCENTAGE_CUSTOMIZE.equals(stateCountRule)) {
                    Integer countUnitId = drugBatch.getCountUnitId();
                    Integer unitId = drugBatch.getUnitId();
                    // 统计单位与库存单位是否一致，如果一致可以直接计算，否则需要将数量换算到一致的单位
                    if(countUnitId.equals(unitId)) {
                        if(stockNumber <= countVal) {
                            drugBatch.setState(StockStateEnum.SHORTAGE);
                            needUpdateStockStateToShortage.add(drugBatch);
                        }
                    } else {
                        // 取出当前库存批次，对应的库存单位（包含全部单位与进制）
                        List<StockUnit> currentStockBatchUnits = stockUnitService.lambdaQuery()
                                .eq(StockUnit::getBatchId, drugBatch.getId()).orderByAsc(StockUnit::getSort).list();
                        // 获取统计单位与库存单位
                        StockUnit countUnit = null;
                        StockUnit stockNumberUnit = null;
                        for (StockUnit stockUnit : currentStockBatchUnits) {
                            if (stockUnit.getUnitId().equals(countUnitId)) {
                                countUnit = stockUnit;
                            }
                            if (stockUnit.getUnitId().equals(unitId)) {
                                stockNumberUnit = stockUnit;
                            }
                        }
                        // 设置了统计单位后，才能进行计算
                        if(nonNull(countUnit) && nonNull(stockNumberUnit)) {
                            Integer countUnitSort = countUnit.getSort();
                            Integer stockNumberSort = stockNumberUnit.getSort();
                            long parentUnitStockNumber = 0L;
                            if(countUnit.getSort() < stockNumberUnit.getSort()) {
                                // 由库存单位（较大单位），向统计单位（较小单位）遍历（0, 统计单位, 库存单位）
                                for (int index = stockNumberSort; index > countUnitSort; index--) {
                                    parentUnitStockNumber = stockNumber / currentStockBatchUnits.get(index).getStepSize();
                                }
                            } else {
                                // 由统计单位（较大单位），向库存单位（较小单位）遍历（0, 库存单位, 统计单位）
                                for (int index = stockNumberSort; index < countUnitSort; index++) {
                                    //此处，需要 * 下一级单位的 stepSize
                                    // 比如，本单位为箱，有 50 箱，需要得出有多少瓶
                                    // （下一级单位瓶，下一级单位进制 stepSize：100，即 100 瓶 = 1 箱）
                                    // 总瓶数量 = 50 箱 * 100瓶
                                    parentUnitStockNumber = stockNumber * currentStockBatchUnits.get(index +1).getStepSize();
                                }
                            }
                            if(parentUnitStockNumber <= countVal) {
                                drugBatch.setState(StockStateEnum.SHORTAGE);
                                needUpdateStockStateToShortage.add(drugBatch);
                            }
                        }
                    }

                    // 当库存数量不足 20% 时，提醒库存不足（库存数量按最小单位统计，如粒/片/袋等）
                } else if(DrugStockRule.MIN_UNIT_PERCENTAGE_20.equals(stateCountRule)) {
                    if(stockNumber <= totalNumber * 0.2) {
                        drugBatch.setState(StockStateEnum.SHORTAGE);
                        needUpdateStockStateToShortage.add(drugBatch);
                    }

                    // 当库存数量不足 50% 时，提醒库存不足（库存数量按最小单位统计，如粒/片/袋等）
                } else if(DrugStockRule.MIN_UNIT_PERCENTAGE_50.equals(stateCountRule)) {
                    if(stockNumber <= totalNumber * 0.5) {
                        drugBatch.setState(StockStateEnum.SHORTAGE);
                        needUpdateStockStateToShortage.add(drugBatch);
                    }

                    // 当库存数量不足 80% 时，提醒库存不足（库存数量按最小单位统计，如粒/片/袋等）
                } else if(DrugStockRule.MIN_UNIT_PERCENTAGE_80.equals(stateCountRule)) {
                    if(stockNumber <= totalNumber * 0.8) {
                        drugBatch.setState(StockStateEnum.SHORTAGE);
                        needUpdateStockStateToShortage.add(drugBatch);
                    }
                }
            } else if(StockStateEnum.SHORTAGE.equals(stockState)) {
                shortageStockDrugs.add(drugBatch);
            }
            // 只重新统计【正常】【临期】库存，【已过期】不重新统计
            Integer expiryState = drugBatch.getExpiryState();
            DrugExpiryStateEnum computeExpiryState = AppStockService.computeDrugIsExpiry(drugBatch, stockExpiryAlertMonth);
            if(isNull(expiryState)) {                                       //空状态，第一次需要 update 状态
                switch (computeExpiryState) {
                    case NORMAL:
                        drugBatch.setExpiryState(NORMAL.getCode());
                        needUpdateExpiresStateToNormal.add(drugBatch);
                        break;
                    case ABOUT_EXPIRES:
                        drugBatch.setExpiryState(ABOUT_EXPIRES.getCode());
                        needUpdateExpiresStateToAbout.add(drugBatch);
                        break;
                    case EXPIRES:
                        drugBatch.setExpiryState(EXPIRES.getCode());
                        needUpdateExpiresStateToExpires.add(drugBatch);
                        break;
                }
            } else {
                if(computeExpiryState.getCode().equals(expiryState)) {      // 计算后状态相同，不需要更新
                    switch (computeExpiryState) {
                        case NORMAL:
                            expiresStateNormalStockDrugs.add(drugBatch);
                            break;
                        case ABOUT_EXPIRES:
                            aboutExpiresStateStockDrugs.add(drugBatch);
                            break;
                        case EXPIRES:
                            expiresStateStockDrugs.add(drugBatch);
                            break;
                    }
                } else {                                                    // 需要更新
                    switch (computeExpiryState) {
                        case NORMAL:
                            drugBatch.setExpiryState(NORMAL.getCode());
                            needUpdateExpiresStateToNormal.add(drugBatch);
                            break;
                        case ABOUT_EXPIRES:
                            drugBatch.setExpiryState(ABOUT_EXPIRES.getCode());
                            needUpdateExpiresStateToAbout.add(drugBatch);
                            break;
                        case EXPIRES:
                            drugBatch.setExpiryState(EXPIRES.getCode());
                            needUpdateExpiresStateToExpires.add(drugBatch);
                            break;
                    }
                }
            }
        });
        // 合并批量更新
        List<StockBatch> needUpdateList = new ArrayList<>();
        needUpdateList.addAll(updateExpiresState(needUpdateExpiresStateToNormal, needUpdateExpiresStateToExpires, needUpdateExpiresStateToAbout));
        needUpdateList.addAll(updateStockState(needUpdateStockStateToShortage));
        if(needUpdateList.size() > INTEGER_ZERO) {
            batchService.updateBatchById(needUpdateList);
        }
        expiresStateNormalStockDrugs.addAll(needUpdateExpiresStateToNormal);
        aboutExpiresStateStockDrugs.addAll(needUpdateExpiresStateToAbout);
        expiresStateStockDrugs.addAll(needUpdateExpiresStateToExpires);
        shortageStockDrugs.addAll(needUpdateStockStateToShortage);
        return new DrugExpiryGroup(expiresStateNormalStockDrugs, aboutExpiresStateStockDrugs, expiresStateStockDrugs, shortageStockDrugs);
    }

    private List<StockBatch> updateExpiresState(List<StockBatch> needUpdateExpiresStateToNormal, List<StockBatch> needUpdateExpiresStateToExpires, List<StockBatch> needUpdateExpiresStateToAbout) {
        List<StockBatch> needUpdateStockBatch = new ArrayList<>();
        if(needUpdateExpiresStateToNormal.size() > INTEGER_ZERO) {
            needUpdateStockBatch.addAll(needUpdateExpiresStateToNormal);
        }
        if(needUpdateExpiresStateToExpires.size() > INTEGER_ZERO) {
            needUpdateStockBatch.addAll(needUpdateExpiresStateToExpires);
        }
        if(needUpdateExpiresStateToAbout.size() > INTEGER_ZERO) {
            needUpdateStockBatch.addAll(needUpdateExpiresStateToAbout);
        }
        return needUpdateStockBatch;
    }

    private List<StockBatch> updateStockState(List<StockBatch> needUpdateStockStateToShortage) {
        List<StockBatch> needUpdateStockBatch = new ArrayList<>();
        if(needUpdateStockStateToShortage.size() > INTEGER_ZERO) {
            needUpdateStockBatch.addAll(needUpdateStockStateToShortage);
        }
        return needUpdateStockBatch;
    }


    @Autowired
    public StockServiceImpl(StockUnitService stockUnitService, StockBatchService batchService, SettingsService settingsService) {
        this.stockUnitService = stockUnitService;
        this.batchService = batchService;
        this.settingsService = settingsService;
    }
}




