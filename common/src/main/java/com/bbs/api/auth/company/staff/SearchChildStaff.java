package com.bbs.api.auth.company.staff;

import java.util.Set;

public interface SearchChildStaff {

    Set<ChildStaff> search(Long uid, Long companyID);
}
