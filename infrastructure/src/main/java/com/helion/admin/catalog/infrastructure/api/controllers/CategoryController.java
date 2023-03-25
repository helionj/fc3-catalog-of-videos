package com.helion.admin.catalog.infrastructure.api.controllers;

import com.helion.admin.catalog.application.category.create.CreateCategoryCommand;
import com.helion.admin.catalog.application.category.create.CreateCategoryOutput;
import com.helion.admin.catalog.application.category.create.CreateCategoryUseCase;
import com.helion.admin.catalog.application.category.delete.DeleteCategoryUseCase;
import com.helion.admin.catalog.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.helion.admin.catalog.application.category.retrieve.list.ListCategoriesUseCase;
import com.helion.admin.catalog.application.category.update.UpdateCategoryCommand;
import com.helion.admin.catalog.application.category.update.UpdateCategoryOutput;
import com.helion.admin.catalog.application.category.update.UpdateCategoryUseCase;
import com.helion.admin.catalog.domain.category.pagination.SearchQuery;
import com.helion.admin.catalog.domain.category.pagination.Pagination;
import com.helion.admin.catalog.domain.validation.handler.Notification;
import com.helion.admin.catalog.infrastructure.api.CategoryAPI;
import com.helion.admin.catalog.infrastructure.category.models.CategoryListResponse;
import com.helion.admin.catalog.infrastructure.category.models.CategoryResponse;
import com.helion.admin.catalog.infrastructure.category.models.CreateCategoryRequest;
import com.helion.admin.catalog.infrastructure.category.models.UpdateCategoryRequest;
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

    private DeleteCategoryUseCase deleteCategoryUseCase;

    private ListCategoriesUseCase listCategoriesUseCase;

    public CategoryController(final CreateCategoryUseCase createCategoryUseCase,
                              final GetCategoryByIdUseCase getCategoryByIdUseCase,
                              final UpdateCategoryUseCase updateCategoryUseCase,
                              final DeleteCategoryUseCase deleteCategoryUseCase,
                              final ListCategoriesUseCase listCategoriesUseCase) {
        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
        this.getCategoryByIdUseCase = Objects.requireNonNull(getCategoryByIdUseCase);
        this.updateCategoryUseCase = Objects.requireNonNull(updateCategoryUseCase);
        this.deleteCategoryUseCase = Objects.requireNonNull(deleteCategoryUseCase);
        this.listCategoriesUseCase = Objects.requireNonNull(listCategoriesUseCase);
    }

    @Override
    public ResponseEntity<?> createCategory(final CreateCategoryRequest input) {

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
    public Pagination<CategoryListResponse> listCategories(
            String search,
            int page,
            int perPage,
            String sort,
            String direction) {
        return listCategoriesUseCase.execute(new SearchQuery(page, perPage, search, sort, direction))
                .map(CategoryApiPresenter::present);
    }

    @Override
    public CategoryResponse getById(final String id) {
        return CategoryApiPresenter.present(this.getCategoryByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateCategoryRequest input) {
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

    @Override
    public void deleteById(final String anId) {
        this.deleteCategoryUseCase.execute(anId);
    }
}
