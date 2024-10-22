package com.bbs.auth.util;

import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;

/**
 * 接口权限管理（超敏感操作前的校验）
 */
public class ApiPermissionManagement {


    @Value("${api.management.permission.code}")
    private String apiManagementPermissionCode;

    private static String staticApiManagementPermissionCode = null;

    @PostConstruct
    private void init() {
        staticApiManagementPermissionCode = apiManagementPermissionCode;
    }

    /**
     * 用于没有参数，但是请求类型是 Post/Del/Put 的接口，使用 @RequestBody 获取权限码
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Param {
        private String code;
    }

    /**
     * 对暗号，访问这个接口，必须要传一个 code 码，与 nacos 中的一致
     */
    public static void checkApiPermissionManagementCode(Param param) throws IllegalArgumentException {
        String code = param.getCode();
        checkApiPermissionManagementCode(code);
    }

    /**
     * 对暗号，访问这个接口，必须要传一个 code 码，与 nacos 中的一致
     */
    public static void checkApiPermissionManagementCode(String code) throws IllegalArgumentException {
        Preconditions.checkArgument(
                StringUtils.isNotBlank(code) && staticApiManagementPermissionCode.equals(code),
                "您没有权限访问该接口"
        );
    }
}
