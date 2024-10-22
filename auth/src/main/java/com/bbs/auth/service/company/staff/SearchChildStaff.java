package com.bbs.auth.service.company.staff;

import com.bbs.api.auth.company.staff.ChildStaff;
import com.bbs.auth.entity.CompanyStructure;
import com.bbs.auth.entity.User;
import com.bbs.auth.service.CompanyService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ONE;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;

@DubboService
@Service("searchChildStaffService")
public class SearchChildStaff implements com.bbs.api.auth.company.staff.SearchChildStaff {

    @Resource
    private CompanyService companyService;

    @Override
    public Set<ChildStaff> search(Long uid, Long companyID) {

        CompanyStructure userCompanyStructure = companyService.searchUserCompanyStructure(uid, companyID);

        // 当前用户归属结构的【层级】
        Integer hierarchical = userCompanyStructure.getHierarchical();  // 2 4(5)

        // 公司全部结构分组（按层级分）
        Map<Integer, List<CompanyStructure>> allStructureHierarchicalGroup = companyAllStructureHierarchicalGroup(companyID);

        // 父级结构 ID 列表
        Set<Long> parentStructureIds = new HashSet<>();
        parentStructureIds.add(userCompanyStructure.getId());

        Set<Long> structureIds = new HashSet<>();

        // 获取子级结构的 ID列表
        loop(parentStructureIds, hierarchical, allStructureHierarchicalGroup, structureIds);

        List<User> users = companyService.searchStructureStaff(structureIds);
        return users.stream().map(user -> new ChildStaff(user.getId(), user.getName())).collect(Collectors.toSet());
    }

    private void loop(Set<Long> parentStructureIds, Integer hierarchical, Map<Integer, List<CompanyStructure>> allStructureHierarchicalGroup, Set<Long> result) {
        int nextHierarchical = hierarchical + INTEGER_ONE;  //递增层级

        // 获取子级结构的 ID列表
        Set<Long> childStructureIds = searchChildStructureIds(parentStructureIds, nextHierarchical, allStructureHierarchicalGroup);

        if(nonNull(childStructureIds) && childStructureIds.size() > INTEGER_ZERO) {
            // 查询用户并填充
            searchAndFillUser(childStructureIds, result);
            // 开始下一次循环
            loop(childStructureIds, nextHierarchical, allStructureHierarchicalGroup, result);
        }
    }

    /**
     * 查询用户并填充到容器
     */
    private void searchAndFillUser(Set<Long> childStructureIds, Set<Long> result) {
        if(nonNull(childStructureIds) && childStructureIds.size() > INTEGER_ZERO) result.addAll(childStructureIds);
    }

    /**
     * 查询【公司全部结构分组（按层级分）】
     * @param companyID 公司ID
     * @return 公司全部结构分组（按层级分）
     */
    private Map<Integer, List<CompanyStructure>> companyAllStructureHierarchicalGroup(Long companyID) {
        return companyService.searchStructure(companyID).stream()
                .collect(Collectors.groupingBy(CompanyStructure::getHierarchical));
    }

    /**
     * 获取子级结构的 ID列表
     * @param parentStructureIds 当前层级 - 全部结构 ID 列表
     * @param nextHierarchical 下个层级
     * @param allStructureHierarchicalGroup 公司全部结构分组（按层级分）
     * @return 子级结构的 ID 列表
     */
    private Set<Long> searchChildStructureIds(Set<Long> parentStructureIds, Integer nextHierarchical, Map<Integer, List<CompanyStructure>> allStructureHierarchicalGroup) {
        List<CompanyStructure> hierarchicalStructures = allStructureHierarchicalGroup.get(nextHierarchical);
        return nonNull(hierarchicalStructures) ? filterChildStructure(parentStructureIds, hierarchicalStructures) : null;
    }

    /**
     * 从【下一层全部结构】中过滤出【直接子级结构】
     * @param parentStructureIds 多个父级结构 ID
     * @param nextHierarchicalStructures 下一层全部结构
     * @return 直接子级结构
     */
    private Set<Long> filterChildStructure(Set<Long> parentStructureIds, List<CompanyStructure> nextHierarchicalStructures) {
        Set<Long> childStructureIds = new HashSet<>();
        if(nonNull(nextHierarchicalStructures) && nextHierarchicalStructures.size() > INTEGER_ZERO) {
            childStructureIds = nextHierarchicalStructures.stream()
                    .filter(childStructure -> parentStructureIds.stream()
                            .filter(parentStructureId -> parentStructureId.equals(childStructure.getPid()))
                            .count() > INTEGER_ZERO
                    )
                    .map(CompanyStructure::getId)
                    .collect(Collectors.toSet());
        }
        return childStructureIds;
    }
}
