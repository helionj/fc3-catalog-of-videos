package com.helion.admin.catalog.application.video.media.upload;

import com.helion.admin.catalog.domain.exceptions.NotFoundException;
import com.helion.admin.catalog.domain.video.MediaResourceGateway;
import com.helion.admin.catalog.domain.video.Video;
import com.helion.admin.catalog.domain.video.VideoGateway;
import com.helion.admin.catalog.domain.video.VideoID;

import java.util.Objects;

public class DefaultUploadMediaUseCase extends UploadMediaUseCase{

    private final VideoGateway videoGateway;
    private final MediaResourceGateway mediaResourceGateway;

    public DefaultUploadMediaUseCase(final VideoGateway videoGateway, final MediaResourceGateway mediaResourceGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
    }

    @Override
    public UploadMediaOutput execute(final UploadMediaCommand aCmd) {
        final var anId = VideoID.from(aCmd.videoId());
        final var aResource = aCmd.videoResource();
        final var aVideo = this.videoGateway.findById(anId).orElseThrow(() ->notFound(anId));

        switch (aResource.getType()){
            case VIDEO -> aVideo.setVideo(mediaResourceGateway.storeAudioVideo(anId,aResource));
            case TRAILER -> aVideo.setTrailer(mediaResourceGateway.storeAudioVideo(anId,aResource));
            case BANNER -> aVideo.setBanner(mediaResourceGateway.storeImage(anId,aResource));
            case THUMBNAIL -> aVideo.setThumbnail(mediaResourceGateway.storeImage(anId,aResource));
            case THUMBNAIL_HALF -> aVideo.setThumbnailHalf(mediaResourceGateway.storeImage(anId,aResource));
        }
        return UploadMediaOutput.with(this.videoGateway.update(aVideo), aResource.getType());
    }

    private NotFoundException notFound(VideoID anId) {
        return NotFoundException.with(Video.class, anId);
    }
}
