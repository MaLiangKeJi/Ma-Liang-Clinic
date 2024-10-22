package com.bbs.auth.app.company.structure;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.bbs.Result;
import com.bbs.auth.entity.CompanyStructure;
import com.bbs.auth.service.CompanyService;
import com.bbs.auth.service.UserService;
import com.bbs.vo.UserVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.cache.annotation.CacheRemove;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.util.List;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;
import static org.apache.commons.lang3.math.NumberUtils.LONG_ZERO;

@RestController
@RequestMapping
public class Selected {

    private static final String LABEL = "label";

    private static final String CHILDREN = "children";

    @Resource
    private CompanyService companyService;

    @Resource
    private UserService userService;
    @Resource
    private TransactionDefinition transactionDefinition;
    @Resource
    private DataSourceTransactionManager transactionManager;

    @PostMapping("/company/structure")
    @CacheRemove(cacheName = "companyStructure")
    public Result<Boolean> create(@Valid @RequestBody Param param) {
        UserVO loginUser = userService.loginUser();
        Long loginUID = loginUser.getId();
        Long companyID = loginUser.getCompanyId();
        TransactionStatus transaction = transactionManager.getTransaction(transactionDefinition);
        try {
            if(param.useDefaultStructure) {
                List<CompanyStructure> companyStructures = companyService.searchStructure();
                for (int index = 0; index < companyStructures.size(); index++) {
                    CompanyStructure structure = companyStructures.get(index);

                    structure.setCompanyId(companyID);
                    structure.setId(null);
                    structure.setCreateTime(null);
                    structure.setCreateBy(loginUID);
                    structure.setUpdateTime(null);
                    structure.setUpdateBy(null);

                    if(index > 0) structure.setPid(companyStructures.get(index - 1).getId());

                    companyService.createCompanyStructure(structure);
                }
            } else {
                each(
                        companyID,
                        JSONUtil.parseArray(param.getCreateStructureJSON()),
                        LONG_ZERO,
                        loginUID
                );
            }
            transactionManager.commit(transaction);
            return Result.success();
        } catch (TransactionException e) {
            transactionManager.rollback(transaction);
            throw new RuntimeException(e);
        }
    }

    public void each(Long companyID, JSONArray jsonArray, Long pid, Long currentUID) {
        for (int index = INTEGER_ZERO; index < jsonArray.size(); index++) {
            JSONObject item = JSONUtil.parseObj(jsonArray.get(index));


            CompanyStructure structure = new CompanyStructure(companyID, item.get(LABEL).toString(), pid, currentUID, index);
            companyService.createCompanyStructure(structure);


            Object childrenObj = item.get(CHILDREN);
            if(nonNull(childrenObj)) {
                JSONArray children = JSONUtil.parseArray(childrenObj);
                if(children.size() > INTEGER_ZERO) {
                    each(companyID, children, structure.getPid(), currentUID);
                }
            }
        }
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Param {
        @NotNull
        private Boolean useDefaultStructure;

        private Long defaultStructureID;

        private String createStructureJSON;
    }
}
