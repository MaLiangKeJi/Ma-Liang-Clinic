package com.clinic.service.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bbs.util.FirstWordsSqlUtils;
import com.clinic.dto.param.UnitParam;
import com.clinic.entity.Unit;
import com.clinic.enums.DefaultStateEnum;
import com.clinic.mapper.UnitMapper;
import com.clinic.service.UnitService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static cn.hutool.core.lang.Validator.isWord;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;

/**
* @author 路晨霖
* @description 针对表【unit(单位（级联, tree 结构）)】的数据库操作Service实现
* @createDate 2023-09-17 04:18:46
*/
@Service
public class UnitServiceImpl extends ServiceImpl<UnitMapper, Unit>
    implements UnitService{

    @Override
    public List<Unit> search(UnitParam param) {
        LambdaQueryChainWrapper<Unit> wrapper = lambdaQuery();
        if(nonNull(param.getId())) {
            wrapper = wrapper.eq(Unit::getId, param.getId());
        } else {
            wrapper = wrapper
                    .like(isNotEmpty(param.getName()), Unit::getName, param.getName())
                    .eq(nonNull(param.getParentId()), Unit::getParentId, param.getParentId())
                    .eq(nonNull(param.getDefaultState()), Unit::getDefaultState, param.getDefaultState());
        }
        return wrapper.list();
    }

    @Override
    public List<Unit> searchAllDefUnit() {
        return lambdaQuery()
                .eq(Unit::getDefaultState, DefaultStateEnum.DEFAULT.getCode())
                .list();
    }

    @Override
    public List<Unit> searchNoRepeatAllDefUnit() {
        return lambdaQuery()
                .eq(Unit::getDefaultState, DefaultStateEnum.DEFAULT.getCode())
                .eq(Unit::getParentId, INTEGER_ZERO)
                .list();
    }

    @Override
    public List<Tree<Integer>> getTraceDefUnit() {
        return TreeUtil.build(
                searchAllDefUnit().stream()
                        .map(unit -> new TreeNode<>(unit.getId(), unit.getParentId(), unit.getName(), null))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public List<Unit> search(String name) {
        LambdaQueryChainWrapper<Unit> wrapper = lambdaQuery()
                .eq(Unit::getDefaultState, DefaultStateEnum.DEFAULT.getCode());

        if(StringUtils.isNotBlank(name)){
            if(isWord(name)){
                String sql = FirstWordsSqlUtils.getSql(name);
                wrapper.apply(sql);
            }else{
                wrapper.like(Unit::getName, name);
            }
        }
        return wrapper.eq(Unit::getParentId, INTEGER_ZERO)
                .list();
    }

}




