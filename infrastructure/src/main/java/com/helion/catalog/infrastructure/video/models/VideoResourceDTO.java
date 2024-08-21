package com.helion.catalog.infrastructure.video.models;

public record VideoResourceDTO(
        String id,
        String checksum,
        String name,
        String location,
        String encodedLocation,
        String status

) {
}
