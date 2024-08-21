package com.helion.catalog.application.category.list;

import com.helion.catalog.application.UseCase;
import com.helion.catalog.domain.category.CategoryGateway;
import com.helion.catalog.domain.category.CategorySearchQuery;
import com.helion.catalog.domain.pagination.Pagination;

public class ListCategoryUseCase extends UseCase<CategorySearchQuery, Pagination<ListCategoryOutput>> {

    private final CategoryGateway categoryGateway;

    public ListCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Override
    public Pagination<ListCategoryOutput> execute(CategorySearchQuery aQuery) {
        return this.categoryGateway.findAll(aQuery).map(ListCategoryOutput::from);
    }
}
