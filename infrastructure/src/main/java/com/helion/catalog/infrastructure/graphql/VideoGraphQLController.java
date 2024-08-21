package com.helion.catalog.infrastructure.graphql;

import com.helion.catalog.application.castmember.get.GetAllCastMembersByIdUseCase;
import com.helion.catalog.application.category.get.GetAllCategoriesByIdUseCase;
import com.helion.catalog.application.genre.get.GetAllGenresByIdUseCase;
import com.helion.catalog.application.video.list.ListVideoUseCase;
import com.helion.catalog.application.video.save.SaveVideoUseCase;
import com.helion.catalog.infrastructure.castmember.GqlCastMemberPresenter;
import com.helion.catalog.infrastructure.castmember.models.GqlCastMember;
import com.helion.catalog.infrastructure.category.GqlCategoryPresenter;
import com.helion.catalog.infrastructure.category.models.GqlCategory;
import com.helion.catalog.infrastructure.genre.GqlGenrePresenter;
import com.helion.catalog.infrastructure.genre.models.GqlGenre;
import com.helion.catalog.infrastructure.video.GqlVideoPresenter;
import com.helion.catalog.infrastructure.video.models.GqlVideo;
import com.helion.catalog.infrastructure.video.models.GqlVideoInput;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Controller
public class VideoGraphQLController {

    private final ListVideoUseCase listVideoUseCase;
    private final GetAllCategoriesByIdUseCase getAllCategoriesByIdUseCase;
    private final GetAllCastMembersByIdUseCase getAllCastMembersByIdUseCase;
    private final GetAllGenresByIdUseCase getAllGenresByIdUseCase;

    private final SaveVideoUseCase saveVideoUseCase;


    public VideoGraphQLController(final ListVideoUseCase listVideoUseCase,
                                  final GetAllCategoriesByIdUseCase getAllCategoriesByIdUseCase,
                                  final GetAllCastMembersByIdUseCase getAllCastMembersByIdUseCase,
                                  final GetAllGenresByIdUseCase getAllGenresByIdUseCase,
                                  final SaveVideoUseCase saveVideoUseCase) {
        this.listVideoUseCase = Objects.requireNonNull(listVideoUseCase);
        this.getAllCategoriesByIdUseCase = Objects.requireNonNull(getAllCategoriesByIdUseCase);
        this.getAllCastMembersByIdUseCase = Objects.requireNonNull(getAllCastMembersByIdUseCase);
        this.getAllGenresByIdUseCase = Objects.requireNonNull(getAllGenresByIdUseCase);
        this.saveVideoUseCase = Objects.requireNonNull(saveVideoUseCase);
    }

    @QueryMapping
    public List<GqlVideo> videos(
            @Argument final String search,
            @Argument final int page,
            @Argument final int perPage,
            @Argument final String sort,
            @Argument final String direction,
            @Argument final String rating,
            @Argument final Integer yearLaunched,
            @Argument final Set<String> castMembers,
            @Argument final Set<String> categories,
            @Argument final Set<String> genres

    ){
        final var input = new ListVideoUseCase.Input(page, perPage, search, sort, direction, rating, yearLaunched, categories, castMembers, genres);
        return this.listVideoUseCase.execute(input).map(GqlVideoPresenter::present).data();
    }

    @SchemaMapping(typeName = "Video", field = "castMembers")
    public List<GqlCastMember> castMembers(final GqlVideo video){
        return this.getAllCastMembersByIdUseCase.execute(new GetAllCastMembersByIdUseCase.Input(video.castMembersId()))
                .stream()
                .map(GqlCastMemberPresenter::present).toList();
    }

    @SchemaMapping(typeName = "Video", field = "categories")
    public List<GqlCategory> categories(final GqlVideo video){
        return this.getAllCategoriesByIdUseCase.execute(new GetAllCategoriesByIdUseCase.Input(video.categoriesId()))
                .stream()
                .map(GqlCategoryPresenter::present).toList();
    }
    @SchemaMapping(typeName = "Video", field = "genres")
    public List<GqlGenre> genres(final GqlVideo video){
        return this.getAllGenresByIdUseCase.execute(new GetAllGenresByIdUseCase.Input(video.genresId()))
                .stream()
                .map(GqlGenrePresenter::present).toList();
    }

    @MutationMapping
    public SaveVideoUseCase.Output saveVideo(@Argument(name = "input") final GqlVideoInput arg){
        final var input = new SaveVideoUseCase.Input(
                arg.id(), arg.title(), arg.description(),
                arg.yearLaunched(), arg.duration(), arg.rating(),
                arg.opened(), arg.published(),
                arg.createdAt(), arg.updatedAt(), arg.banner(),
                arg.thumbnail(), arg.thumbnailHalf(), arg.trailer(),
                arg.video(), arg.categoriesId(), arg.genresId(), arg.castMembersId());
        final var out = this.saveVideoUseCase.execute(input);
        return out;

    }

}
