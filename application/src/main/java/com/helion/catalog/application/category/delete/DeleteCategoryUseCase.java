package com.helion.catalog.application.category.delete;

import com.helion.catalog.application.UnitUseCase;
import com.helion.catalog.domain.category.CategoryGateway;

import java.util.Objects;

public class DeleteCategoryUseCase extends UnitUseCase<String> {

    private final CategoryGateway categoryGateway;

    public DeleteCategoryUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public void execute(String anId) {
        if(anId == null) {
            return;
        }
        this.categoryGateway.deleteById(anId);
    }
}
