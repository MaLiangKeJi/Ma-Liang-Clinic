package com.clinic.dto.param;

import com.clinic.entity.Stock;
import com.clinic.entity.StockBatch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PutStockList {

    /**
     * 批准文号列表
     */
    private List<String> approvalNumbers;

    /**
     * 批次号列表
     */
    private List<String> batchNumbers;

    /**
     * 药品信息
     */
    private List<StockBatch> batchList;

    /**
     * 药品列表
     */
    private List<Stock> drugs;

    /**
     * 总价
     */
    private BigDecimal totalCost;

    /**
     * 备注
     */
    private String remark;

    @Data
    public static class Count {

        /**
         * 统计类型
         */
        private Integer countType;

        /**
         * 统计数量
         */
        private Integer countNumber;

        /**
         * 统计单位
         */
        private Long unitId;
    }

    @Data
    public static class Unit {

        /**
         * 单位编号
         */
        private Long id;

        /**
         * 数量（最大单位的数量，是该药品的进货数量。其余都是上个单位的子级数量（如一箱共 12 瓶，中的 12））
         */
        private Long number;

    }
}
