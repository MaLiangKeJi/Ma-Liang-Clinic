package com.clinic.util;

import com.bbs.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class SpringUtil {
    private SpringUtil() {
        throw new RuntimeException("no create obj");
    }

    public static <T, R> R getRespData(Class<T> clazz, ApplicationContext context, Function<T, Result<R>> callBeanFunc) {
        T t = getBean(clazz, context);
        return callBeanFunc.apply(t).getData();
    }

    public static <T, R> Result<R> getResp(Class<T> clazz, ApplicationContext context, Function<T, Result<R>> callBeanFunc) {
        T t = getBean(clazz, context);
        return callBeanFunc.apply(t);
    }

    public static <R> R getBean(Class clazz, ApplicationContext context) {
        return (R) context.getBean(getClassNameOfFirstLow(clazz));
    }

    /**
     * 获得首字母小写的类名称
     */
    public static String getClassNameOfFirstLow(Class clazz) {
        String className = clazz.getSimpleName();
        String firstEN = className.substring(0, 1);
        String otherEN = className.substring(1);
        return firstEN.toLowerCase() + otherEN;
    }

    /**
     * 匹配qs.stringify(array, { indices: false })
     *
     * @param text           数组字符串
     * @param getDoneObjFunc 获取实际类型接口
     * @param <R>            返回值泛型
     */
    public static <R> List<R> str2ListByQs(String text, Function<String, R> getDoneObjFunc) {
        if (StringUtils.isBlank(text))
            return Collections.emptyList();

        String[] strArr = text.split("&");
        List<String> tmpList = Arrays.stream(strArr).map(s -> s.split("=")[1]).collect(Collectors.toList());
        List<R> result = new ArrayList<>();
        for (String str : tmpList)
            result.add(getDoneObjFunc.apply(str));
        return result;
    }
}