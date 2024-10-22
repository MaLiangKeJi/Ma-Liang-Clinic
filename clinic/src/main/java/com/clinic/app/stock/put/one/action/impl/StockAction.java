package com.clinic.app.stock.put.one.action.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.clinic.app.stock.put.one.action.AddStockAction;
import com.clinic.app.stock.put.one.action.UpdateStockAction;
import com.clinic.cache.stock.drug.StockDrugCache;
import com.clinic.cache.stock.drug.StockDrugNameCache;
import com.clinic.dto.param.PutStockParam;
import com.clinic.entity.Stock;
import com.clinic.entity.StockBatch;
import com.clinic.service.StockBatchService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static cn.hutool.extra.spring.SpringUtil.getBean;
import static com.clinic.app.stock.put.one.method.StockMethod.*;
import static java.util.Objects.nonNull;

@Component
public class StockAction implements AddStockAction, UpdateStockAction {

    @Resource
    private StockDrugCache cache;

    @Override
    public Long add(PutStockParam param, String stockNo, Long drugNumber) {
        if(!(param.getStockUnit().size() > 0 && param.getStockUnit().size() <= 4)) {
            throw new IllegalArgumentException("单位结构数据不可用!");
        }
        Stock stock = stockConverter().toEntity(param);

        Long stockID = saveStockAndGetID(stock);

        Long stockBatchID = saveStockBatchAndGetID(param, stockID, drugNumber);
        saveUnit(param, stockBatchID);
        return stockBatchID;
    }

    private static Long saveStockAndGetID(Stock param) {

        Stock stock = stockService().searchByName(param);
        if(nonNull(stock)) {    // 库存中，是否有存在，相同名称的药品（不同厂家/规格）
            return stock.getId();
        } else {
            stockService().save(fillUserID(param));
            return param.getId();
        }
    }

    private static Long saveStockBatchAndGetID(PutStockParam param, Long stockID, Long drugNumber) {
        StockBatch stockBatch = converterAndFill(param, stockID, drugNumber);

        stockBatchService().save(stockBatch);

        SpringUtil.getBean(StockDrugNameCache.class).set(stockBatch);
        return stockBatch.getId();
    }


    @Override
    public Long update(PutStockParam param, StockBatch stock , Long drugNumber) {
        StockBatchService service = getBean(StockBatchService.class);

        service.updateById(fillNumber(drugNumber, stock));

        cache.remove(stock.getId());

        return stock.getId();
    }
}
