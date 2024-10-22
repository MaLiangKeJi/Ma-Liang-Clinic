package com.bbs.auth.entity;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.bbs.auth.app.config.bloom.config.CheckPhoneIsRegisterConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ONE;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;

/**
 * 布隆过滤器初始化执行日志
 * @TableName bloom_filter_init_log
 */
@TableName(value ="bloom_filter_init_log")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class BloomFilterInitLog extends Model<BloomFilterInitLog> implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 业务编码
     */
    @TableField(value = "code")
    private String code;

    /**
     * 过滤器 Redis 名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 业务描述
     */
    @TableField(value = "bloom_filter_init_log.describe")
    private String describe;

    /**
     * 预计大小
     */
    @TableField(value = "bloom_filter_init_log.expected")
    private Long expected;

    /**
     * 错误率
     */
    @TableField(value = "bloom_filter_init_log.probability")
    private Double probability;

    /**
     * 执行耗时（毫秒数）
     */
    @TableField(value = "exec_time")
    private Long execTime;

    /**
     * 执行结果（1成功/0失败）
     */
    @TableField(value = "result")
    private Integer result;

    /**
     * 失败信息
     */
    @TableField(value = "error_msg")
    private String errorMsg;

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
     * 计时器
     */
    @TableField(exist = false)
    private TimeInterval timeInterval;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public void initConfig(CheckPhoneIsRegisterConfig config) {
        this.expected = config.getExpected();
        this.probability = config.getProbability();
    }

    public BloomFilterInitLog(String code, String name, String describe) {
        this.code = code;
        this.name = name;
        this.describe = describe;
    }

    public void of(Boolean execResult) {
        if(execResult) {
            success();
        } else {
            fail();
        }
    }

    public void success() {
        this.execTime = timeInterval.interval();
        this.result = INTEGER_ONE;
    }

    public void fail() {
        this.execTime = timeInterval.interval();
        this.result = INTEGER_ZERO;
    }

    public void fail(String msg) {
        this.execTime = timeInterval.interval();
        this.result = INTEGER_ZERO;
        this.errorMsg = msg;
    }

    public void startCounting() {
        timeInterval = DateUtil.timer();
    }

}