package com.helion.catalog.domain.video;

import com.helion.catalog.domain.pagination.Pagination;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface VideoGateway {

    Video save(Video video);

    void deleteById(String videoId);

    Optional<Video> findById(String videoId);

    Pagination<Video> findAll(VideoSearchQuery query);

    List<Video> findAllById(Set<String> ids);
}
