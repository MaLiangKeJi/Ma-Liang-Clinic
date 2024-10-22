package com.bbs.enums.dfs;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DSF 服务存储类型
 */
@Getter
@AllArgsConstructor
public enum ResourceType {

    DIR(10, "目录"),  //目录

    FILE(11, "文件");    //文件

    private final Integer code;

    private final String msg;
}
