package com.helion.catalog.infrastructure.video.models;

public record ImageResourceDTO(
        String id,
        String name,
        String checksum,
        String location

) {
}
