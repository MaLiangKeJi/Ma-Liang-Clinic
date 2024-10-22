package com.clinic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 库存
 * @TableName stock
 */
@TableName(value ="stock")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Stock implements Serializable {
    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户编号
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 药品别名
     */
    @TableField(value = "alias")
    private String alias;

    /**
     * 药品名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 拼音全拼
     */
    @TableField(value = "pin_yin")
    private String pinYin;

    /**
     * 拼音首字母
     */
    @TableField(value = "pin_yin_first_letter")
    private String pinYinFirstLetter;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 批次
     * 含不同厂家/不同批次/不同规格以及数量
     */
    @TableField(exist = false)
    private List<StockBatch> batchList;

    public Stock(Long userId, String name, List<StockBatch> batchList) {
        this.userId = userId;
        this.name = name;
        this.batchList = batchList;
    }
}