package com.helion.admin.catalog.application.video.media.upload;

import com.helion.admin.catalog.domain.video.VideoID;
import com.helion.admin.catalog.domain.video.VideoResource;

public record UploadMediaCommand(
        String videoId,
        VideoResource videoResource) {
    public static UploadMediaCommand with(final String anId, final VideoResource aResource) {
        return new UploadMediaCommand(anId, aResource);
    }
}
