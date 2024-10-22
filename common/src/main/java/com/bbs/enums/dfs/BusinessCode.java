package com.bbs.enums.dfs;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BusinessCode {

    CONTENT_COVER(1002, "内容：封面图片"),
    CONTENT_IMAGE(1003, "内容图片"),
    CONTENT_VIDEO(1004, "内容视频"),
    CONTENT_VIDEO_COVER_TMP(1005, "内容视频：临时封面"),

    FINANCIAL_CERTIFICATE(2001, "财务凭证-附件");

    /**
     * 编码
     */
    private final Integer code;
    /**
     * 注释
     */
    private final String annotation;
}
