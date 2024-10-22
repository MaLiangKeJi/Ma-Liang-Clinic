package com.clinic.app;

import com.clinic.converter.DrugLotConverter;
import com.clinic.dto.DrugLotVo;
import com.clinic.dto.param.DrugDetailParam;
import com.clinic.dto.param.DrugLotParam;
import com.clinic.entity.DrugDetail;
import com.clinic.entity.DrugLot;
import com.clinic.service.DrugDetailService;
import com.clinic.service.DrugLotService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class AppDrugService {

    private DrugLotService drugLotService;

    private DrugDetailService drugDetailService;

    private DrugLotConverter converter;



    public void deleteByIds(List<Long> ids) {
        drugLotService.removeBatchByIds(ids);
    }



    public List<DrugLotVo> selectDrugLot(DrugDetailParam param) {
        List<DrugDetail> drugDetails =  drugDetailService.selectByDrug(param);
        return converter.toDto(drugDetails);
    }

    @Transactional
    public Boolean inDrugLot(DrugLotParam param) {
        saveDrugLot(param);
        return true;
    }
    @Transactional
    public Boolean outDrugLot(DrugLotParam param) {
        updateInDrugInventory(param.getDrugs());
        saveDrugLot(param);
        return true;
    }

    private void updateInDrugInventory(List<DrugDetailParam> drugs) {
        drugs.forEach(drugParam -> {
            long num = drugParam.getInventory() - drugParam.getInOrOutQTY();
            drugDetailService.updateById(new DrugDetail(drugParam.getId(),num,
                    drugParam.getCost().multiply(new BigDecimal(drugParam.getInOrOutQTY())),
                    drugParam.getSellingPrice().multiply(new BigDecimal(drugParam.getInOrOutQTY()))));
            drugParam.setId(null);
        });
    }


    private void saveDrugLot(DrugLotParam param){
        DrugLot drugLot = converter.paramToEntity(param);
        List<DrugDetail> drugBatchDetails = converter.toDetailEntity(param.getDrugs());
        String lotNo = createLotNo();
        BigDecimal allPrice = drugBatchDetails.stream().map(detail -> {
            detail.setLotNo(lotNo);
            detail.setTotalCost(detail.getCost().multiply(new BigDecimal(detail.getInOrOutQTY())));
            detail.setTotalSellingPrice(detail.getSellingPrice().multiply(new BigDecimal(detail.getInOrOutQTY())));
            detail.setInventory(detail.getInOrOutQTY());
            return detail.getTotalSellingPrice();
        }).reduce(BigDecimal.valueOf(0.0), BigDecimal::add);
        drugLot.setPrice(allPrice);
        drugLot.setLotNo(lotNo);
        drugLotService.save(drugLot);
        drugDetailService.saveBatch(drugBatchDetails);
    }


    private String createLotNo() {
        long time = System.currentTimeMillis();
        int random = (int) (Math.random() * Integer.MAX_VALUE);
        UUID uuid = new UUID(time, random);
        return uuid.toString();
    }


    @Resource
    public void setDrugLotService(DrugLotService drugLotService) {
        this.drugLotService = drugLotService;
    }

    @Resource
    public void setDrugDetailService(DrugDetailService drugDetailService) {
        this.drugDetailService = drugDetailService;
    }

    @Resource
    public void setConverter(DrugLotConverter converter) {
        this.converter = converter;
    }
}
