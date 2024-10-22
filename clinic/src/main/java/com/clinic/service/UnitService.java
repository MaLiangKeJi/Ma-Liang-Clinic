package com.clinic.service;

import cn.hutool.core.lang.tree.Tree;
import com.clinic.entity.Unit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.clinic.dto.param.UnitParam;

import java.util.List;

/**
* @author 路晨霖
* @description 针对表【unit(单位（级联, tree 结构）)】的数据库操作Service
* @createDate 2023-09-17 04:18:46
*/
public interface UnitService extends IService<Unit> {

    List<Unit> search(UnitParam param);

    List<Unit> searchAllDefUnit();

    List<Unit>  searchNoRepeatAllDefUnit();

    List<Tree<Integer>> getTraceDefUnit();

    List<Unit> search(String name);
}
