package com.helion.admin.catalog.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.helion.admin.catalog.domain.video.AudioVideoMedia;

public record AudioVideoMediaResponse(
        @JsonProperty("id") String id,
        @JsonProperty("checksum") String checksum,
        @JsonProperty("name") String name,
        @JsonProperty("location") String rawLocation,
        @JsonProperty("encoded_location") String encodedLocation,
        @JsonProperty("status") String status
) {
    public static AudioVideoMediaResponse with(AudioVideoMedia media){
        return new AudioVideoMediaResponse(
                media.id(),
                media.checksum(),
                media.name(),
                media.rawLocation(),
                media.encodedLocation(),
                media.status().name()
        );
    }
}
