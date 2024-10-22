package com.bbs.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bbs.vo.UserVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户 & 公司关系表（职位）
 * @TableName user_company
 */
@TableName(value ="user_company")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserCompany implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户主键
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 公司主键
     */
    @TableField(value = "company_id")
    private Long companyId;

    /**
     * 工号
     */
    @TableField(value = "job_card")
    private String jobCard;

    /**
     * 部门ID（公司结构ID）
     */
    @TableField(value = "structure_id")
    private Long structureId;

    /**
     * 职位名称
     */
    @TableField(value = "position_name")
    private String positionName;

    /**
     * 上级领导
     */
    @TableField(value = "lead_id")
    private Long leadId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 信息创建人
     */
    @TableField(value = "create_by")
    private Long createBy;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 信息修改人
     */
    @TableField(value = "update_by")
    private Long updateBy;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private Company company;

    @TableField(exist = false)
    private UserVO user;

    @TableField(exist = false)
    private CompanyStructure structure;

    public UserCompany(Long userID, Long companyID, Long structureID, Long loginUID) {
        this.userId = userID;
        this.companyId = companyID;
        this.structureId = structureID;
        this.createBy = loginUID;
    }

    public UserCompany(Long userId, Long companyId, String jobCard, Long structureId) {
        this.userId = userId;
        this.companyId = companyId;
        this.jobCard = jobCard;
        this.structureId = structureId;
        this.createBy = userId;
    }
}