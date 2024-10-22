package com.clinic.entity;

import cn.hutool.json.JSONArray;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName question
 */
@Data
public class Question implements Serializable {

    /**
     * 
     */
    private Long id;

    /**
     *
     */
    private String title;

    /**
     * 
     */
    private Integer created_at;


    /**
     *
     */
    private Integer vote_count;

    /**
     * 
     */
    private Integer answer_count;

    /**
     *
     */
    private JSONArray user_info;

    /**
     *
     */
    private JSONArray tags;

    /**
     *
     */
    private String status;


    private static final long serialVersionUID = 1L;
}