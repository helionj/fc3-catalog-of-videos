package com.helion.catalog.infrastructure.category;

import com.helion.catalog.application.category.get.GetAllCategoriesByIdUseCase;
import com.helion.catalog.application.category.list.ListCategoryOutput;
import com.helion.catalog.domain.category.Category;
import com.helion.catalog.infrastructure.category.models.GqlCategory;

public final class GqlCategoryPresenter {

    private GqlCategoryPresenter(){}

    public static GqlCategory present(final ListCategoryOutput out){
        return new GqlCategory(out.id(), out.name(), out.description());
    }

    public static GqlCategory present(final GetAllCategoriesByIdUseCase.Output out){
        return new GqlCategory(out.id(), out.name(), out.description());
    }

    public static GqlCategory present(Category aCategory){
        return new GqlCategory(aCategory.id(), aCategory.name(), aCategory.description());
    }
}
