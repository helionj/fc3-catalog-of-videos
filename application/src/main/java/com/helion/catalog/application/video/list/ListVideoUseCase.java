package com.helion.catalog.application.video.list;

import com.helion.catalog.application.UseCase;
import com.helion.catalog.domain.pagination.Pagination;
import com.helion.catalog.domain.video.Video;
import com.helion.catalog.domain.video.VideoGateway;
import com.helion.catalog.domain.video.VideoSearchQuery;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;

public class ListVideoUseCase extends UseCase<ListVideoUseCase.Input, Pagination<ListVideoUseCase.Output>> {

    private final VideoGateway videoGateway;

    public ListVideoUseCase(VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public Pagination<Output> execute(Input input) {

        final var aQuery = new VideoSearchQuery(
                input.page(),
                input.perPage(),
                input.terms(),
                input.sort(),
                input.direction(),
                input.rating(),
                input.launchedAt(),
                input.castMembers(),
                input.categories(),
                input.genres()
        );

        return this.videoGateway.findAll(aQuery)
                .map(Output::from);
    }


    public record Input(
            int page,
            int perPage,
            String terms,
            String sort,
            String direction,
            String rating,
            Integer launchedAt,
            Set<String> categories,
            Set<String> castMembers,
            Set<String> genres
    ){}

    public record Output(
            String id,
            String title,
            String description,
            boolean published,
            int yearLaunched,
            String rating,
            Double duration,
            boolean opened,
            String trailer,
            String banner,
            String thumbnail,
            String thumbnailHalf,
            String video,
            Set<String> categoriesId,
            Set<String> castMembersId,
            Set<String> genresId,
            Instant createdAt,
            Instant updatedAt

    ){
        public static Output from(Video video){

            return new Output(
                    video.id(),
                    video.title(),
                    video.description(),
                    video.published(),
                    video.launchedAt().getValue(),
                    video.rating().getName(),
                    video.duration(),
                    video.opened(),
                    video.trailer(),
                    video.banner(),
                    video.thumb(),
                    video.thumbHalf(),
                    video.video(),
                    video.categories(),
                    video.castMembers(),
                    video.genres(),
                    video.createdAt(),
                    video.updatedAt()
            );
        }
    }


}
