package com.helion.catalog.application.category.save;

import com.helion.catalog.application.UseCase;
import com.helion.catalog.domain.category.Category;
import com.helion.catalog.domain.category.CategoryGateway;
import com.helion.catalog.domain.exceptions.NotificationException;
import com.helion.catalog.domain.validation.Error;
import com.helion.catalog.domain.validation.handler.Notification;

import java.util.Objects;

public class SaveCategoryUseCase extends UseCase<Category, Category> {

    private final CategoryGateway categoryGateway;

    public SaveCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Category execute(final Category aCategory) {
        if (aCategory == null){
            throw NotificationException.with(new Error("'aCategory' cannot be null"));
        }

        final var notification = Notification.create();
        aCategory.validate(notification);

        if (notification.hasError()){
            throw NotificationException.with("Invalid category", notification);
        }
        return this.categoryGateway.save(aCategory);
    }
}
