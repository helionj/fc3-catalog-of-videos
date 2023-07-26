package com.helion.admin.catalog.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.helion.admin.catalog.domain.video.VideoMediaType;

public record UploadMediaResponse(
        @JsonProperty("video_id") String videoId,
        @JsonProperty("media_type") VideoMediaType mediaType
        ) { }
