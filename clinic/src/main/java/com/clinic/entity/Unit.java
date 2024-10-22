package com.clinic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 单位（级联, tree 结构）
 * @TableName unit
 */
@TableName(value ="unit")
@Data
public class Unit implements Serializable {
    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 展示内容
     */
    @TableField(value = "name")
    private String name;

    /**
     * 父级编号
     */
    @TableField(value = "parent_id")
    private Integer parentId;

    /**
     * 排序字段
     */
    @TableField(exist = false)
    private Integer sort;

    /**
     * 1个父级单位对应的本单位数量(如1箱对应8瓶); 默认0代表最大单位
     */
    @TableField(exist = false)
    private Long stepSize;

    /**
     * 是否为默认模板（0 非默认，1 默认）
     */
    @TableField(value = "is_default")
    private Integer defaultState;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}