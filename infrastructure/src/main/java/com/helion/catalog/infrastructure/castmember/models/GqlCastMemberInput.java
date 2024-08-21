package com.helion.catalog.infrastructure.castmember.models;

import com.helion.catalog.domain.castmember.CastMember;
import com.helion.catalog.domain.castmember.CastMemberType;

import java.time.Instant;

public record GqlCastMemberInput(
        String id,
        String name,
        String type,
        Instant createdAt,
        Instant updatedAt

) {

    public CastMember toCastMember() {
        return CastMember.with(id, name, CastMemberType.of(type), createdAt, updatedAt);
    }
}
