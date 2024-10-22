package com.clinic.app.search;

import com.bbs.Result;
import com.clinic.cache.stock.drug.StockDrugCache;
import com.clinic.cache.stock.drug.StockDrugNameCache;
import com.clinic.cache.unit.UnitCache;
import com.clinic.entity.Stock;
import com.clinic.entity.StockBatch;
import com.clinic.entity.Unit;
import com.clinic.service.StockService;
import com.clinic.service.impl.StockServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;

@RequestMapping
@RestController
public class SearchExpiredStockDrugs {

    @Resource
    private StockService stockService;

    @Resource
    private UnitCache unitCache;

    /**
     * 查询库存即将过期药品
     */
    @GetMapping("/search/stock/expired")
    public Result<StockServiceImpl.DrugExpiryGroup> search() {
        StockServiceImpl.DrugExpiryGroup drugExpiryGroup = stockService.countAndUpdateDrugExpiryState();
        List<StockBatch> aboutExpires = drugExpiryGroup.getAboutExpires();
        List<StockBatch> expires = drugExpiryGroup.getExpires();
        Set<Long> stockIds = new HashSet<>();
        Set<Integer> unitIds = new HashSet<>();
        if(aboutExpires.size() > INTEGER_ZERO) {
            stockIds.addAll(aboutExpires.stream().map(stockBatch -> {
                unitIds.add(stockBatch.getUnitId());
                return stockBatch.getStockId();
            }).collect(Collectors.toSet()));
        }
        if(expires.size() > INTEGER_ZERO) {
            stockIds.addAll(expires.stream().map(stockBatch -> {
                unitIds.add(stockBatch.getUnitId());
                return stockBatch.getStockId();
            }).collect(Collectors.toSet()));
        }
        if(stockIds.size() > INTEGER_ZERO) {
            Map<Integer, Unit> unitMap = unitCache.getUnitMap(unitIds);
            Map<Long, Stock> stockMap = stockService.listByIds(stockIds).stream().collect(Collectors.toMap(Stock::getId, stock -> stock));
            if(aboutExpires.size() > INTEGER_ZERO) {
                aboutExpires.forEach(stockBatch -> {
                    stockBatch.setStock(stockMap.get(stockBatch.getStockId()));
                    stockBatch.setUnit(unitMap.get(stockBatch.getUnitId()));
                });
            }
            if(expires.size() > INTEGER_ZERO) {
                expires.forEach(stockBatch -> {
                    stockBatch.setStock(stockMap.get(stockBatch.getStockId()));
                    stockBatch.setUnit(unitMap.get(stockBatch.getUnitId()));
                });
            }
        }
        return Result.success(drugExpiryGroup);
    }
}
