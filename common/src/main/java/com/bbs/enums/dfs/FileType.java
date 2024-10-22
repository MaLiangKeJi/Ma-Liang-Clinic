package com.bbs.enums.dfs;

import cn.hutool.core.util.EnumUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.MimeTypeUtils;

import java.util.Map;
import java.util.stream.Collectors;

import static cn.hutool.http.ContentType.*;

@Getter
@AllArgsConstructor
public enum FileType {

    FILE(10, "未知", OCTET_STREAM.getValue()),

    IMAGE(11, "图片", MimeTypeUtils.IMAGE_JPEG_VALUE),

    VIDEO(12, "视频", "video/mp4");

    private final Integer code;

    private final String msg;

    private final String contentType;

    public static final Map<Integer, FileType> map = EnumUtil.getEnumMap(FileType.class).values().stream()
            .collect(Collectors.toMap(FileType::getCode, item -> item));
}
