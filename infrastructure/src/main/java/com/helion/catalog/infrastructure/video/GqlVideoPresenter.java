package com.helion.catalog.infrastructure.video;

import com.helion.catalog.application.video.list.ListVideoUseCase;
import com.helion.catalog.infrastructure.video.models.GqlVideo;

import java.time.Instant;

public final class GqlVideoPresenter {

    private GqlVideoPresenter(){}

    public static GqlVideo present(final ListVideoUseCase.Output out){
        return new GqlVideo(
                out.id(),
                out.title(),
                out.description(),
                out.published(),
                out.yearLaunched(),
                out.rating(),
                out.duration(),
                out.opened(),
                out.trailer(),
                out.banner(),
                out.thumbnail(),
                out.thumbnailHalf(),
                out.video(),
                out.castMembersId(),
                out.categoriesId(),
                out.genresId(),
                formatDate(out.createdAt()),
                formatDate(out.updatedAt())
        );
    }



    private static String formatDate(final Instant date) {
        return date != null ? date.toString() : "";
    }
}
