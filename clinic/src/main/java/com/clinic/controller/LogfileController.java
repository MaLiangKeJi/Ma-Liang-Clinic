package com.clinic.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping("/logfile")
public class LogfileController {

    @Value("${management.endpoints.logfile.info-file}")
    private Resource infoLogFile;

    @Value("${management.endpoints.logfile.error-file}")
    private Resource errorLogFile;

    @GetMapping("/info")
    public String getInfoLogfile() throws IOException {
        return new String(Files.readAllBytes(Paths.get(infoLogFile.getURI())));
    }

    @GetMapping("/error")
    public String getErrorLogfile() throws IOException {
        return new String(Files.readAllBytes(Paths.get(errorLogFile.getURI())));
    }
}
