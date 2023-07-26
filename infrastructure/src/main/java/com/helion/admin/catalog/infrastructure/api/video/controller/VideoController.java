package com.helion.admin.catalog.infrastructure.api.video.controller;

import com.helion.admin.catalog.application.video.create.CreateVideoCommand;
import com.helion.admin.catalog.application.video.create.CreateVideoUseCase;
import com.helion.admin.catalog.application.video.delete.DeleteVideoUseCase;
import com.helion.admin.catalog.application.video.media.get.GetMediaCommand;
import com.helion.admin.catalog.application.video.media.get.GetMediaUseCase;
import com.helion.admin.catalog.application.video.media.upload.UploadMediaCommand;
import com.helion.admin.catalog.application.video.media.upload.UploadMediaUseCase;
import com.helion.admin.catalog.application.video.retrieve.get.GetVideoByIdUseCase;
import com.helion.admin.catalog.application.video.retrieve.list.ListVideosUseCase;
import com.helion.admin.catalog.application.video.update.UpdateVideoCommand;
import com.helion.admin.catalog.application.video.update.UpdateVideoUseCase;
import com.helion.admin.catalog.domain.castmember.CastMemberID;
import com.helion.admin.catalog.domain.category.CategoryID;
import com.helion.admin.catalog.domain.category.pagination.Pagination;
import com.helion.admin.catalog.domain.exceptions.NotificationException;
import com.helion.admin.catalog.domain.genre.GenreID;
import com.helion.admin.catalog.domain.resource.Resource;
import com.helion.admin.catalog.domain.validation.Error;
import com.helion.admin.catalog.domain.video.VideoMediaType;
import com.helion.admin.catalog.domain.video.VideoResource;
import com.helion.admin.catalog.domain.video.VideoSearchQuery;
import com.helion.admin.catalog.infrastructure.api.video.VideoAPI;
import com.helion.admin.catalog.infrastructure.utils.HashingUtils;
import com.helion.admin.catalog.infrastructure.video.models.CreateVideoRequest;
import com.helion.admin.catalog.infrastructure.video.models.UpdateVideoRequest;
import com.helion.admin.catalog.infrastructure.video.models.VideoListResponse;
import com.helion.admin.catalog.infrastructure.video.models.VideoResponse;
import com.helion.admin.catalog.infrastructure.video.presenters.VideoApiPresenter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.Objects;
import java.util.Set;

import static com.helion.admin.catalog.domain.utils.CollectionUtils.*;

@RestController
public class VideoController implements VideoAPI {

    private final CreateVideoUseCase createVideoUseCase;
    private final GetVideoByIdUseCase getVideoByIdUseCase;

    private final UpdateVideoUseCase updateVideoUseCase;

    private final DeleteVideoUseCase deleteVideoUseCase;

    private final ListVideosUseCase listVideosUseCase;

    private final GetMediaUseCase getMediaUseCase;

    private final UploadMediaUseCase uploadMediaUseCase;


    public VideoController(
            final CreateVideoUseCase useCase,
            final GetVideoByIdUseCase getVideoByIdUseCase,
            final UpdateVideoUseCase updateVideoUseCase,
            final DeleteVideoUseCase deleteVideoUseCase,
            final ListVideosUseCase listVideosUseCase,
            final GetMediaUseCase getMediaUseCase,
            final UploadMediaUseCase uploadMediaUseCase) {
        this.createVideoUseCase = Objects.requireNonNull(useCase);
        this.getVideoByIdUseCase = Objects.requireNonNull(getVideoByIdUseCase);
        this.updateVideoUseCase = Objects.requireNonNull(updateVideoUseCase);
        this.deleteVideoUseCase = Objects.requireNonNull(deleteVideoUseCase);
        this.listVideosUseCase = Objects.requireNonNull(listVideosUseCase);
        this.getMediaUseCase = Objects.requireNonNull(getMediaUseCase);
        this.uploadMediaUseCase = Objects.requireNonNull(uploadMediaUseCase);
    }

