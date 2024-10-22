package com.bbs.util;

import com.github.luben.zstd.Zstd;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;


/**
 * 压缩工具
 * @author ext.luchenlin1
 */
@Slf4j
public class CompressUtil {

    /**
     * 压缩阈值：大于 256 压缩生效
     */
    private static final Integer COMPRESS_THRESHOLD = 256;

    /**
     * ZSTD 压缩
     * @param data 数据
     * @return 压缩后的字节数组
     * @throws IOException 写入流失败
     */
    public static byte[] compressByZSTD(byte[] data) {
        if(data.length > COMPRESS_THRESHOLD) {
            return Zstd.compress(data);
        }
        return data;
    }
}
