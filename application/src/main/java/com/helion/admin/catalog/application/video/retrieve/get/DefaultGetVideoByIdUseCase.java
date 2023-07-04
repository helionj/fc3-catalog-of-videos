package com.helion.admin.catalog.application.video.retrieve.get;

import com.helion.admin.catalog.domain.exceptions.NotFoundException;
import com.helion.admin.catalog.domain.video.Video;
import com.helion.admin.catalog.domain.video.VideoGateway;
import com.helion.admin.catalog.domain.video.VideoID;

import java.util.Objects;

public class DefaultGetVideoByIdUseCase extends GetVideoByIdUseCase{

    private final VideoGateway videoGateway;

    public DefaultGetVideoByIdUseCase(VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public VideoOutput execute(String anId) {
        final var aVideoId = VideoID.from(anId);
        return this.videoGateway.findById(VideoID.from(anId))
                .map(VideoOutput::from)
                .orElseThrow(() -> NotFoundException.with(Video.class, aVideoId));

    }
}
