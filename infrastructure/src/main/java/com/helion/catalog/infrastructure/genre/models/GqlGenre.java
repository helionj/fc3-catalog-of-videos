package com.helion.catalog.infrastructure.genre.models;

import java.util.Set;

public record GqlGenre(
        String id,
        String name,
        Set<String> categories,
        Boolean active,
        String createdAt,
        String  updatedAt,
        String deletedAt
) {}
