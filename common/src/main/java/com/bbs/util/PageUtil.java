package com.bbs.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.ArrayList;
import java.util.List;

public class PageUtil {

    /**
     * 对列表进行分页
     *
     * @param list     原始列表
     * @param page     页码，从 1 开始
     * @param pageSize 每页的大小
     * @param <T>      列表元素的类型
     * @return 分页后的列表
     */
    public static <T> List<T> paginate(List<T> list, int page, int pageSize) {
        if (list == null || list.isEmpty() || page <= 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Invalid input parameters");
        }

        int fromIndex = (page - 1) * pageSize;
        if (fromIndex >= list.size()) {
            return new ArrayList<>(); // 返回空列表
        }

        int toIndex = Math.min(fromIndex + pageSize, list.size());
        return list.subList(fromIndex, toIndex);
    }

    /**
     * 对列表进行分页，并返回包含分页信息的结果对象
     *
     * @param list     原始列表
     * @param current     页码，从 1 开始
     * @param size 每页的大小
     * @param <T>      列表元素的类型
     * @return 包含分页信息的结果对象
     */
    public static <T> Page<T> paginateWithInfo(List<T> list, int current, int size) {
        if (list == null || list.isEmpty() || current <= 0 || size <= 0) {
            return new Page<>(current, size, 0);
        }

        int totalItems = list.size();
        int fromIndex = (current - 1) * size;
        if (fromIndex >= totalItems) {
            Page<T> page = new Page<>(current, size, totalItems);
            page.setRecords(new ArrayList<>());
            return page; // 返回空结果
        }

        int toIndex = Math.min(fromIndex + size, totalItems);
        List<T> pagedList = list.subList(fromIndex, toIndex);
        Page<T> page = new Page<>(current, size, totalItems);
        page.setRecords(pagedList);
        return page;
    }
}
