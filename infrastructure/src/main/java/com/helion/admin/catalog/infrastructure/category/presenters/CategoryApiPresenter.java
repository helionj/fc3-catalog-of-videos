package com.helion.admin.catalog.infrastructure.category.presenters;

import com.helion.admin.catalog.application.category.retrieve.get.CategoryOutput;
import com.helion.admin.catalog.infrastructure.category.models.CategoryApiOutput;

public class CategoryApiPresenter {

    public static CategoryApiOutput present(CategoryOutput output){
        return new CategoryApiOutput(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }
}
