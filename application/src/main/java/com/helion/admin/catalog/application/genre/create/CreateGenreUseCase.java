package com.helion.admin.catalog.application.genre.create;

import com.helion.admin.catalog.application.UseCase;
import com.helion.admin.catalog.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateGenreUseCase extends
        UseCase<CreateGenreCommand, CreateGenreOutput> {
}
