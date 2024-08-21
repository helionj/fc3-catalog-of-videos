package com.helion.catalog.domain.genre;

import java.util.Set;

public record GenreSearchQuery(
        int page,
        int perPage,
        String terms,
        String sort,
        String direction,
        Set<String> categories
) {
}
