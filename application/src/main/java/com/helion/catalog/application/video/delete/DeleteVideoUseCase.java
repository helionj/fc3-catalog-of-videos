package com.helion.catalog.application.video.delete;

import com.helion.catalog.application.UnitUseCase;
import com.helion.catalog.domain.video.VideoGateway;

import java.util.Objects;

public class DeleteVideoUseCase extends UnitUseCase<DeleteVideoUseCase.Input>{

    private final VideoGateway videoGateway;

    public DeleteVideoUseCase(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public void execute(final Input input) {
        if (input == null || input.genreId() == null) {
            return;
        }

        this.videoGateway.deleteById(input.genreId());

    }

    public record Input(String genreId) {}
}
