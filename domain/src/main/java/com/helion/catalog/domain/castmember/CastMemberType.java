package com.helion.catalog.domain.castmember;

import java.util.Arrays;

public enum CastMemberType {
    ACTOR,
    DIRECTOR,
    UNKNOWN;

    public static CastMemberType of(String type) {

        return Arrays.stream(values())
                .filter(it -> it.name().equalsIgnoreCase(type))
                .findFirst()
                .orElse(CastMemberType.UNKNOWN);
    }
}
