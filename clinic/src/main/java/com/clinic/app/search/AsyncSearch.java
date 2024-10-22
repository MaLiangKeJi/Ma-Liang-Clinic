package com.clinic.app.search;

import com.clinic.entity.Drug;
import com.clinic.entity.Patient;
import com.clinic.entity.StockBatch;
import com.clinic.service.DrugService;
import com.clinic.service.PatientService;
import com.clinic.service.StockBatchService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

import static java.util.Objects.isNull;

@Component
public class AsyncSearch {

    @Resource
    private PatientService patientService;

    @Resource
    private DrugService drugService;

    @Resource
    private StockBatchService stockBatchService;

    @Async("asyncMethodThreadPool")
    public void search(String val, GlobalSearch.VO vo, CountDownLatch countDownLatch, Long userId) {
        List<Patient> patients = vo.getPatients();//搜病人
        if(isNull(patients)) {
            patients = new ArrayList<>();
            vo.setPatients(patients);
        }

        List<Drug> drugs = vo.getDrugs();//搜药品
        if(isNull(drugs)) {
            drugs = new ArrayList<>();
            vo.setDrugs(drugs);
        }

        List<StockBatch> stockBatches = vo.getStockBatches();//搜库存
        if(isNull(stockBatches)) {
            stockBatches = new ArrayList<>();
            vo.setStockBatches(stockBatches);
        }


        patients.addAll(patientService.selectByName(val, userId));  //搜病人
        drugs.addAll(drugService.searchByName(val));    //搜药品
        stockBatches.addAll(stockBatchService.search(val, userId)); //查库存
        countDownLatch.countDown();
    }
}
