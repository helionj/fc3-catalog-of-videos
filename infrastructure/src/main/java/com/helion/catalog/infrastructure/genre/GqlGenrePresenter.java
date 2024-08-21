package com.helion.catalog.infrastructure.genre;

import com.helion.catalog.application.genre.get.GetAllGenresByIdUseCase;
import com.helion.catalog.application.genre.list.ListGenreUseCase;
import com.helion.catalog.domain.genre.Genre;
import com.helion.catalog.infrastructure.genre.models.GqlGenre;

import java.time.Instant;

public final class GqlGenrePresenter {

    private GqlGenrePresenter(){}

    public static GqlGenre present(final ListGenreUseCase.Output out){
        return new GqlGenre(
                out.id(),
                out.name(),
                out.categories(),
                out.active(),
                formatDate(out.createdAt()),
                formatDate(out.updatedAt()),
                formatDate(out.deletedAt()));
    }

    public static GqlGenre present(final Genre out){
        return new GqlGenre(
                out.id(),
                out.name(),
                out.categories(),
                out.active(),
                formatDate(out.createdAt()),
                formatDate(out.updatedAt()),
                formatDate(out.deletedAt()));
    }

    public static GqlGenre present(final GetAllGenresByIdUseCase.Output out){
        return new GqlGenre(
                out.id(),
                out.name(),
                out.categories(),
                out.active(),
                formatDate(out.createdAt()),
                formatDate(out.updatedAt()),
                formatDate(out.deletedAt()));
    }

    private static String formatDate(final Instant date) {
        return date != null ? date.toString() : "";
    }
}
