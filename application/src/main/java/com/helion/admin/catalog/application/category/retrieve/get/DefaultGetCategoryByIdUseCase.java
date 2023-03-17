package com.helion.admin.catalog.application.category.retrieve.get;

import com.helion.admin.catalog.domain.category.Category;
import com.helion.admin.catalog.domain.category.CategoryGateway;
import com.helion.admin.catalog.domain.category.CategoryID;
import com.helion.admin.catalog.domain.exceptions.DomainException;
import com.helion.admin.catalog.domain.exceptions.NotFoundException;
import com.helion.admin.catalog.domain.validation.Error;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultGetCategoryByIdUseCase extends GetCategoryByIdUseCase{

    private final CategoryGateway categoryGateway;

    public DefaultGetCategoryByIdUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public CategoryOutput execute(final String anId) {
        final var anCategoryId = CategoryID.from(anId);
        return this.categoryGateway.findById(anCategoryId)
                .map(CategoryOutput::from)
                .orElseThrow(notFound(anCategoryId));

    }

    private Supplier<NotFoundException> notFound(final CategoryID anId) {
        return () -> NotFoundException.with(Category.class, anId);
    }
}
