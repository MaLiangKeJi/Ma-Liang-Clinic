package com.clinic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;


@TableName(value ="unit")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UnitCascade {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("parent_id")
    private Integer parentId;

    /**
     * 父级编号
     */
    private String name;

    /**
     * 是否未默认模板（0 非默认，1 默认）
     */
    @TableField("is_default")
    private Integer defaultState;

    /**
     * 子级
     */
    @TableField(exist = false)
    private List<UnitCascade> children;
}
