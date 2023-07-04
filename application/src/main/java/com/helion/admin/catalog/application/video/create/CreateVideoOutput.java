package com.helion.admin.catalog.application.video.create;

import com.helion.admin.catalog.domain.video.Video;

public record CreateVideoOutput(String id) {

    public static CreateVideoOutput from(final Video aVideo){
        return new CreateVideoOutput(aVideo.getId().getValue());
    }
}
