package com.clinic.app.drug;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bbs.Result;
import com.clinic.cache.unit.UnitCache;
import com.clinic.converter.StockConverter;
import com.clinic.dto.vo.PrescriptionSearchDrugVO;
import com.clinic.entity.*;
import com.clinic.enums.DrugExpiryStateEnum;
import com.clinic.enums.StockStateEnum;
import com.clinic.service.DrugService;
import com.clinic.service.SettingsService;
import com.clinic.service.StockBatchService;
import com.clinic.util.LoginUser;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ONE;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;

@RestController
@RequestMapping
public class Search {

    @Resource
    private StockBatchService stockBatchService;
    @Resource
    private UnitCache unitCache;
    @Resource
    private SettingsService settingsService;
    @Resource
    private StockConverter converter;
    @Resource
    private DrugService drugService;

    private static final Integer LIMIT = 50;

    private static final Integer SEARCH_DRUG_LIMIT = 20;

    /**
     * 药品查询
     */
    @Transactional
    @GetMapping("/prescription/drug")
    public Result<List<PrescriptionSearchDrugVO>> search(
            @RequestParam(required = false) String name
    ) {
        List<StockBatch> stocks = searchStock(name);
        List<PrescriptionSearchDrugVO> vos;
        if(stocks.size() > LIMIT) {
            stocks = stocks.subList(INTEGER_ZERO, LIMIT);
            vos = fillUnitAndUpdateStockStateAndExpireState(stocks);
        } else {
            vos = fillUnitAndUpdateStockStateAndExpireState(stocks);
            Page<Drug> drugPage = drugService.search(name, new Page<>(INTEGER_ONE, SEARCH_DRUG_LIMIT));
            vos.addAll(converter.toPrescriptionSearchDrugVOList(drugPage.getRecords()));
        }
        return Result.success(vos);
    }

    private List<StockBatch> searchStock(String name) {
        MPJLambdaWrapper<StockBatch> queryWrapper = new MPJLambdaWrapper<StockBatch>()
                .selectAll(StockBatch.class)
                .leftJoin(Stock.class, Stock::getId, StockBatch::getStockId, ext -> ext
                        .selectAssociation(Stock.class, StockBatch::getStock)
                )
                .leftJoin(StockUnit.class, StockUnit::getBatchId, StockBatch::getId, ext -> ext
                        .selectCollection(StockUnit.class, StockBatch::getStockUnitList)
                )
                .selectAssociation(Stock.class , StockBatch::getName,ext->ext.result(Stock::getName))
                .leftJoin(Stock.class, "st",Stock::getId, StockBatch::getStockId)
                .eq(StockBatch::getUserId, LoginUser.getId())
                ;

        if(StringUtils.isNotBlank(name)) {
            // 如果是纯英文，进行拼音或首字母模糊匹配
            if (name.matches("[a-zA-Z]+")) {
                queryWrapper.and(
                        ext -> ext
                                .likeRight(Stock::getPinYin, name)              //全拼
                                .or()
                                .likeRight(Stock::getPinYinFirstLetter, name)  //拼音首字母
                );
            } else {
                // 否则进行普通 LIKE 查询
                queryWrapper.and(
                        ext -> ext
                                .like(Stock::getName, name)     //药品名称
                                .or()
                                .like(Stock::getAlias, name)    //自定义别名
                );
            }
        }

        return stockBatchService.selectJoinList(StockBatch.class, queryWrapper);
    }

    /**
     * 填充单位 & 尝试更新库存状态、过期状态
     */
    private List<PrescriptionSearchDrugVO> fillUnitAndUpdateStockStateAndExpireState(List<StockBatch> stockBatches) {
        Set<Integer> unitIds = stockBatches.stream().map(stockBatch -> {
            Set<Integer> set = new HashSet<>();
            set.add(stockBatch.getSingleDoseUnitId());
            set.add(stockBatch.getUnitId());
            set.add(stockBatch.getCountUnitId());
            set.add(stockBatch.getCostUnitId());
            set.addAll(stockBatch.getStockUnitList().stream().map(StockUnit::getUnitId).collect(Collectors.toList()));
            return set;
        }).flatMap(Collection::stream).filter(Objects::nonNull).collect(Collectors.toSet());

        Map<Integer, Unit> unitMap = unitCache.getUnitMap(unitIds);

        List<StockBatch> needUpdateStockState = new ArrayList<>();

        Settings settings = settingsService.getByUserId();
        Integer stockExpiryAlertMonth = settingsService.getUserSettingStockExpiryAlertMonth(settings);  //用户设置的库存药品过期提醒时间


        List<PrescriptionSearchDrugVO> result = new ArrayList<>();
        for (StockBatch stockBatch : stockBatches) {
            stockBatch.setSingleDoseUnit(unitMap.get(stockBatch.getSingleDoseUnitId()));
            stockBatch.setUnit(unitMap.get(stockBatch.getUnitId()));
            stockBatch.setCountUnit(unitMap.get(stockBatch.getCountUnitId()));
            stockBatch.setCostUnit(unitMap.get(stockBatch.getCostUnitId()));
            List<Unit> units = new ArrayList<>();
            stockBatch.getStockUnitList().forEach(stockUnit -> {
                Unit unit = unitMap.get(stockUnit.getUnitId());
                stockUnit.setUnit(unit);
                units.add(unit);
            });
            stockBatch.setUnits(units);
            // 计算库存状态
            StockStateEnum stockStateEnum = stockBatch.computeStockState();
            if(nonNull(stockStateEnum)) {
                stockBatch.setState(stockStateEnum);
                needUpdateStockState.add(stockBatch);
            }
            // 计算过期状态
            DrugExpiryStateEnum expiryStateEnum = stockBatch.computeExpiryState(stockExpiryAlertMonth);
            if(nonNull(expiryStateEnum)) {
                stockBatch.setExpiryState(expiryStateEnum.getCode());
                needUpdateStockState.add(stockBatch);
            }

            PrescriptionSearchDrugVO prescriptionSearchDrugVO = converter.toPrescriptionSearchDrugVO(stockBatch);
            prescriptionSearchDrugVO.setIsStock(true);
            prescriptionSearchDrugVO.setStockNumber(stockBatch.getNumber());
            prescriptionSearchDrugVO.setStockNumberUnit(stockBatch.getUnit().getName());
            prescriptionSearchDrugVO.setExpiryDate(DateUtil.format(stockBatch.getExpiryDate(), "yyyy/MM/dd"));
            List<PrescriptionSearchDrugVO.Unit> voUnits = prescriptionSearchDrugVO.getUnits();
            prescriptionSearchDrugVO.setMinUnit(voUnits.get(INTEGER_ZERO));
            prescriptionSearchDrugVO.setMaxUnit(voUnits.get(voUnits.size() - INTEGER_ONE));
            prescriptionSearchDrugVO.setSingleDoseUnit(stockBatch.getSingleDoseUnit()==null ?null:stockBatch.getSingleDoseUnit().getName());
            prescriptionSearchDrugVO.setStockState(stockBatch.getState().getCode());
            prescriptionSearchDrugVO.setExpiryState(stockBatch.getExpiryState());
            prescriptionSearchDrugVO.setStateCountRule(stockBatch.getStateCountRule().getCode());
            result.add(prescriptionSearchDrugVO);
        }
        if(needUpdateStockState.size() > INTEGER_ZERO) stockBatchService.updateBatchById(needUpdateStockState);
        return result;
    }
}
