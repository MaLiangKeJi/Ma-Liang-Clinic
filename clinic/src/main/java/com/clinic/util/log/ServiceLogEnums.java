package com.clinic.util.log;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ServiceLogEnums {

    STOCK_ADD(1, "库存", "药品入库"),
    RETAIL(2, "零售", "出售药品"),
    ADMISSION(3, "接诊", "接诊病人"),
    PAY_ADD_ITEM(4, "收费", "增加收费项"),

    PAY_UPDATE_ITEM(5, "收费", "修改收费项"),
    PAY(6, "收费", "支付费用"),

    PRESCRIPTION_ADD(7, "处方", "开具处方"),

    PRESCRIPTION_UPDATE(8, "处方", "修改处方"),

    PRESCRIPTION_DOWNLOAD(9, "处方", "下载处方"),
    DIAGNOSIS_PROOF_ADD(10, "诊断证明", "开具证明"),
    DISINFECTION_ADD(11, "消杀", "增加记录"),
    STERILIZE_ADD(12, "消毒", "增加记录"),
    DOSSIER_ADD(13, "病例", "增加病例"),
    DOSSIER_UPDATE(14, "病例", "修改病人病例"),
    PATIENT_ADD(15, "病人", "增加病人信息"),
    PATIENT_UPDATE(16, "病人", "修改病人信息"),
    USER_SETTING_UPDATE(11, "用户设置", "修改设置"),
    ;

    private final Integer serviceCode;


    private final String title;

    private final String label;
}
