package com.clinic.controller;

import com.bbs.Result;
import com.clinic.cache.inform.InformCache;
import com.clinic.entity.Inform;
import com.clinic.util.LoginUser;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
public class InformController {


    @Resource
    private InformCache InformCache;

    /**
     * 查询当前用户未读通知
     * @return Result<List<Inform>>
     */
    @GetMapping("/inform")
    public Result<Map<String,Object>> getLoginInform() {
        return Result.success(InformCache.getInformList(LoginUser.getId()));
    }

    /**
     * 修改登录用户全部通知为已读
     * @return Result<Boolean>
     */
    @PostMapping("/inform")
    public Result<Boolean> updateLoginInform() {
        InformCache.updateUserReadInform(LoginUser.getId());
        return Result.success();
    }


    /**
     * 查询所有通知
     * @return Result<List<Inform>>
     */
    @GetMapping("/back/inform")
    public Result<Object> getAllInform() {
        Map<String, Object> map = InformCache.getInformList(null);
        return Result.success(map.get("list"));
    }

    @Data
    public static class InformVo{
        private String title;
        private String content;
        private int type;
    }

    /**
     * 添加通知
     * @param informVo InformVo
     * @return Boolean
     */
    @PutMapping("/back/inform")
    public Result<Boolean> putInform(@RequestBody InformVo informVo) {
        InformCache.putInform(new Inform(informVo));
        return Result.success();
    }

}
