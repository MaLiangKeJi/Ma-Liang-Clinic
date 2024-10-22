package com.clinic.dto.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddSterilizeLogParam {

    /**
     * 消毒时间
     */
    @NotNull
    private Date sterilizeTime;

    /**
     * 消毒部位
     */
    private String content;

    /**
     * 消毒方法
     */
    private String method;

    /**
     * 消毒剂
     */
    private String disinfector;

    /**
     * 消毒人
     */
    private String executor;
}
