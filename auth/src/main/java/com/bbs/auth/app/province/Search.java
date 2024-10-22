package com.bbs.auth.app.province;

import com.bbs.Result;
import com.bbs.auth.entity.Province;
import com.bbs.auth.service.ProvinceService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@RestController("SearchProvince")
@RequestMapping
@Slf4j
public class Search {

    @Resource
    private ProvinceService provinceService;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VO {

        private BigInteger value;

        private String label;

        private Boolean leaf = false;

        public VO(BigInteger value, String label) {
            this.value = value;
            this.label = label;
        }
    }


    @RequestMapping("/province")
    public Result<Set<VO>> search(
            @RequestParam(required = false) BigInteger id
    ) {
        return Result.success(provinceService.search(id));
    }
}
