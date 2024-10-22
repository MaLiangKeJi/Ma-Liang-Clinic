package com.bbs.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 资源
 * @TableName resource
 */
@TableName(value ="resource")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resource implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 类型
     */
    @TableField(value = "type")
    private Long type;

    /**
     * 名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 编码
     */
    @TableField(value = "code")
    private String code;

    /**
     * 描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 值
     */
    @TableField(value = "value")
    private String value;

    /**
     * 父级 ID
     */
    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 父级
     */
    @TableField(exist = false)
    private Resource parent;

    /**
     * 状态
     */
    @TableField(value = "state")
    private Integer state;

    /**
     * 权重，用于同级排序
     */
    @TableField(value = "weight")
    private Integer weight;

    public String getIdStr() {
        return id.toString();
    }

    public Resource(Long type, String name, String code, String value) {
        this.type = type;
        this.name = name;
        this.code = code;
        this.value = value;
    }

    public Resource(Long type, String name, String code, String description, String value, Long parentId) {
        this.type = type;
        this.name = name;
        this.code = code;
        this.description = description;
        this.value = value;
        this.parentId = parentId;
    }

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}