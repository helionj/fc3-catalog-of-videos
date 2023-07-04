package com.helion.admin.catalog.application.video.media.get;

import com.helion.admin.catalog.domain.resource.Resource;

public record MediaOutput(

        byte[] content,
        String contentType,
        String name

) {

    public static MediaOutput with(final Resource aResource) {
        return new MediaOutput(aResource.content(), aResource.contentType(),aResource.name());
    }
}
