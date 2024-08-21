package com.helion.catalog.application.genre.get;

import com.helion.catalog.application.UseCase;
import com.helion.catalog.domain.genre.Genre;
import com.helion.catalog.domain.genre.GenreGateway;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class GetAllGenresByIdUseCase extends UseCase<GetAllGenresByIdUseCase.Input, List<GetAllGenresByIdUseCase.Output>> {

    private final GenreGateway genreGateway;

    public GetAllGenresByIdUseCase(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public List<Output> execute(Input in) {
        if(in.ids == null || in.ids().isEmpty()){
            return List.of();
        }
        return this.genreGateway.findAllById(in.ids())
                .stream()
                .map(Output::new)
                .toList();
    }

    public record Input(Set<String> ids){
        @Override
        public Set<String> ids() {
            return ids != null ? ids : Collections.emptySet();
        }
    }

    public record Output(
            String id,
            String name,
            boolean active,
            Set<String> categories,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt

    ){
        public Output(Genre aGenre){
            this(
                    aGenre.id(),
                    aGenre.name(),
                    aGenre.active(),
                    aGenre.categories(),
                    aGenre.createdAt(),
                    aGenre.updatedAt(),
                    aGenre.deletedAt()

            );
        }
    }
}
