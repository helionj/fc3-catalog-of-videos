package com.helion.admin.catalog.infrastructure.api.genre.controller;

import com.helion.admin.catalog.application.genre.create.CreateGenreCommand;
import com.helion.admin.catalog.application.genre.create.CreateGenreUseCase;
import com.helion.admin.catalog.application.genre.delete.DeleteGenreUseCase;
import com.helion.admin.catalog.application.genre.retrieve.get.GetGenreByIdUseCase;
import com.helion.admin.catalog.application.genre.retrieve.list.ListGenreUseCase;
import com.helion.admin.catalog.application.genre.update.UpdateGenreCommand;
import com.helion.admin.catalog.application.genre.update.UpdateGenreUseCase;
import com.helion.admin.catalog.domain.category.pagination.Pagination;
import com.helion.admin.catalog.domain.category.pagination.SearchQuery;
import com.helion.admin.catalog.infrastructure.api.genre.GenreAPI;
import com.helion.admin.catalog.infrastructure.genre.models.CreateGenreRequest;
import com.helion.admin.catalog.infrastructure.genre.models.GenreListResponse;
import com.helion.admin.catalog.infrastructure.genre.models.GenreResponse;
import com.helion.admin.catalog.infrastructure.genre.models.UpdateGenreRequest;
import com.helion.admin.catalog.infrastructure.genre.presenters.GenreApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class GenreController implements GenreAPI {


    private final CreateGenreUseCase createGenreUseCase;

    private final GetGenreByIdUseCase getGenreByIdUseCase;

    private final UpdateGenreUseCase updateGenreUseCase;

    private final DeleteGenreUseCase deleteGenreUseCase;

    private final ListGenreUseCase listGenreUseCase;

    public GenreController(
            CreateGenreUseCase createGenreUseCase,
            GetGenreByIdUseCase getGenreByIdUseCase,
            UpdateGenreUseCase updateGenreUseCase,
            DeleteGenreUseCase deleteGenreUseCase, ListGenreUseCase listGenreUseCase) {
        this.createGenreUseCase = Objects.requireNonNull(createGenreUseCase);
        this.getGenreByIdUseCase = Objects.requireNonNull(getGenreByIdUseCase);
        this.updateGenreUseCase = Objects.requireNonNull(updateGenreUseCase);
        this.deleteGenreUseCase = Objects.requireNonNull(deleteGenreUseCase);
        this.listGenreUseCase = Objects.requireNonNull(listGenreUseCase);
    }

    @Override
    public ResponseEntity<?> createGenre(CreateGenreRequest input) {

        final var aCommand = CreateGenreCommand.with(
                input.name(),
                input.isActive(),
                input.categories()

        );
        final var output = this.createGenreUseCase.execute(aCommand);
        return ResponseEntity.created(URI.create("/genres/"+output.id())).body(output);
    }

    @Override
    public Pagination<GenreListResponse> listGenres(String search, int page, int perPage, String sort, String direction) {
        return listGenreUseCase.execute(new SearchQuery(page, perPage, search, sort, direction))
                .map(GenreApiPresenter::present);
    }

    @Override
    public GenreResponse getById(String id) {
        return GenreApiPresenter.present(this.getGenreByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(String id, UpdateGenreRequest input) {
        final var aCommand = UpdateGenreCommand.with(
                id,
                input.name(),
                input.active(),
                input.categories()
        );
        final var output = this.updateGenreUseCase.execute(aCommand);
        return ResponseEntity.ok().body(output);
    }

    @Override
    public void deleteById(String id) {
        this.deleteGenreUseCase.execute(id);
    }
}
