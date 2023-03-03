package com.helion.admin.catalog.application.category.create;

import com.helion.admin.catalog.application.UseCase;
import com.helion.admin.catalog.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateCategoryUseCase
        extends UseCase<CreateCategoryCommand, Either<Notification, CreateCategoryOutput>> {

}
