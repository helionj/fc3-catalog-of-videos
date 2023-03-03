package com.helion.admin.catalog.application.category.update;

import com.helion.admin.catalog.application.UseCase;
import com.helion.admin.catalog.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class UpdateCategoryUseCase
        extends UseCase<UpdateCategoryCommand, Either<Notification, UpdateCategoryOutput>> {

}
