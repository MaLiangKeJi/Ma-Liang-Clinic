package com.clinic.converter;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clinic.dto.PayRecordPatientDto;
import com.clinic.dto.param.GetPayParam;
import com.clinic.dto.param.IsPayRecordParam;
import com.clinic.dto.param.NoPayRecordParam;
import com.clinic.dto.param.ReturnPayRecordParam;
import com.clinic.dto.param.UpdatePayById;
import com.clinic.entity.Pay;
import com.clinic.enums.PayStateEnum;
import com.clinic.enums.PayWay;
import com.clinic.util.LoginUser;
import org.mapstruct.Mapper;

import java.util.Optional;

@Mapper(componentModel = "spring", imports = { Optional.class, PayStateEnum.class, PayWay.class, LoginUser.class})
public interface PayConverter {

    Page<PayRecordPatientDto> ToDtoPage(Page<Pay> page);

    PayRecordPatientDto payRecordToPayRecordDto(Pay pay);


    Pay toPayEntity(UpdatePayById param);

    GetPayParam toParam(IsPayRecordParam param);

    GetPayParam toParam(NoPayRecordParam param);

    GetPayParam toParam(ReturnPayRecordParam param);
}
