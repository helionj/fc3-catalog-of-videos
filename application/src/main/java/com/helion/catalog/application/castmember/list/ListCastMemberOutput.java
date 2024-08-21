package com.helion.catalog.application.castmember.list;

import com.helion.catalog.domain.castmember.CastMember;
import com.helion.catalog.domain.castmember.CastMemberType;

import java.time.Instant;

public record ListCastMemberOutput(
        String id,
        String name,
        CastMemberType type,
        Instant createdAt,
        Instant updatedAt
) {
    public static ListCastMemberOutput from(final CastMember aMember){
        return new ListCastMemberOutput(
                aMember.id(),
                aMember.name(),
                aMember.type(),
                aMember.createdAt(),
                aMember.updatedAt());
    }
}
