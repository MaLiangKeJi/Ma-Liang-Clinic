package com.clinic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 辅助检查类型
 * @TableName auxiliary_type
 */
@TableName(value ="auxiliary_type")
@Data
@Accessors(chain = true)
public class AuxiliaryType implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 辅助检查类型名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 单位名称
     */
    @TableField(value = "unit_name")
    private String unitName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}