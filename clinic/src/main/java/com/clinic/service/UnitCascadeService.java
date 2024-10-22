package com.clinic.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.service.IService;
import com.clinic.entity.UnitCascade;

import java.util.List;

public interface UnitCascadeService extends IService<UnitCascade> {

    List<Tree<Integer>> getUnitTrace();
}
