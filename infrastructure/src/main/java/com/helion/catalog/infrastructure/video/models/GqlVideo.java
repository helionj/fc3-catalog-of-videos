package com.helion.catalog.infrastructure.video.models;

import java.util.Set;

public record GqlVideo(
        String id,
        String title,
        String description,
        boolean published,
        int yearLaunched,
        String rating,
        Double duration,
        boolean opened,
        String trailer,
        String banner,
        String thumbnail,
        String thumbnailHalf,
        String video,
        Set<String> castMembersId,
        Set<String> categoriesId,
        Set<String> genresId,
        String createdAt,
        String updatedAt
) {
}
