package com.clinic.controller;

import com.clinic.entity.UpdateLog;
import com.clinic.service.UpdateLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
public class UpdateLogController {

    @Resource
    private UpdateLogService updateLogService;

    @PutMapping("/update/log")
    public void insertUpdateLog(UpdateLog updateLog) {
        updateLogService.save(updateLog);
    }


    @GetMapping("/update/log")
    public List<UpdateLog> getUpdateLog() {
        return updateLogService.list();
    }

}
