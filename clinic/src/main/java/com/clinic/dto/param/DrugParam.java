package com.clinic.dto.param;

import com.bbs.vo.BaseParam;
import lombok.Data;

import java.util.Date;

@Data
public class DrugParam extends BaseParam {
    /**
     * id
     */
    private Long id;

    /**
     * 药品名称
     */
    private String name;

    /**
     * 库存状态
     */
    private Integer stockStatus;

    /**
     * 标签 ID
     */
    private Long tagId;

    /**
     * 入库时间（起始）
     */
    private Date storageStartTime;

    /**
     * 入库时间（结束）
     */
    private Date storageEndTime;

    /**
     * 生产日期（起始）
     */
    private Date productionStartDate;

    /**
     * 生产日期（结束）
     */
    private Date productionEndDate;

    /**
     * 批准文号
     */
    private String approvalNumber;

    /**
     * 药品编码
     */
    private String drugNo;

    /**
     * 生产单位
     */
    private String manufacturer;

    /**
     * 剂型（药品类型）
     */
    private String type;

    /**
     * 规格
     */
    private String spec;

    /**
     * 生产日期
     */
    private Date produceDate;

    /**
     * 过期日期
     */
    private Date expiryDate;

    /**
     * 产品批号
     */
    private String batchNumber;
}
