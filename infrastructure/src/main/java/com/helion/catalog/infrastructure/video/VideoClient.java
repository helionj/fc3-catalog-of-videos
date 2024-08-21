package com.helion.catalog.infrastructure.video;

import com.helion.catalog.infrastructure.video.models.VideoDTO;

import java.util.Optional;

public interface VideoClient {

    Optional<VideoDTO> videoOfId(String genreId);

}
