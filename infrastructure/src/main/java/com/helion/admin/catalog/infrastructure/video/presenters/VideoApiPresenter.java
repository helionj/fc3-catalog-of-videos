package com.helion.admin.catalog.infrastructure.video.presenters;

import com.helion.admin.catalog.application.video.media.upload.UploadMediaOutput;
import com.helion.admin.catalog.application.video.retrieve.get.VideoOutput;
import com.helion.admin.catalog.application.video.retrieve.list.VideoListOutput;
import com.helion.admin.catalog.application.video.update.UpdateVideoOutput;
import com.helion.admin.catalog.domain.category.pagination.Pagination;
import com.helion.admin.catalog.domain.video.AudioVideoMedia;
import com.helion.admin.catalog.domain.video.ImageMedia;
import com.helion.admin.catalog.infrastructure.video.models.*;

public interface VideoApiPresenter {

   public static VideoResponse presenter(VideoOutput output){
        return new VideoResponse(
                output.id(),
                output.title(),
                output.description(),
                output.launchedAt(),
                output.duration(),
                output.opened(),
                output.published(),
                output.rating(),
                output.createdAt(),
                output.updatedAt(),
                present(output.video()),
                present(output.trailer()),
                present(output.banner()),
                present(output.thumbNail()),
                present(output.thumbNailHalf()),
                output.categories(),
                output.genres(),
                output.castMembers()
        );
    }

    static ImageMediaResponse present(final ImageMedia media){
       if(media != null){
           return new ImageMediaResponse(
                   media.id(),
                   media.checksum(),
                   media.name(),
                   media.location()
           );
       }
       return null;
    }

    static AudioVideoMediaResponse present(final AudioVideoMedia media){
       if(media != null) {
           return new AudioVideoMediaResponse(
                   media.id(),
                   media.checksum(),
                   media.name(),
                   media.rawLocation(),
                   media.encodedLocation(),
                   media.status().name()
           );
       }
       return null;
    }

    static UpdateVideoResponse present(final UpdateVideoOutput output) {
       return new UpdateVideoResponse(output.id());
    }

    static VideoListResponse present(final VideoListOutput output){
       return new VideoListResponse(
               output.id(),
               output.title(),
               output.description(),
               output.createdAt(),
               output.updatedAt()
       );
    }
    static Pagination<VideoListResponse> present(Pagination<VideoListOutput> page) {
       return page.map(VideoApiPresenter::present);
    }

    static UploadMediaResponse present(final UploadMediaOutput output) {
       return new UploadMediaResponse(output.videoId(), output.mediaType());
    }
}
