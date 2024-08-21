package com.helion.catalog.application.genre.list;

import com.helion.catalog.application.UseCase;
import com.helion.catalog.domain.genre.Genre;
import com.helion.catalog.domain.genre.GenreGateway;
import com.helion.catalog.domain.genre.GenreSearchQuery;
import com.helion.catalog.domain.pagination.Pagination;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;

public class ListGenreUseCase extends UseCase<ListGenreUseCase.Input, Pagination<ListGenreUseCase.Output>> {

    private final GenreGateway genreGateway;

    public ListGenreUseCase(GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public Pagination<Output> execute(Input input) {

        final var aQuery = new GenreSearchQuery(
                input.page(),
                input.perPage(),
                input.terms(),
                input.sort(),
                input.direction(),
                input.categories()
        );

        return this.genreGateway.findAll(aQuery)
                .map(Output::from);
    }


    public record Input(
            int page,
            int perPage,
            String terms,
            String sort,
            String direction,
            Set<String> categories
    ){}

    public record Output(
            String id,
            String name,
            Set<String> categories,
            Boolean active,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt
    ){
        public static Output from(Genre genre){

            return new Output(
                    genre.id(),
                    genre.name(),
                    genre.categories(),
                    genre.active(),
                    genre.createdAt(),
                    genre.updatedAt(),
                    genre.deletedAt()
            );
        }
    }


}
