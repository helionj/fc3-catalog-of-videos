package com.helion.admin.catalog.application.category.retrieve.list;

import com.helion.admin.catalog.domain.category.CategoryGateway;
import com.helion.admin.catalog.domain.category.pagination.SearchQuery;
import com.helion.admin.catalog.domain.category.pagination.Pagination;

import java.util.Objects;

public class DefaultListCategoriesUseCase extends ListCategoriesUseCase{

    private final CategoryGateway categoryGateway;

    public DefaultListCategoriesUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Pagination<CategoryListOutput> execute(final SearchQuery aQuery) {
        return this.categoryGateway.findAll(aQuery)
                .map(CategoryListOutput::from);
    }
}
