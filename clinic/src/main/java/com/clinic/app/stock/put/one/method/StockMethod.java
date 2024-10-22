package com.clinic.app.stock.put.one.method;

import cn.hutool.extra.spring.SpringUtil;
import com.clinic.converter.StockConverter;
import com.clinic.converter.UnitConverter;
import com.clinic.dto.param.PutStockParam;
import com.clinic.entity.Stock;
import com.clinic.entity.StockBatch;
import com.clinic.entity.StockUnit;
import com.clinic.enums.DrugStockRule;
import com.clinic.enums.DrugTypeEnum;
import com.clinic.service.StockBatchService;
import com.clinic.service.StockService;
import com.clinic.service.StockUnitService;
import com.clinic.util.LoginUser;

import java.util.List;
import java.util.stream.Collectors;

import static cn.hutool.extra.spring.SpringUtil.getBean;

public class StockMethod {


    public static StockConverter stockConverter() {
        return getBean(StockConverter.class);
    }

    public static StockBatchService stockBatchService() {
        return SpringUtil.getBean(StockBatchService.class);
    }

    public static StockService stockService() {
        return SpringUtil.getBean(StockService.class);
    }

    public static Stock fillUserID(Stock param) {
        param.setUserId(LoginUser.getId());
        return param;
    }

    public static StockBatch converterAndFill(PutStockParam param, Long stockID, Long drugNumber) {
        StockConverter converter = SpringUtil.getBean(StockConverter.class);
        return converter.toBatchEntity(param)
                .setUserId(LoginUser.getId())
                .setStockId(stockID)
                .setNumber(drugNumber)
                .setTotalNumber(drugNumber)
                .setUnitId(getMinUnit(param).getId())
                .setStateCountRule(DrugStockRule.map.get(param.getCountType()))
                .setType(DrugTypeEnum.map.get(param.getSort()).getCode())
                ;
    }

    private static PutStockParam.StockUnitParam getMinUnit(PutStockParam param) {
        List<PutStockParam.StockUnitParam> stockUnit = param.getStockUnit();
        return stockUnit.get(stockUnit.size() - 1);
    }

    public static Boolean saveUnit(PutStockParam param, Long stockBatchID) {
        StockUnitService service = SpringUtil.getBean(StockUnitService.class);

        return service.saveBatch(fillUnitLackProperty(converter(param), stockBatchID));
    }

    private static List<StockUnit> converter(PutStockParam param) {
        UnitConverter converter = SpringUtil.getBean(UnitConverter.class);

        return param.getStockUnit().stream().map(unit -> {
            StockUnit stockUnit = converter.toEntity(unit);
            stockUnit.setId(null);
            return stockUnit;
        }).collect(Collectors.toList());
    }

    /**
     * 填充缺少的属性
     * @param units 单位列表
     * @param stockBatchID 批次ID
     * @return 单位列表
     */
    private static List<StockUnit> fillUnitLackProperty(List<StockUnit> units, Long stockBatchID) {
        for (int index = 0; index < units.size(); index++) {
            StockUnit unit = units.get(index);
            fillBatchId(unit, stockBatchID);
            fillSort(unit, index);
        }
        return units;
    }

    private static void fillBatchId(StockUnit unit, Long batchId) {
        unit.setBatchId(batchId);
    }

    private static void fillSort(StockUnit unit, int index) {
        unit.setSort(index);
    }

    /**
     * 填充数量
     */
    public static StockBatch fillNumber(Long drugNumber, StockBatch stock) {
        StockBatch stockBatch = new StockBatch();
        stockBatch.setId(stock.getId());
        stockBatch.setNumber(drugNumber);
        return stockBatch;
    }

    /**
     * 计算最小单位的药品数量
     */
    public static Long computeMinUnitDrugNumber(PutStockParam param) {
        return param.getStockUnit().stream()
                .mapToLong(PutStockParam.StockUnitParam::getStepSize)
                .reduce(1, (a, b) -> a * b);
    }
}
