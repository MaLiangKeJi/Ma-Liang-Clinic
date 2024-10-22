package com.clinic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clinic.dto.param.DrugDetailParam;
import com.clinic.entity.DrugDetail;
import com.clinic.mapper.DrugDetailMapper;
import com.clinic.service.DrugDetailService;
import com.clinic.util.LoginUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 *
 */
@Service
public class DrugDetailServiceImpl extends ServiceImpl<DrugDetailMapper, DrugDetail>
    implements DrugDetailService {

    @Override
    public List<DrugDetail> selectByDrug(DrugDetailParam param) {
        return  lambdaQuery()
                .eq(StringUtils.isNotBlank(param.getName()), DrugDetail::getName, param.getName())
                .eq(Objects.nonNull(param.getType()), DrugDetail::getType, param.getType())
                .list();
    }



    @Override
    public List<DrugDetail> selectInfoByLot() {
        return lambdaQuery().gt(DrugDetail::getInventory,0).list();
    }

    @Override
    public List<DrugDetail> search(String val) {
        return lambdaQuery()
                .eq(DrugDetail::getUserId, LoginUser.getId())
                .like(DrugDetail::getLotNo,val)
                .or()
                .like(DrugDetail::getName, val)
                .or()
                .like(DrugDetail::getOrigin, val)
                .or()
                .like(DrugDetail::getManufacturer, val)
                .or()
                .like(DrugDetail::getDrugNo, val)
                .list();
    }


//    @Override
//    public List<DrugDetail> selectInfoDistinct() {
//        return lambdaQuery()
//                .select(DrugDetail::getName,DrugDetail::getType,DrugDetail::getSize,DrugDetail::getUnit,DrugDetail::getOrigin,
//                        DrugDetail::getManufacturer,DrugDetail::getDrugNo,DrugDetail::getCost,DrugDetail::getExpiryDate)
//                .gt(DrugDetail::getInventory,0)
//                .groupBy(DrugDetail::getName,DrugDetail::getType,DrugDetail::getSize,DrugDetail::getUnit,DrugDetail::getOrigin,
//                        DrugDetail::getManufacturer,DrugDetail::getDrugNo,DrugDetail::getCost,DrugDetail::getExpiryDate)
//                .list();
//    }


}




