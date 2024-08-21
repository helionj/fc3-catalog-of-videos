package com.helion.catalog.application.video.save;

import com.helion.catalog.application.UseCase;
import com.helion.catalog.domain.exceptions.DomainException;
import com.helion.catalog.domain.validation.Error;
import com.helion.catalog.domain.video.Video;
import com.helion.catalog.domain.video.VideoGateway;

import java.util.Objects;
import java.util.Set;

public class SaveVideoUseCase extends UseCase<SaveVideoUseCase.Input, SaveVideoUseCase.Output> {

    private final VideoGateway videoGateway;

    public SaveVideoUseCase(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public Output execute(final Input input) {
        if (input == null){
            throw DomainException.with(new Error("'SaveVideoUseCase.Input' cannot be null"));
        }
        final var aVideo = Video.with(
                input.id(),
                input.title(),
                input.description(),
                input.launchedAt(),
                input.duration(),
                input.rating(),
                input.opened(),
                input.published(),
                input.createdAt(),
                input.updatedAt(),
                input.banner(),
                input.thumb(),
                input.thumbHalf,
                input.trailer(),
                input.video(),
                input.categories(),
                input.genres(),
                input.castMembers()
        );
        this.videoGateway.save(aVideo);

        return new Output(aVideo.id());
    }

    public record Input(
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
    ){}

    public record Output(String id) {}
}
