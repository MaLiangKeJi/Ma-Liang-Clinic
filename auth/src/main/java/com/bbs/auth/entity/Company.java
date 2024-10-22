package com.bbs.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * 公司
 * @TableName company
 */
@TableName(value ="company")
@Data
public class Company implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 注册资本
     */
    @TableField(value = "registered_capital")
    private String registeredCapital;

    /**
     * 统一社会信用代码
     */
    @TableField(value = "code")
    private String code;

    /**
     * 营业执照照片 url
     */
    @TableField(value = "business_license")
    private String businessLicense;

    /**
     * 所属地区 ID（三级联动）
     */
    @TableField(value = "attribution_id")
    private Long attributionID;

    /**
     * 注册地址
     */
    @TableField(value = "registered_address")
    private String registeredAddress;

    /**
     * 行业（国标码）
     */
    @TableField(value = "norm_industry_id")
    private Long normIndustryId;

    @TableField(value = "admin_id")
    private Long adminId;

    @TableField(value = "expiration")
    private Date expiration;

    /**
     * 状态
     */
    @TableField(value = "state")
    private Integer state;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 信息创建人
     */
    @TableField(value = "create_by")
    private Long createBy;

    @TableField(exist = false)
    private User createUser;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 信息修改人
     */
    @TableField(value = "update_by")
    private Long updateBy;

    @TableField(exist = false)
    private User updateUser;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private Page<UserCompany> staffList;

    @TableField(exist = false)
    private User admin;


    @TableField(exist = false)
    private Integer staffNum;

    /**
     * 是否设置了公司结构
     */
    @TableField(exist = false)
    private Boolean isSetCompanyStructure;
}