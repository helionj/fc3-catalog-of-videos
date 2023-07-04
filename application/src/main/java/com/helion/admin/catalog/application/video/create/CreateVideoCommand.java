package com.helion.admin.catalog.application.video.create;

import com.helion.admin.catalog.domain.resource.Resource;

import java.util.Optional;
import java.util.Set;

public record CreateVideoCommand(
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
        Resource video,
        Resource trailer,
        Resource banner,
        Resource thumbnail,
        Resource thumbnailHalf)
{
    public static CreateVideoCommand with(
                                           final String title,
                                           final String description,
                                           final Integer launchedAt,
                                           final Double duration,
                                           final Boolean opened,
                                           final Boolean published,
                                           final String rating,
                                           final Set<String> categories,
                                           final Set<String> genres,
                                           final Set<String> castMembers,
                                           final Resource video,
                                           final Resource trailer,
                                           final Resource banner,
                                           final Resource thumbnail,
                                           final Resource thumbnailHalf){
        return new CreateVideoCommand(
                title,
                description,
                launchedAt,
                duration,
                opened,
                published,
                rating,
                categories,
                genres,
                castMembers,
                video,
                trailer,
                banner,
                thumbnail,
                thumbnailHalf
        );
    }

    Optional<Resource> getVideo(){
        return Optional.ofNullable(video);
    }
    Optional<Resource> getTrailer(){
        return Optional.ofNullable(trailer);
    }
    Optional<Resource> getBanner(){
        return Optional.ofNullable(banner);
    }
    Optional<Resource> getThumbnailHalf(){
        return Optional.ofNullable(thumbnailHalf);
    }
    Optional<Resource> getThumbnail(){
        return Optional.ofNullable(thumbnail);
    }




}
