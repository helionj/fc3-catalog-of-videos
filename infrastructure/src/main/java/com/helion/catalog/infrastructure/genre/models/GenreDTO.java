package com.helion.catalog.infrastructure.genre.models;

import com.helion.catalog.domain.genre.Genre;

import java.time.Instant;
import java.util.Set;

public record GenreDTO(
        String id,
        String name,
        Set<String> categoriesId,
        Boolean isActive,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt

    ){

    public static Object from(Genre genre) {
        return new GenreDTO(
                genre.id(),
                genre.name(),
                genre.categories(),
                genre.active(),
                genre.createdAt(),
                genre.updatedAt(),
                genre.deletedAt()
        );
    }
}


