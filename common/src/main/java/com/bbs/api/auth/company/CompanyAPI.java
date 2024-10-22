package com.bbs.api.auth.company;

import com.bbs.vo.Company;
import com.bbs.vo.CompanyStructure;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CompanyAPI {

    List<Company> list(Collection<Long> ids);

    List<CompanyStructure> searchStructureNames(Long companyId, Collection<String> name);

    CompanyStructure searchStructureNames(Long companyId, String name);

    List<CompanyStructure> search(Set<Long> structureIds);

    Map<Long, CompanyStructure> searchIdMap(Set<Long> structureIds);
}
