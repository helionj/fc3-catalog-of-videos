package com.helion.admin.catalog.infrastructure.category.presenters;

import com.helion.admin.catalog.application.category.retrieve.get.CategoryOutput;
import com.helion.admin.catalog.application.category.retrieve.list.CategoryListOutput;
import com.helion.admin.catalog.infrastructure.category.models.CategoryResponse;
import com.helion.admin.catalog.infrastructure.category.models.CategoryListResponse;

public class CategoryApiPresenter {

    public static CategoryResponse present(CategoryOutput output){
        return new CategoryResponse(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }

    public static CategoryListResponse present(CategoryListOutput output){
        return new CategoryListResponse(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.deletedAt()
        );
    }
}
