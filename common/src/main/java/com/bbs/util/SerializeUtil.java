package com.bbs.util;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

/**
 * 序列化工具
 * @author ext.luchenlin1
 */
public class SerializeUtil {
    /**
     * Protobuf 序列化
     * @param t 数据
     * @param clazz 序列化类型
     * @return 序列化字节数据
     * @param <T> 序列化类型
     * <a href="https://www.jianshu.com/p/657fbf347934">参考：常见的序列化框架及Protobuf序列化原理</a>
     */
    public static <T> byte[] serializeByProtobuf(T t, Class<T> clazz) {
        return ProtobufIOUtil.toByteArray(t, RuntimeSchema.createFrom(clazz),
                LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
    }

    /**
     * Protobuf 反序列化
     * @param data 序列化字节数据
     * @param clazz 序列化类型
     * @return 反序列化结果
     * @param <T> 序列化类型
     */
    public static <T> T deSerializeByProtobuf(byte[] data, Class<T> clazz) {
        RuntimeSchema<T> runtimeSchema = RuntimeSchema.createFrom(clazz);
        T t = runtimeSchema.newMessage();
        ProtobufIOUtil.mergeFrom(data, t, runtimeSchema);
        return t;
    }
}
