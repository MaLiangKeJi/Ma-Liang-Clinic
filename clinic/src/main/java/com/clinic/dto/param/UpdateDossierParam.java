package com.clinic.dto.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateDossierParam {

    /**
     * id
     */
    @NotNull(message = "ID不能为空！")
    private Long dossierId;

    /**
     * 既往史 (含药物过敏史)
     */
    @Length(max = 255, message = "既往史不能大于 255 个字符！")
    private String pastMedicalHistory;

    /**
     * 主诉
     */
    @NotBlank(message = "主诉信息必须填写！")
    @Length(max = 255, message = "主诉不能大于 255 个字符！")
    private String chiefComplaint;

    /**
     * 诊断
     */
    @Length(max = 255, message = "诊断不能大于 255 个字符！")
    private String diagnosis;

    /**
     * 现病史
     */
    @Length(max = 255, message = "现病史不能大于 255 个字符！")
    private String historyPresentIllness;

    /**
     * 查体
     */
    @Length(max = 255, message = "查体不能大于 255 个字符！")
    private String checkup;

    /**
     * 辅助检查
     */
    @Length(max = 255, message = "辅助检查不能大于 255 个字符！")
    private String auxiliaryCheckup;

    /**
     * 处理
     */
    @Length(max = 255, message = "处理不能大于 255 个字符！")
    private String dealWith;

    /**
     * 备注
     */
    @Length(max = 255, message = "备注不能大于 255 个字符！")
    private String notes;

}
