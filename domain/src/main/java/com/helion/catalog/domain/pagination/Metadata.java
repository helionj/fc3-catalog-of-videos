package com.helion.catalog.domain.pagination;

public record Metadata(
        int currentPage,
        int perPage,
        long total
) {
}
