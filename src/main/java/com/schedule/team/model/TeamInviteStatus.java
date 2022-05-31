package com.schedule.team.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum TeamInviteStatus {
    ACCEPTED("accepted"),
    REJECTED("rejected"),
    CLOSED("closed"),
    OPEN("open");

    private final String code;

    TeamInviteStatus(String code) {
        this.code = code;
    }

    @JsonCreator
    public static TeamInviteStatus decode(final String code) {
        return Stream.of(
                        TeamInviteStatus.values()
                )
                .filter(targetEnum -> targetEnum.code.equals(code.toLowerCase()))
                .findFirst()
                .orElse(null);
    }

    @JsonValue
    public String getCode() {
        return code;
    }
}