package com.helion.admin.catalog.application.genre.retrieve.get;

import com.helion.admin.catalog.domain.category.CategoryID;
import com.helion.admin.catalog.domain.genre.Genre;
import com.helion.admin.catalog.domain.genre.GenreID;

import java.time.Instant;
import java.util.List;

public record GenreOutput(
        String id,
        String name,
        boolean isActive,
        List<String> categories,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {
    public static GenreOutput from(final Genre aGenre){
        return new GenreOutput(
                aGenre.getId().getValue(),
                aGenre.getName(),
                aGenre.isActive(),
                asString(aGenre.getCategories()),
                aGenre.getCreatedAt(),
                aGenre.getUpdatedAt(),
                aGenre.getDeletedAt()
        );
    }

    private static List<String> asString(List<CategoryID> categories) {
        return categories.stream()
                .map(CategoryID::getValue)
                .toList();
    }
}
