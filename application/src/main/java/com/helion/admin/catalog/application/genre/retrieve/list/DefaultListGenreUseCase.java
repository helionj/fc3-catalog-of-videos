package com.helion.admin.catalog.application.genre.retrieve.list;

import com.helion.admin.catalog.domain.category.pagination.Pagination;
import com.helion.admin.catalog.domain.category.pagination.SearchQuery;
import com.helion.admin.catalog.domain.genre.GenreGateway;

import java.util.Objects;

public class DefaultListGenreUseCase extends ListGenreUseCase{

    private final GenreGateway genreGateway;

    public DefaultListGenreUseCase(GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public Pagination<GenreListOutput> execute(SearchQuery aQuery) {
        return this.genreGateway.findAll(aQuery).map(GenreListOutput::from);
    }
}
