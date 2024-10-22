package com.bbs.auth.app.dubbo;

import com.bbs.api.auth.company.CompanyAPI;
import com.bbs.auth.converter.CompanyConverter;
import com.bbs.auth.service.CompanyService;
import com.bbs.vo.Company;
import com.bbs.vo.CompanyStructure;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@DubboService
public class CompanyAPIImpl implements CompanyAPI {

    @Resource
    private CompanyService companyService;

    @Resource
    private CompanyConverter converter;

    @Override
    public List<Company> list(Collection<Long> ids) {
        return converter.toVO(companyService.listByIds(ids));
    }

    @Override
    public List<CompanyStructure> searchStructureNames(Long companyId, Collection<String> name) {
        if (name != null && !name.isEmpty()) {
            return converter.toCSVO(companyService.searchStructure(companyId, new ArrayList<>(name)));
        }
        return Collections.emptyList();
    }

    @Override
    public CompanyStructure searchStructureNames(Long companyId, String name) {
        return converter.toCSVO(companyService.searchStructure(companyId, name));
    }

    @Override
    public List<CompanyStructure> search(Set<Long> structureIds) {
        return converter.toCSVO(companyService.searchStructure(structureIds));
    }

    @Override
    public Map<Long, CompanyStructure> searchIdMap(Set<Long> structureIds) {
        return search(structureIds).stream().collect(Collectors.toMap(CompanyStructure::getId, structure -> structure));
    }

}
