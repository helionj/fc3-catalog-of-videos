package com.helion.catalog.application.video.get;

import com.helion.catalog.application.UseCase;
import com.helion.catalog.domain.video.Video;
import com.helion.catalog.domain.video.VideoGateway;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class GetVideoUseCase extends UseCase<GetVideoUseCase.Input, Optional<GetVideoUseCase.Output>> {

    private final VideoGateway videoGateway;

    public GetVideoUseCase(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public Optional<Output> execute(Input input) {
        if (input == null || input.videoId() == null) {
            return Optional.empty();
        }
        return videoGateway.findById(input.videoId())
                .map(Output::from);
    }


    public record Input(String videoId){}

    public record Output(
            String id,
            String title,
            String description,
            Integer launchedAt,
            double duration,
            String rating,
            boolean opened,
            boolean published,
            String createdAt,
            String updatedAt,
            String banner,
            String thumb,
            String thumbHalf,
            String trailer,
            String video,
            Set<String> categories,
            Set<String> genres,
            Set<String> castMembers
    ){
        public static Output from(Video video){
            return new Output(
                    video.id(),
                    video.title(),
                    video.description(),
                    video.launchedAt().getValue(),
                    video.duration(),
                    video.rating().getName(),
                    video.opened(),
                    video.published(),
                    video.createdAt().toString(),
                    video.updatedAt().toString(),
                    video.banner(),
                    video.thumb(),
                    video.thumbHalf(),
                    video.trailer(),
                    video.video(),
                    video.categories(),
                    video.genres(),
                    video.castMembers()
            );
        }
    }


}
