package com.clinic.app.search;

import com.bbs.Result;
import com.clinic.entity.Stock;
import com.clinic.entity.StockBatch;
import com.clinic.service.StockService;
import com.clinic.service.impl.StockServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;

@RequestMapping
@RestController
public class SearchUnderStockDrugs {

    @Resource
    private StockService stockService;

    /**
     * 查询库存即将过期药品
     */
    @GetMapping("/search/stock/under")
    public Result<List<StockBatch>> search() {
        StockServiceImpl.DrugExpiryGroup drugExpiryGroup = stockService.countAndUpdateDrugExpiryState();
        List<StockBatch> stockShortage = drugExpiryGroup.getStockShortage();
        if(stockShortage.size() > INTEGER_ZERO) {
            Map<Long, Stock> stockMap = stockService.listByIds(stockShortage.stream().map(StockBatch::getStockId).collect(Collectors.toSet()))
                    .stream().collect(Collectors.toMap(Stock::getId, stock -> stock));
            stockShortage.forEach(stockBatch -> {
                stockBatch.setStock(stockMap.get(stockBatch.getStockId()));
            });
        }
        return Result.success(stockShortage);
    }
}
