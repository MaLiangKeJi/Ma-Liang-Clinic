package com.clinic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clinic.entity.Drug;
import com.clinic.entity.PrescriptionDrug;
import com.clinic.entity.StockBatch;
import com.clinic.entity.StockUnit;
import com.clinic.entity.Unit;
import com.clinic.mapper.PrescriptionDrugMapper;
import com.clinic.service.PrescriptionDrugService;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class PrescriptionDrugServiceImpl extends MPJBaseServiceImpl<PrescriptionDrugMapper, PrescriptionDrug>
    implements PrescriptionDrugService{


    public List<PrescriptionDrug> searchDrug(Long prescriptionId) {
        List<PrescriptionDrug> list = baseMapper.selectJoinList(PrescriptionDrug.class, new MPJLambdaWrapper<PrescriptionDrug>()
                .selectAll(PrescriptionDrug.class)
                .selectCollection(StockBatch.class, PrescriptionDrug::getBatchList,batch -> batch
                        .collection(StockUnit.class, StockBatch::getStockUnitList, stockUnit -> stockUnit
                                .collection(Unit.class, StockUnit::getUnitList))
                        .collection(Drug.class,StockBatch::getDrug))
                .leftJoin(StockBatch.class,StockBatch::getId,PrescriptionDrug::getStockBatchId)
                .leftJoin(StockUnit.class, StockUnit::getBatchId, StockBatch::getId)
                .leftJoin(Drug.class,Drug::getApprovalNumber,StockBatch::getApprovalNumber)
                .leftJoin(Unit.class, Unit::getId, StockUnit::getUnitId)
                .eq(PrescriptionDrug::getPrescriptionId, prescriptionId)
        );
        return list;
    }






}