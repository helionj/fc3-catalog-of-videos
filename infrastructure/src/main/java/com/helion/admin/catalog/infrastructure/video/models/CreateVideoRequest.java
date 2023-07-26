package com.helion.admin.catalog.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public record CreateVideoRequest (
        @JsonProperty("title") String title,
        @JsonProperty("description") String description,
        @JsonProperty("year_launched") Integer yaerLaunched,
        @JsonProperty("duration") Double duration,
        @JsonProperty("opened") Boolean opened,
        @JsonProperty("published") Boolean published,
        @JsonProperty("rating") String rating,
        @JsonProperty("categories") Set<String> categories,
        @JsonProperty("genres") Set<String> genres,
        @JsonProperty("cast_members") Set<String> castMembers
) {
}
