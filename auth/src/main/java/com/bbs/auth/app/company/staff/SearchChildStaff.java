package com.bbs.auth.app.company.staff;

import com.bbs.Result;
import com.bbs.api.auth.company.staff.ChildStaff;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@RestController
@RequestMapping
public class SearchChildStaff {

    @Resource(name = "searchChildStaffService")
    private com.bbs.auth.service.company.staff.SearchChildStaff searchChildStaff;

    @GetMapping("/company/staff/child")
    public Result<Set<ChildStaff>> search(Long uid, Long companyID) {
        return Result.success(searchChildStaff.search(uid, companyID));
    }
}
