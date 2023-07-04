package com.helion.admin.catalog.application.video.retrieve.list;

import com.helion.admin.catalog.application.UseCase;
import com.helion.admin.catalog.domain.category.pagination.Pagination;
import com.helion.admin.catalog.domain.video.VideoSearchQuery;

public abstract class ListVideosUseCase extends UseCase<VideoSearchQuery, Pagination<VideoListOutput>> {
}
