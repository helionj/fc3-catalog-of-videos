package com.helion.admin.catalog.domain.video;


import com.helion.admin.catalog.domain.resource.Resource;

import java.util.Optional;

public interface MediaResourceGateway {

    AudioVideoMedia storeAudioVideo(VideoID anId, VideoResource aResource);
    ImageMedia storeImage(VideoID anId, VideoResource aResource);
    void clearResources(VideoID anId);

    Optional<Resource> getResource(VideoID anId, VideoMediaType type);
}
