package com.bbs.api;

import cn.hutool.core.io.resource.InputStreamResource;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.http.*;
import cn.hutool.json.JSONUtil;
import com.bbs.Result;
import com.bbs.enums.dfs.BusinessCode;
import com.bbs.enums.dfs.FileType;
import com.bbs.enums.dfs.ResourceType;
import com.bbs.exception.BusinessException;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class DFS {

    public static final String clean_api = "/batch";

    public static final String clean_temporary_api = "/clean/temporary";

    @Slf4j
    @Component
    public static class Clean {

//        @Value("${dfs.host}")
        private String host;


        public void batchClean(String api, List<String> resourceIds) {
            Map<String, Object> param = new HashMap<>();
            param.put("resourceIds", resourceIds);
            String resultStr = HttpRequest.delete(host + api)
                    .form(param)
                    .execute().body();
            if(StringUtils.isNotBlank(resultStr)) {
                Result<Upload.VO> result = JSONUtil.toBean(resultStr, new TypeReference<Result<Upload.VO>>() {
                }, true);
                if(result.getCode() == HttpStatus.HTTP_OK) {
                    return;
                }
            }
            throw new BusinessException();
        }
    }

    @Slf4j
    @Component
    public static class Upload {

//        @Value("${dfs.host}")
        private String host;

        private static final String api = "/upload";

        private static final String temporary_api = "/upload/temporary";

        private static final String BUSINESS_CODE = "businessCode";

        private static final String RESOURCE_TYPE = "resourceType";

        private static final String FILE_TYPE = "fileType";

        private static final String CONTENT_TYPE = "contentType";

        private static final String FILE = "file";


        public VO uploadFile(BusinessCode businessCode, ResourceType resourceType, FileType fileType, MultipartFile file) {
            Map<String,Object> map = createParam(businessCode, resourceType, fileType, file);
            return uploadFile(host + api, map);
        }

        public VO uploadFile(BusinessCode businessCode, ResourceType resourceType, String contentType, MultipartFile file) {
            Map<String,Object> map = createParam(businessCode, resourceType, contentType, file);
            return uploadFile(host + api, map);
        }

        public VO uploadTemporaryFile(BusinessCode businessCode, ResourceType resourceType, FileType fileType, MultipartFile file) {
            Map<String,Object> map = createParam(businessCode, resourceType, fileType, file);
            return uploadFile(host + temporary_api, map);
        }

        public VO uploadTemporaryFile(BusinessCode businessCode, ResourceType resourceType, String contentType, MultipartFile file) {
            Map<String,Object> map = createParam(businessCode, resourceType, contentType, file);
            return uploadFile(host + temporary_api, map);
        }

        private Map<String,Object> createParam(BusinessCode businessCode, ResourceType resourceType, FileType fileType, MultipartFile file) {
            Map<String,Object> map = Maps.newHashMap();
            map.put(BUSINESS_CODE, businessCode.getCode());
            map.put(RESOURCE_TYPE, resourceType.getCode());
            map.put(FILE_TYPE, fileType.getCode());
            map.put(FILE, toInputStream(file));
            return map;
        }

        private Map<String,Object> createParam(BusinessCode businessCode, ResourceType resourceType, String contentType, MultipartFile file) {
            Map<String,Object> map = Maps.newHashMap();
            map.put(BUSINESS_CODE, businessCode.getCode());
            map.put(RESOURCE_TYPE, resourceType.getCode());
            map.put(CONTENT_TYPE, contentType);
            map.put(FILE, toInputStream(file));
            return map;
        }

        public VO uploadFile(String url, Map<String, Object> params) {
            String resultJSON = HttpRequest.post(url)
                    .contentType(ContentType.MULTIPART.getValue())
                    .form(params)
                    .execute().body();
            if(StringUtils.isNotBlank(resultJSON)) {
                Result<VO> result = JSONUtil.toBean(resultJSON, new TypeReference<Result<VO>>() {}, true);
                if(result.getCode() == HttpStatus.HTTP_OK) {
                    return result.getData();
                }
            }
            throw new BusinessException();
        }


        public InputStreamResource toInputStream(MultipartFile file) {
            InputStreamResource isr = null;
            try {
                isr = new InputStreamResource(file.getInputStream(), file.getOriginalFilename());
            } catch (IOException e) {
                log.info("文件流转换异常:{}", e.getMessage());
            }
            return isr;
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class VO {

            private String url;

            private String resourceID;
        }
    }
}
