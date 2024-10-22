package com.bbs.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

public class IDUtil {

    private static final Snowflake LOG_SNOWFLAKE_ID = IdUtil.getSnowflake(1, 1);

    /**
     * 获取 ID
     * @return ID
     */
    public static Long getLogID() {
        return LOG_SNOWFLAKE_ID.nextId();
    }
}
