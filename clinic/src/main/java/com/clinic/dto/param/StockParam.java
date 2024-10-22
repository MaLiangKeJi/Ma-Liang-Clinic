package com.clinic.dto.param;

import com.bbs.vo.BaseParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockParam extends BaseParam {

    private Long id;

    /**
     * 药品名称
     */
    private String name;

    /**
     * 药品编号
     */
    private Long drugId;

    /**
     * 库存状态
     */
    private Integer state;

    /**
     * 标签
     */
    private Long tagId;

    /**
     * 入库时间（起始）
     */
    private Date StorageStartTime;

    /**
     * 入库时间（结束）
     */
    private Date StorageEndTime;

    /**
     * 生产单位
     */
    private Long manufacturerId;

    /**
     * 生产单位名称
     */
    private String manufacturerName;
}
