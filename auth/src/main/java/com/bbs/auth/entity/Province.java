package com.bbs.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigInteger;

import lombok.Data;

/**
 * 省市县三级联动（参考https://github.com/uiwjs/province-city-china?tab=readme-ov-file）
 * @TableName province
 */
@TableName(value ="province")
@Data
public class Province implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private BigInteger id;

    /**
     * 编码
     */
    @TableField(value = "code")
    private BigInteger code;

    /**
     * 名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 省份
     */
    @TableField(value = "province")
    private String province;

    /**
     * 城市
     */
    @TableField(value = "city")
    private String city;

    /**
     * 地区
     */
    @TableField(value = "area")
    private String area;

    /**
     * 城镇
     */
    @TableField(value = "town")
    private String town;

    /**
     * 层级
     */
    @TableField(value = "tier")
    private Integer tier;

    /**
     * 父级ID
     */
    @TableField(value = "parent_id")
    private BigInteger parentId;

    /**
     * 父级Code
     */
    @TableField(value = "parent_code")
    private BigInteger parentCode;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}