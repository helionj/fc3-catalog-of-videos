package com.helion.admin.catalog.application.video.media.update;

import com.helion.admin.catalog.domain.exceptions.NotFoundException;
import com.helion.admin.catalog.domain.video.*;

import java.util.Objects;

public class DefaultUpdateMediaStatusUseCase extends UpdateMediaStatusUseCase{

    private final VideoGateway videoGateway;

    public DefaultUpdateMediaStatusUseCase(VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public void execute(UpdateMediaStatusCommand aCmd) {
        final var anId = VideoID.from(aCmd.videoId());
        final var aResourceId = aCmd.resourceId();
        final var folder = aCmd.folder();
        final var filename = aCmd.filename();

        final var aVideo = this.videoGateway.findById(anId).orElseThrow(() -> notFound(anId));

        final var encodePath = "%s/%s".formatted(folder, filename);

        if (matches(aResourceId,aVideo.getVideo().orElse(null))) {

            updateVideo(VideoMediaType.VIDEO,aCmd.status(),aVideo,encodePath);

        } else if (matches(aResourceId,aVideo.getTrailer().orElse(null))) {

            updateVideo(VideoMediaType.TRAILER,aCmd.status(),aVideo,encodePath);
        }
    }

    private void updateVideo(final VideoMediaType aType, final MediaStatus aStatus, final Video aVideo, final String encodedPath) {
        switch (aStatus) {
            case PENDING -> {}
            case PROCESSING -> aVideo.processing(aType);
            case COMPLETED -> aVideo.completed(aType, encodedPath);
        }
        this.videoGateway.update(aVideo);
    }

    private boolean matches(final String anId, final AudioVideoMedia media){
        if (media == null) return false;
        return anId.equals(media.id());
    }

    private NotFoundException notFound(VideoID anId) {
        return NotFoundException.with(Video.class, anId);
    }
}
