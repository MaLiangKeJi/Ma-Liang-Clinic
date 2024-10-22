package com.bbs.auth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static org.apache.commons.lang3.math.NumberUtils.*;

@Getter
@AllArgsConstructor
public enum InviteClaimStatus {

    UNFINISHED(INTEGER_ZERO, "未完成"),
    AWARD_RECEIVED(INTEGER_ONE, "已领取奖励"),
    UNCLAIMED_AWARD(-INTEGER_ONE, "未领取奖励"),
    ;

    private final Integer code;

    private final String description;
}
