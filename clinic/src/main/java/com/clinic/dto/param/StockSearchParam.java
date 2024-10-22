package com.clinic.dto.param;


import com.bbs.vo.BaseParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockSearchParam extends BaseParam {

    /**
     * 编号
     */
    private Long id;

    /**
     * 批注文号
     */
    private String approvalNumber;

    /**
     * 药品名
     */
    private String name;

    /**
     * 产品批号
     */
    private String batchNumber;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 生产单位名称
     */
    private String manufacturerName;

    /**
     * 剂型
     */
    private String dosageForm;


    /**
     * 库存状态（正常、不足）
     */
    private Integer state;


    /**
     * 生产日期
     */
    private Date produceDate;

    /**
     * 生产日期（起始）
     */
    private Date produceStartDate;

    /**
     * 生产日期（结束）
     */
    private Date produceEndDate;

    /**
     * 过期日期
     */
    private Date expiryDate;

    /**
     * 过期日期（起始）
     */
    private Date expiryStartDate;

    /**
     * 过期日期（结束）
     */
    private Date expiryEndDate;
}
