package com.helion.admin.catalog.application.video.retrieve.get;

import com.helion.admin.catalog.domain.Identifier;
import com.helion.admin.catalog.domain.castmember.CastMemberID;
import com.helion.admin.catalog.domain.category.CategoryID;
import com.helion.admin.catalog.domain.genre.GenreID;
import com.helion.admin.catalog.domain.utils.CollectionUtils;
import com.helion.admin.catalog.domain.video.AudioVideoMedia;
import com.helion.admin.catalog.domain.video.ImageMedia;
import com.helion.admin.catalog.domain.video.Video;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

public record VideoOutput(
        String id,
        String title,
        String description,
        Integer launchedAt,
        Double duration,
        Boolean opened,
        Boolean published,
        String rating,
        Set<String> categories,
        Set<String> genres,
        Set<String> castMembers,
        Instant createdAt,
        Instant updatedAt,
        ImageMedia banner,
        ImageMedia thumbNail,
        ImageMedia thumbNailHalf,
        AudioVideoMedia video,
        AudioVideoMedia trailer


) {
    public static VideoOutput from(final Video aVideo){
        return new VideoOutput(
                aVideo.getId().getValue(),
                aVideo.getTitle(),
                aVideo.getDescription(),
                aVideo.getLaunchedAt().getValue(),
                aVideo.getDuration(),
                aVideo.isOpened(),
                aVideo.isPublished(),
                aVideo.getRating().getName(),
                CollectionUtils.mapTo(aVideo.getCategories(), Identifier::getValue),
                CollectionUtils.mapTo(aVideo.getGenres(), Identifier::getValue),
                CollectionUtils.mapTo(aVideo.getCastMembers(), Identifier::getValue),
                aVideo.getCreatedAt(),
                aVideo.getUpdatedAt(),
                aVideo.getBanner().orElse(null),
                aVideo.getThumbnail().orElse(null),
                aVideo.getThumbnailHalf().orElse(null),
                aVideo.getVideo().orElse(null),
                aVideo.getTrailer().orElse(null)
        );
    }
}
