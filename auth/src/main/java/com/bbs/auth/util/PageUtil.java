package com.bbs.auth.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections4.ListUtils;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;

public class PageUtil {

    public static <T> Page<T> execPage(Integer current, Integer size, List<T> dataList) {
        int allSize = dataList.size();
        if(dataList.size() == INTEGER_ZERO) {
            Page<T> page = new Page<>(current, size, INTEGER_ZERO);
            page.setRecords(new ArrayList<>());
            return page;
        } else {
            Page<T> page = new Page<>(current, size, allSize);
            List<List<T>> group = ListUtils.partition(dataList, size);
            List<T> result = new ArrayList<>();
            if(group.size() <= current) {
                result = group.get(current - 1);
            }
            page.setRecords(result);
            return page;
        }
    }
}
