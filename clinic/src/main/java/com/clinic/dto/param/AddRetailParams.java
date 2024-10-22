package com.clinic.dto.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddRetailParams {

    /**
     * 患者姓名
     */
    private String name;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 出生年月
     */
    private Date birthday;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 手机号
     */
    private Long phone;

    /**
     * 身份证
     */
    private String idCard;

    /**
     * 联系人
     */
    private String contact;

    /**
     * 联系人电话
     */
    private Long contactPhone;

    /**
     * 地址
     */
    private String address;

    /**
     * 药品列表
     */
    private List<Drug> drugList;

    /**
     * 总价
     */
    private BigDecimal totalPrice;

    public List<Long> getStockIDs() throws IllegalArgumentException {
        return drugList.stream().map(drug -> {
            if(isNull(drug) || isNull(drug.getStockBatchId())) throw new IllegalArgumentException();
            return drug.getStockBatchId();
        }).collect(Collectors.toList());
    }


    /**
     * 零售药品信息
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Drug {

        /**
         * 库存批次ID
         */
        @NotNull
        private Long stockBatchId;

        /**
         * 数量
         */
        private Integer number;

        /**
         * 单位
         */
        private String unit;

        /**
         * 单价
         */
        private BigDecimal price;

        /**
         * 备注
         */
        private String remark;

        /**
         * 是否是库存
         */
        private Boolean isStock;
    }
}
