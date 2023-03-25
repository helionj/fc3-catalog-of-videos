package com.helion.admin.catalog.domain.genre;

import com.helion.admin.catalog.domain.category.pagination.Pagination;
import com.helion.admin.catalog.domain.category.pagination.SearchQuery;

import java.util.Optional;

public interface GenreGateway {

    Genre create(Genre aGenre);

    void deleteById(GenreID anId);

    Optional<Genre> findById(GenreID anId);

    Genre update(Genre aGenre);

    Pagination<Genre> findAll(SearchQuery aQuery);
}
