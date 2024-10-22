package com.clinic.dto.param;

import com.bbs.vo.BaseParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class PatientParam extends BaseParam {


    /**
     *  用户ID
     */
    private Long userId;

    /**
     * 编号
     */
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别
     */
    @Min(value = -1, message = "性别有误！")
    @Max(value = 2, message = "性别有误！")
    private Integer sex;

    /**
     * 年龄
     */
    @Min(value = 0, message = "最小年龄不能小于0！")
    @Max(value = 120, message = "最大年龄不能大于120！")
    private Integer age;

    /**
     * 婚姻情况：0未知（涵盖已婚未婚）；1未婚；2已婚
     */
    @Min(value = -1, message = "婚姻有误！")
    @Max(value = 3, message = "婚姻有误！")
    private Integer marriage;

    /**
     * 出生年月
     */
    private Date birthDate;

    /**
     * 手机号
     */
    private Long phone;

    /**
     * 住址
     */
    private String address;

    /**
     * 备注
     */
    private String remark;

    /**
     * 最近就诊时间
     */
    private String last_visit;

    /**
     * 就诊时间：范围起始
     */
    private String start;

    /**
     * 就诊时间：范围结束
     */
    private String end;
}
