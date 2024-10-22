package com.clinic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 诊所设置
 * @TableName settings
 */
@TableName(value ="settings")
@Data
public class Settings implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 过期预提醒时间（月）
     */
    @TableField(value = "expiry_alert_month")
    private Integer expiryAlertMonth;

    /**
     * 库存统计规则
     */
    @TableField(value = "state_count_rule")
    private Integer stateCountRule;

    /**
     * 统计值（统计方式值，如百分比 10%； 数量）
     */
    @TableField(value = "count_val")
    private Integer countVal;

    /**
     * 统计单位(0最小单位1最大单位)
     */
    @TableField(value = "count_unit")
    private Integer countUnit;

    /**
     * 诊所名称
     */
    @TableField(value = "clinic_name")
    private String clinicName;

    /**
     * 科别：内科，外科，中医
     */
    @TableField(value = "division")
    private String division;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 创建日期
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 显示在处方单上的诊所名
     */
    @TableField(value = "alternate_name")
    private String alternateName;

    /**
     * 医师姓名
     */
    @TableField(value = "physician")
    private String physician;

    /**
     * 邀请方用户id
     */
    @TableField(value = "invite_uid")
    private Long inviteUid;

    /**
     * 营业天数列表JSON字符串
     */
    @TableField(value = "business_day")
    private String businessDay;

    /**
     * 营业时间列表JSON字符串
     */
    @TableField(value = "business_time")
    private String businessTime;

    /**
     * 诊所地址 id
     */
    @TableField(value = "province_id")
    private BigInteger provinceId;

    /**
     * 详细地址
     */
    @TableField(value = "addr")
    private String addr;

    /**
     * 是否由药房收费，0否1是
     */
    @TableField(value = "is_pharmacy_pay")
    private Integer isPharmacyPay;

    /**
     * 营业天数列表
     */
    @TableField(exist = false)
    private List<String> businessDayList;

    /**
     * 营业时间列表
     */
    @TableField(exist = false)
    private List<Map<Integer, List<String>>> businessTimeList;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}