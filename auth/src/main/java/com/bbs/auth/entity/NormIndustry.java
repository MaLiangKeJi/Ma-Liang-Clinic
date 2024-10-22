package com.bbs.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 标准行业表(GB/T 4754-2017)
 * @TableName norm_industry
 */
@TableName(value ="norm_industry")
@Data
public class NormIndustry implements Serializable {
    /**
     * 自增数据ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 行业编号
     */
    @TableField(value = "code")
    private String code;

    /**
     * 行业名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 父行业编号
     */
    @TableField(value = "parent_code")
    private String parentCode;

    /**
     * 是否有子节点：1是，0否
     */
    @TableField(value = "has_child")
    private Integer hasChild;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}