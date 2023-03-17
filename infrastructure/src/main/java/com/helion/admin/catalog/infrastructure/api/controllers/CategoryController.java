package com.helion.admin.catalog.infrastructure.api.controllers;

import com.helion.admin.catalog.application.category.create.CreateCategoryCommand;
import com.helion.admin.catalog.application.category.create.CreateCategoryOutput;
import com.helion.admin.catalog.application.category.create.CreateCategoryUseCase;
import com.helion.admin.catalog.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.helion.admin.catalog.application.category.update.UpdateCategoryCommand;
import com.helion.admin.catalog.application.category.update.UpdateCategoryOutput;
import com.helion.admin.catalog.application.category.update.UpdateCategoryUseCase;
import com.helion.admin.catalog.domain.category.pagination.Pagination;
import com.helion.admin.catalog.domain.validation.handler.Notification;
import com.helion.admin.catalog.infrastructure.api.CategoryAPI;
import com.helion.admin.catalog.infrastructure.category.models.CategoryApiOutput;
import com.helion.admin.catalog.infrastructure.category.models.CreateCategoryApiInput;
import com.helion.admin.catalog.infrastructure.category.models.UpdateCategoryApiInput;
import com.helion.admin.catalog.infrastructure.category.presenters.CategoryApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;

@RestController
public class CategoryController implements CategoryAPI {

    private CreateCategoryUseCase createCategoryUseCase;

    private GetCategoryByIdUseCase getCategoryByIdUseCase;

    private UpdateCategoryUseCase updateCategoryUseCase;

    public CategoryController(final CreateCategoryUseCase createCategoryUseCase, final GetCategoryByIdUseCase getCategoryByIdUseCase, UpdateCategoryUseCase updateCategoryUseCase) {
        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
        this.getCategoryByIdUseCase = Objects.requireNonNull(getCategoryByIdUseCase);
        this.updateCategoryUseCase = Objects.requireNonNull(updateCategoryUseCase);
    }

    @Override
    public ResponseEntity<?> createCategory(final CreateCategoryApiInput input) {

        final var aCommand =CreateCategoryCommand.with(
                input.name(),
                input.description(),
                input.active() != null ? input.active() : true
        );
        final Function<Notification, ResponseEntity<?>> onError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        final Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess = output ->
                ResponseEntity.created(URI.create("/categories/"+output.id())).body(output);

        return this.createCategoryUseCase.execute(aCommand)
                .fold(onError, onSuccess);
    }

    @Override
    public Pagination<?> listCategories(String search, int page, int perPage, String sort, String direction) {
        return null;
    }

    @Override
    public CategoryApiOutput getById(final String id) {
        return CategoryApiPresenter.present(this.getCategoryByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateCategoryApiInput input) {
        final var aCommand = UpdateCategoryCommand.with(
                id,
                input.name(),
                input.description(),
                input.active() != null ? input.active() : true
        );
        final Function<Notification, ResponseEntity<?>> onError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        final Function<UpdateCategoryOutput, ResponseEntity<?>> onSuccess = ResponseEntity::ok;

        return this.updateCategoryUseCase.execute(aCommand)
                .fold(onError, onSuccess);
    }
}
