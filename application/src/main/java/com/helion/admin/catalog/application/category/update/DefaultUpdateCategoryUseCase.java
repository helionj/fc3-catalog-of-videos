package com.helion.admin.catalog.application.category.update;

import com.helion.admin.catalog.domain.category.Category;
import com.helion.admin.catalog.domain.category.CategoryGateway;
import com.helion.admin.catalog.domain.category.CategoryID;
import com.helion.admin.catalog.domain.exceptions.DomainException;
import com.helion.admin.catalog.domain.exceptions.NotFoundException;
import com.helion.admin.catalog.domain.validation.Error;
import com.helion.admin.catalog.domain.validation.handler.Notification;
import io.vavr.API;
import io.vavr.control.Either;


import java.util.Objects;
import java.util.function.Supplier;

public class DefaultUpdateCategoryUseCase extends UpdateCategoryUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultUpdateCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Either<Notification, UpdateCategoryOutput> execute(UpdateCategoryCommand aCommand) {
        final var anId = CategoryID.from(aCommand.id());
        final var aName = aCommand.name();
        final var aDescription = aCommand.description();
        final var isActive = aCommand.isActive();

        final var aCategory = this.categoryGateway.findById(anId)
                .orElseThrow(notFound(anId));
        final var notification = Notification.create();

        aCategory
                .update(aName, aDescription, isActive)
                .validate(notification);

        return  notification.hasErrors() ? API.Left(notification) : update(aCategory);
    }

    private Either<Notification, UpdateCategoryOutput> update(Category aCategory) {

        return API.Try(() ->  this.categoryGateway.update(aCategory))
                .toEither()
                .bimap(Notification::create, UpdateCategoryOutput::from);
    }

    private Supplier<DomainException> notFound(final CategoryID anId) {
        return () -> NotFoundException.with(Category.class, anId);
    }

}
