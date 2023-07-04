package com.helion.admin.catalog.application.video.media.upload;

import com.helion.admin.catalog.domain.video.Video;
import com.helion.admin.catalog.domain.video.VideoMediaType;

public record UploadMediaOutput(
        String videoId,
        VideoMediaType mediaType
) {
    public static UploadMediaOutput with(final Video aVideo, final VideoMediaType aType){
        return new UploadMediaOutput(
                aVideo.getId().getValue(),
                aType
        );
    }
}
