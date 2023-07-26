package com.helion.admin.catalog.application.video.retrieve.get;

import com.helion.admin.catalog.application.UseCaseTest;
import com.helion.admin.catalog.domain.Fixture;
import com.helion.admin.catalog.domain.castmember.CastMemberID;
import com.helion.admin.catalog.domain.category.CategoryID;
import com.helion.admin.catalog.domain.exceptions.NotFoundException;
import com.helion.admin.catalog.domain.genre.GenreID;
import com.helion.admin.catalog.domain.utils.CollectionUtils;
import com.helion.admin.catalog.domain.utils.IdUtils;
import com.helion.admin.catalog.domain.video.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.Set;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GetVideoByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultGetVideoByIdUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(videoGateway);
    }

    @Test
    public void givenAnValidId_whenCallsGetVideoById_shouldReturnsAVideo() {

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(Fixture.Categories.aulas().getId());
        final var expectedGenres = Set.of(Fixture.Genres.tech().getId());
        final var expectedMembers = Set.of(
                Fixture.CastMembers.wesley().getId(),
                Fixture.CastMembers.helion().getId()
        );

        final var expectedVideo = audioVideoMedia(VideoMediaType.VIDEO);
        final var expectedTrailer = audioVideoMedia(VideoMediaType.TRAILER);
        final var expectedBanner = imageMedia(VideoMediaType.BANNER);
        final var expectedThumb = imageMedia(VideoMediaType.THUMBNAIL);
        final var expectedThumbHalf = imageMedia(VideoMediaType.THUMBNAIL_HALF);


        final var aVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        aVideo.updateVideoMedia(expectedVideo);
        aVideo.updateTrailerMedia(expectedTrailer);
        aVideo.updateBannerMedia(expectedBanner);
        aVideo.updateThumbnailMedia(expectedThumb);
        aVideo.updateThumbnailHalfMedia(expectedThumbHalf);

        final var expectedId = aVideo.getId();

        when(videoGateway.findById(aVideo.getId()))
                .thenReturn(Optional.of(Video.with(aVideo)));

        final var actualVideo = useCase.execute(expectedId.getValue());

        Assertions.assertEquals(expectedId.getValue(), actualVideo.id());
        Assertions.assertEquals(aVideo.getCreatedAt(), actualVideo.createdAt());
        Assertions.assertEquals(aVideo.getUpdatedAt(), actualVideo.updatedAt());
        Assertions.assertEquals(expectedTitle, actualVideo.title());
        Assertions.assertEquals(expectedDescription, actualVideo.description());
        Assertions.assertEquals(expectedLaunchedAt.getValue(), actualVideo.launchedAt());
        Assertions.assertEquals(expectedDuration, actualVideo.duration());
        Assertions.assertEquals(expectedOpened, actualVideo.opened());
        Assertions.assertEquals(expectedPublished, actualVideo.published());
        Assertions.assertEquals(expectedRating.getName(), actualVideo.rating());
        Assertions.assertEquals(CollectionUtils.mapTo(expectedCategories, CategoryID::getValue), actualVideo.categories());
        Assertions.assertEquals(CollectionUtils.mapTo(expectedGenres, GenreID::getValue), actualVideo.genres());
        Assertions.assertEquals(CollectionUtils.mapTo(expectedMembers, CastMemberID::getValue), actualVideo.castMembers());
        Assertions.assertEquals(expectedVideo, actualVideo.video());
        Assertions.assertEquals(expectedTrailer, actualVideo.trailer());
        Assertions.assertEquals(expectedBanner, actualVideo.banner());
        Assertions.assertEquals(expectedThumb, actualVideo.thumbNail());
        Assertions.assertEquals(expectedThumbHalf, actualVideo.thumbNailHalf());


        verify(videoGateway).findById(eq(expectedId));
    }

    @Test
    public void givenAnInvalidId_whenCallsGetVideoById_shouldReturnNotFoundException(){

        final var expectedErrorMessage = "Video with ID 123 was not found";
        final var expectedId = VideoID.from("123");

        when(videoGateway.findById(any()))
                .thenReturn(Optional.empty());
        final var actualError =
                Assertions.assertThrows(NotFoundException.class,
                () -> this.useCase.execute(expectedId.getValue()));
        Assertions.assertEquals(expectedErrorMessage, actualError.getMessage());
    }

    private AudioVideoMedia audioVideoMedia(VideoMediaType type) {
        final var checkSum = IdUtils.uuid();
        return AudioVideoMedia.with(
                checkSum,
                type.name().toLowerCase(),
                "/videos/" + checkSum,
                "",
                MediaStatus.PENDING);
    }

    private ImageMedia imageMedia(VideoMediaType type) {
        final var checkSum = IdUtils.uuid();
        return ImageMedia.with(
                checkSum,
                type.name().toLowerCase(),
                "/images/" + checkSum);
    }
}



