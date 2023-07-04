package com.helion.admin.catalog.application.video.media.get;

import com.helion.admin.catalog.application.video.retrieve.get.VideoOutput;
import com.helion.admin.catalog.domain.exceptions.DomainException;
import com.helion.admin.catalog.domain.exceptions.NotFoundException;
import com.helion.admin.catalog.domain.validation.Error;
import com.helion.admin.catalog.domain.video.MediaResourceGateway;
import com.helion.admin.catalog.domain.video.VideoID;
import com.helion.admin.catalog.domain.video.VideoMediaType;

import java.util.Objects;

public class DefaultGetMediaUseCase extends GetMediaUseCase{

    private final MediaResourceGateway mediaResourceGateway;

    public DefaultGetMediaUseCase(MediaResourceGateway mediaResourceGateway) {
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
    }

    @Override
    public MediaOutput execute(final GetMediaCommand aCmd) {
        final var anId = VideoID.from(aCmd.videoId());
        final var aType = VideoMediaType.of(aCmd.mediaType())
                .orElseThrow(() -> typeNotFound(aCmd.mediaType()));

        final var aResource = this.mediaResourceGateway
                .getResource(anId,aType)
                .orElseThrow(() -> notFound(anId.getValue(), aType.name()));
        return MediaOutput.with(aResource);
    }

    private NotFoundException typeNotFound(String mediaType) {
        return NotFoundException.with(new Error("Media Type %s doesn't exists".formatted(mediaType)));
    }

    private NotFoundException notFound(String anId, String aType) {
        return NotFoundException.with(new Error("Resource %s not found for video %s".formatted(aType, anId)));
    }
}
