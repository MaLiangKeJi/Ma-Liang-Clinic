package com.clinic.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 库存：入库
 * @TableName stock_in
 */
@Data
public class QueryStockInDto{

    /**
     * 编号
     */
    private Long id;

    /**
     * 入库编号
     */
    private String no;

    /**
     * 总价
     */
    private BigDecimal totalCost;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date createTime;

    @TableField(exist = false)
    private QueryStockInDrugDto stockInDrugs;



}