    @Override
    public Pagination<VideoListResponse> list(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String dir,
            final Set<String> categories,
            final Set<String> genres,
            final Set<String> castMembers) {

        final var aQuery = new VideoSearchQuery(
                page,perPage,search,sort,dir, mapTo(castMembers, CastMemberID::from), mapTo(categories, CategoryID::from), mapTo(genres, GenreID::from));

        return VideoApiPresenter.present(this.listVideosUseCase.execute(aQuery));
    }

    @Override
    public ResponseEntity<?> createFull(
            final String title,
            final String description,
            final Integer yearLaunched,
            final Double duration,
            final Boolean opened,
            final Boolean published,
            final String rating,
            final Set<String> categories,
            final Set<String> castMembers,
            final Set<String> genres,
            final MultipartFile videoFile,
            final MultipartFile trailerFile,
            final MultipartFile bannerFile,
            final MultipartFile thumbFile,
            final MultipartFile thumbHalfFile) {

        final var aCmd = CreateVideoCommand
                .with(
                        title,
                        description,
                        yearLaunched,
                        duration,
                        opened,
                        published,
                        rating,
                        categories,
                        genres,
                        castMembers,
                        resourceOf(videoFile),
                        resourceOf(trailerFile),
                        resourceOf(bannerFile),
                        resourceOf(thumbFile),
                        resourceOf(thumbHalfFile)
                );
        final var output = this.createVideoUseCase.execute(aCmd);

        return ResponseEntity.created(URI.create("/videos/"+ output.id())).body(output);
    }

    @Override
    public ResponseEntity<?> createPartial(final CreateVideoRequest payload) {
        final var aCmd = CreateVideoCommand
                .with(
                        payload.title(),
                        payload.description(),
                        payload.yaerLaunched(),
                        payload.duration(),
                        payload.opened(),
                        payload.published(),
                        payload.rating(),
                        payload.categories(),
                        payload.genres(),
                        payload.castMembers()
                );
        final var output = this.createVideoUseCase.execute(aCmd);

        return ResponseEntity.created(URI.create("/videos/"+ output.id())).body(output);
    }

    @Override
    public VideoResponse getById(String id) {
        return VideoApiPresenter.presenter(this.getVideoByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> update(final String id, final UpdateVideoRequest payload) {
        final var aCmd = UpdateVideoCommand
                .with(
                        id,
                        payload.title(),
                        payload.description(),
                        payload.yaerLaunched(),
                        payload.duration(),
                        payload.opened(),
                        payload.published(),
                        payload.rating(),
                        payload.categories(),
                        payload.genres(),
                        payload.castMembers()
                );
        final var output = this.updateVideoUseCase.execute(aCmd);

        return ResponseEntity
                .ok()
                .location(URI.create("/videos/"+output.id()))
                .body(VideoApiPresenter.present(output));
    }

    @Override
    public void deleteById(String id) {
        this.deleteVideoUseCase.execute(id);
    }

    @Override
    public ResponseEntity<byte[]> getMediaByType(final String id, final String type) {
        final var aMedia = this.getMediaUseCase.execute(GetMediaCommand.with(id, type));
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(aMedia.contentType()))
                .contentLength(aMedia.content().length)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=%s".formatted(aMedia.name()))
                .body(aMedia.content());
    }

    @Override
    public ResponseEntity<?> uploadMediaByType(final String id, final String type, final MultipartFile media) {
        final var aType =
                VideoMediaType.of(type).orElseThrow(() -> NotificationException.with(new Error("Invalid %s for VideoMediaType".formatted(type))));
        final var aCmd = UploadMediaCommand.with(id, VideoResource.with(resourceOf(media), aType));
        final var output = this.uploadMediaUseCase.execute(aCmd);
        return ResponseEntity.created(URI.create("/videos/%s/medias/%s".formatted(id,type)))
                .body(VideoApiPresenter.present(output));
    }

    private Resource resourceOf(MultipartFile part){
        if (part == null) return null;
        try{

            return Resource.with(
                    HashingUtils.checksum(part.getBytes()),
                    part.getBytes(),
                    part.getContentType(),
                    part.getOriginalFilename());
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
