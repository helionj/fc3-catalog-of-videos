package com.helion.admin.catalog.application.category.retrieve.list;

import com.helion.admin.catalog.application.UseCase;
import com.helion.admin.catalog.domain.category.CategorySearchQuery;
import com.helion.admin.catalog.domain.category.pagination.Pagination;

public abstract class ListCategoriesUseCase extends UseCase<CategorySearchQuery, Pagination<CategoryListOutput>> {}
