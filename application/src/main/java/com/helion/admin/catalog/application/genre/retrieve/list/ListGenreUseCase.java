package com.helion.admin.catalog.application.genre.retrieve.list;

import com.helion.admin.catalog.application.UseCase;
import com.helion.admin.catalog.domain.category.pagination.Pagination;
import com.helion.admin.catalog.domain.category.pagination.SearchQuery;

public abstract class ListGenreUseCase extends UseCase<SearchQuery, Pagination<GenreListOutput>> {
}
