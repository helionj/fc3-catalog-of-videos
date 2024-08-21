package com.helion.catalog.infrastructure.graphql;

import com.helion.catalog.application.genre.list.ListGenreUseCase;
import com.helion.catalog.application.genre.save.SaveGenreUseCase;
import com.helion.catalog.infrastructure.genre.GqlGenrePresenter;
import com.helion.catalog.infrastructure.genre.models.GqlGenreInput;
import com.helion.catalog.infrastructure.genre.models.GqlGenre;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Controller
public class GenreGraphQLController {

    private final ListGenreUseCase listGenreUseCase;
    private final SaveGenreUseCase saveGenreUseCase;


    public GenreGraphQLController(ListGenreUseCase listGenreUseCase, SaveGenreUseCase saveGenreUseCase) {
        this.listGenreUseCase = Objects.requireNonNull(listGenreUseCase);
        this.saveGenreUseCase = Objects.requireNonNull(saveGenreUseCase);
    }

    @QueryMapping
    public List<GqlGenre> genres(
            @Argument final String search,
            @Argument final int page,
            @Argument final int perPage,
            @Argument final String sort,
            @Argument final String direction,
            @Argument final Set<String> categories

    ){
        final var input = new ListGenreUseCase.Input(page, perPage, search, sort, direction, categories);
        return this.listGenreUseCase.execute(input).map(GqlGenrePresenter::present).data();
    }

    @MutationMapping
    public SaveGenreUseCase.Output saveGenre(@Argument(name = "input") final GqlGenreInput arg){
        final var input = new SaveGenreUseCase.Input(
                arg.id(),
                arg.name(),
                arg.categories(),
                arg.active(),
                arg.createdAt(),
                arg.updatedAt(),
                arg.deletedAt());
        final var out = this.saveGenreUseCase.execute(input);
        return out;

    }


}
