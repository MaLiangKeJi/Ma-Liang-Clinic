package com.bbs.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ONE;

/**
 * 用户表
 * @TableName user
 */
@TableName(value ="user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    /**
     * 用户ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 性别
     */
    @TableField(value = "sex")
    private Integer sex;

    /**
     * 个性签名
     */
    @TableField(value = "sign")
    private String sign;

    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 身份证号
     */
    @TableField(value = "id_card")
    private String idCard;

    /**
     * 手机号
     */
    @TableField(value = "phone")
    private Long phone;

    /**
     * 盐
     */
    @TableField(value = "salt")
    private Integer salt;

    /**
     * 工号
     */
    @TableField(exist = false)
    private String jobCard;


    /**
     * 账号状态： 1：冻结0：正常 -1：封禁 2：离职
     */
    @TableField(value = "state")
    private Integer state;

    @TableField(value = "open_id")
    private String openId;

    /**
     * 账号状态
     */
    @TableField(exist = false)
    private String stateStr;

    /**
     * 过期时间
     */
    @TableField(value = "expiration_time")
    private Date expirationTime;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    @TableField(value = "create_by")
    private Long createBy;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    @TableField(value = "update_by")
    private Long updateBy;

    /**
     * 图片 URL 地址
     */
    @TableField(value = "avatar")
    private String avatar;

    /**
     * 平台角色
     */
    @TableField(value = "paas_role")
    private Integer paasRole;

    /**
     * 个人首页背景图片
     */
    @TableField(value = "background_image")
    private String backgroundImage;

    @TableField(value = "is_admin")
    private Integer isAdmin;

    @TableField(value = "role")
    private Integer role;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private List<UserGroup> userGroupList;

    /**
     * 关注信息（当前实体用户，关注了当前登录用户）
     */
    @TableField(exist = false)
    private Fan fan;

    public User(Long id, String email, String name, String password, Long phone, Integer salt, Integer state, String stateStr, Date expirationTime, Date createTime, Date updateTime, List<UserGroup> userGroupList) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.salt = salt;
        this.state = state;
        this.stateStr = stateStr;
        this.expirationTime = expirationTime;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.userGroupList = userGroupList;
    }

    public Boolean isSupperAdmin() {
        return Objects.equals(isAdmin, INTEGER_ONE);
    }


    public User(String name, Long phone, String openId) {
        this.name = name;
        this.phone = phone;
        this.openId = openId;
    }

    public User(String name, Long phone) {
        this.name = name;
        this.phone = phone;
    }
}