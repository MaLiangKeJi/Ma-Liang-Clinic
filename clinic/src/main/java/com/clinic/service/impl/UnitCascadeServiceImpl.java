package com.clinic.service.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clinic.entity.UnitCascade;
import com.clinic.mapper.UnitCascadeMapper;
import com.clinic.service.UnitCascadeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UnitCascadeServiceImpl
        extends ServiceImpl<UnitCascadeMapper, UnitCascade>
        implements UnitCascadeService
{

    @Override
    public List<Tree<Integer>> getUnitTrace() {
        return TreeUtil.build(list().stream().map(unit -> new TreeNode<>(unit.getId(), unit.getParentId(), unit.getName(), null)).collect(Collectors.toList()));
    }
}
