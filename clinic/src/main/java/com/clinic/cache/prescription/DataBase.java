package com.clinic.cache.prescription;

import com.clinic.dto.vo.PrescriptionSearchDrugVO;
import com.clinic.service.PrescriptionService;
import com.clinic.service.StockBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DataBase {
    private final PrescriptionService prescriptionService;

    private final StockBatchService stockBatchService;

    private final Redis redis;

    public List<PrescriptionSearchDrugVO> search(List<Long> emptyStockIds) {
        List<PrescriptionSearchDrugVO> vos =
                stockBatchService.listByIds(emptyStockIds).stream()
                        .map(prescriptionService::converter).collect(Collectors.toList());
        redis.loadToCache(vos); // 加载到 Redis
        return vos;
    }

    @Autowired
    public DataBase(PrescriptionService prescriptionService, StockBatchService stockBatchService, Redis redis) {
        this.prescriptionService = prescriptionService;
        this.stockBatchService = stockBatchService;
        this.redis = redis;
    }
}
