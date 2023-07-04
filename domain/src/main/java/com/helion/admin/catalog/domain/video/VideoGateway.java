package com.helion.admin.catalog.domain.video;

import com.helion.admin.catalog.domain.category.pagination.Pagination;
import com.helion.admin.catalog.domain.category.pagination.SearchQuery;

import java.util.Optional;

public interface VideoGateway {

    Video create(Video aVideo);

    void deleteById(VideoID anId);

    Video update(Video aVideo);

    Optional<Video> findById(VideoID anId);

    Pagination<VideoPreview> findAll(VideoSearchQuery aQuery);



}
