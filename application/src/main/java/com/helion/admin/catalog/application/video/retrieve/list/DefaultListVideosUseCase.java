package com.helion.admin.catalog.application.video.retrieve.list;

import com.helion.admin.catalog.domain.category.pagination.Pagination;
import com.helion.admin.catalog.domain.video.VideoGateway;
import com.helion.admin.catalog.domain.video.VideoSearchQuery;

import java.util.Objects;

public class DefaultListVideosUseCase extends ListVideosUseCase{

    private final VideoGateway videoGateway;

    public DefaultListVideosUseCase(VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public Pagination<VideoListOutput> execute(VideoSearchQuery aQuery) {
        return this.videoGateway.findAll(aQuery).map(VideoListOutput::from);
    }
}
