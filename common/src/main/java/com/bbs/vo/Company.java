package com.bbs.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 注册资本
     */
    private String registeredCapital;

    /**
     * 统一社会信用代码
     */
    private String code;

    /**
     * 营业执照照片 url
     */
    private String businessLicense;

    /**
     * 所属地区 ID（三级联动）
     */
    private Long attributionID;

    /**
     * 注册地址
     */
    private String registeredAddress;

    /**
     * 行业（国标码）
     */
    private Long normIndustryId;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 信息创建人
     */
    private Long createBy;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 信息修改人
     */
    private Long updateBy;
}
