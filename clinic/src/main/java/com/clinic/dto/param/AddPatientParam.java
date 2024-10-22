package com.clinic.dto.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddPatientParam {

    /**
     * 姓名
     */
    @NotNull(message = "姓名不能为空！")
    private String name;

    /**
     * 性别
     */
    @Min(value = -1, message = "性别有误！")
    @Max(value = 2, message = "性别有误！")
    private Integer sex;

    /**
     * 手机号
     */
    @NotNull(message = "手机号不能为空！")
    private Long phone;

    /**
     * 年龄
     */
    @Min(value = 0, message = "最小年龄不能小于0！")
    @Max(value = 120, message = "最大年龄不能大于120！")
    private Integer age;

    /**
     * 出生年月
     */
    @NotNull(message = "出生年月不能为空！")
    private Date birthDate;

    /**
     * 婚姻情况：0未知（涵盖已婚未婚）；1未婚；2已婚
     */
    @Min(value = -1, message = "婚姻有误！")
    @Max(value = 3, message = "婚姻有误！")
    private Integer marriage;

    /**
     * 住址
     */
    private String address;

    /**
     * 证件号
     */
    private String idCard;

    /**
     * 备注
     */
    private String remark;

}
