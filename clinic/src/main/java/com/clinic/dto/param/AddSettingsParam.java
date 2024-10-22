package com.clinic.dto.param;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@Data
public class AddSettingsParam {

    /**
     * 显示在处方单上的诊所名
     */
    private String alternateName;

    /**
     * 医师姓名
     */
    private String physician;


    /**
     * 科别：内科，中西医结合，中医
     */
    private String division;

    /**
     * 诊所名称
     */
    @NotNull(message = "诊所名称不能为空")
    private String clinicName;

    /**
     * 邀请码
     */
    private String inviteCode;

    /**
     * 诊所地址
     */
    private BigInteger provinceId;

    /**
     * 详细地址
     */
    private String addr;
}
