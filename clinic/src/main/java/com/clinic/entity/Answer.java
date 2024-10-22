package com.clinic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName answer
 */
@TableName(value ="answer")
@Data
public class Answer implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    @TableField(value = "created_at")
    private Date createdAt;

    /**
     * 
     */
    @TableField(value = "updated_at")
    private Date updatedAt;

    /**
     * 
     */
    @TableField(value = "question_id")
    private Long questionId;

    /**
     * 
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 
     */
    @TableField(value = "last_edit_user_id")
    private Long lastEditUserId;

    /**
     * 
     */
    @TableField(value = "original_text")
    private String originalText;

    /**
     * 
     */
    @TableField(value = "parsed_text")
    private String parsedText;

    /**
     * 
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 
     */
    @TableField(value = "adopted")
    private Integer adopted;

    /**
     * 
     */
    @TableField(value = "comment_count")
    private Integer commentCount;

    /**
     * 
     */
    @TableField(value = "vote_count")
    private Integer voteCount;

    /**
     * 
     */
    @TableField(value = "revision_id")
    private Long revisionId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}