package com.helion.catalog.infrastructure.castmember.models;

public record GqlCastMember(
        String id,
        String name,
        String type,
        String createdAt,
        String updatedAt
) {}
