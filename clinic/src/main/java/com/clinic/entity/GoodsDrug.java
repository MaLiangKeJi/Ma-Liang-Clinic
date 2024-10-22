package com.clinic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 商品：药品
 * @TableName goods_drug
 */
@TableName(value ="goods_drug")
@Data
public class GoodsDrug implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 药品ID
     */
    @TableField(value = "drug_id")
    private Long drugId;

    /**
     * 展示名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 价格
     */
    @TableField(value = "price")
    private Integer price;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}