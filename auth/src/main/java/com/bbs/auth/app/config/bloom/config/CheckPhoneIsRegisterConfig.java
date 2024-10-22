package com.bbs.auth.app.config.bloom.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import static java.util.Objects.nonNull;

@Data
@Component
@RefreshScope
public class CheckPhoneIsRegisterConfig {

    @Value("${filter.bloom.phone.name}")
    private String name;

    @Value("${filter.bloom.phone.expected}")
    private String expected;

    @Value("${filter.bloom.phone.probability}")
    private String probability;

    @Value("${filter.bloom.phone.threshold}")
    private String threshold;

    public Long getExpected() {
        return nonNull(expected) ? Long.parseLong(expected) : null;
    }

    public Double getProbability() {
        return nonNull(probability) ? Double.parseDouble(probability) : null;
    }

    public Double getThreshold() {
        return nonNull(threshold) ? Double.parseDouble(threshold) : null;
    }
}
