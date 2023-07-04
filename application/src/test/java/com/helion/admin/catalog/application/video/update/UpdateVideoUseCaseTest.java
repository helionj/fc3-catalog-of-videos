package com.helion.admin.catalog.application.video.update;

import com.helion.admin.catalog.application.UseCaseTest;
import com.helion.admin.catalog.domain.Fixture;
import com.helion.admin.catalog.domain.castmember.CastMemberGateway;
import com.helion.admin.catalog.domain.castmember.CastMemberID;
import com.helion.admin.catalog.domain.category.CategoryGateway;
import com.helion.admin.catalog.domain.category.CategoryID;
import com.helion.admin.catalog.domain.exceptions.DomainException;
import com.helion.admin.catalog.domain.exceptions.InternalErrorException;
import com.helion.admin.catalog.domain.exceptions.NotificationException;
import com.helion.admin.catalog.domain.genre.GenreGateway;
import com.helion.admin.catalog.domain.genre.GenreID;
import com.helion.admin.catalog.domain.resource.Resource;
import com.helion.admin.catalog.domain.utils.IdUtils;
import com.helion.admin.catalog.domain.video.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.Year;
import java.util.*;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class UpdateVideoUseCaseTest extends UseCaseTest{

    @InjectMocks
    private DefaultUpdateVideoUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Mock
    private CategoryGateway categoryGateway;

    @Mock
    private GenreGateway genreGateway;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Mock
    private MediaResourceGateway mediaResourceGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(videoGateway, categoryGateway, genreGateway, castMemberGateway, mediaResourceGateway);
    }

    @Test
    public void givenAnValidCommand_whenCallsUpdateVideo_shouldReturnVideoID(){

        final var aVideo = Fixture.Videos.systemDesign();
        final var expectedId = aVideo.getId().getValue();
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
        final Resource expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final Resource expectedTrailer =  Fixture.Videos.resource(VideoMediaType.TRAILER);
        final Resource expectedBanner =  Fixture.Videos.resource(VideoMediaType.BANNER);
        final Resource expectedThumb =  Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final Resource expectedThumbHalf = Fixture.Videos.resource(VideoMediaType.THUMBNAIL_HALF);

        final var aCommand = UpdateVideoCommand.with(
                expectedId,
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(Video.with(aVideo)));
        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));
        when(genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedGenres));
        when(castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedMembers));
        when(videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());
        mockImageMedia();
        mockAudioVideoMedia();

        final var actualResult = useCase.execute(aCommand);

        Assertions.assertNotNull(actualResult);
        Assertions.assertNotNull(actualResult.id());

        verify(videoGateway).update(argThat(actualVideo ->
                Objects.equals(expectedTitle,actualVideo.getTitle())
                        && Objects.equals(expectedDescription,actualVideo.getDescription())
                        && Objects.equals(expectedLaunchedAt,actualVideo.getLaunchedAt())
                        && Objects.equals(expectedDuration,actualVideo.getDuration())
                        && Objects.equals(expectedOpened,actualVideo.isOpened())
                        && Objects.equals(expectedPublished,actualVideo.isPublished())
                        && Objects.equals(expectedRating,actualVideo.getRating())
                        && Objects.equals(expectedCategories,actualVideo.getCategories())
                        && Objects.equals(expectedGenres,actualVideo.getGenres())
                        && Objects.equals(expectedMembers,actualVideo.getCastMembers())
                        && Objects.equals(expectedVideo.name(), actualVideo.getVideo().get().name())
                        && Objects.equals(expectedTrailer.name(), actualVideo.getTrailer().get().name())
                        && Objects.equals(expectedBanner.name(), actualVideo.getBanner().get().name())
                        && Objects.equals(expectedThumb.name(), actualVideo.getThumbnail().get().name())
                        && Objects.equals(expectedThumbHalf.name(), actualVideo.getThumbnailHalf().get().name())
                        && Objects.equals(aVideo.getCreatedAt(), actualVideo.getCreatedAt())
                        && aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt())
        ));
    }
    @Test
    public void givenAnValidCommandWithoutCategories_whenCallsUpdateVideo_shouldReturnVideoID(){

        final var aVideo = Fixture.Videos.systemDesign();
        final var expectedId = aVideo.getId().getValue();
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.of(Fixture.Genres.tech().getId());
        final var expectedMembers = Set.of(
                Fixture.CastMembers.wesley().getId(),
                Fixture.CastMembers.helion().getId()
        );
        final Resource expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final Resource expectedTrailer =  Fixture.Videos.resource(VideoMediaType.TRAILER);
        final Resource expectedBanner =  Fixture.Videos.resource(VideoMediaType.BANNER);
        final Resource expectedThumb =  Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final Resource expectedThumbHalf = Fixture.Videos.resource(VideoMediaType.THUMBNAIL_HALF);

        final var aCommand = UpdateVideoCommand.with(
                expectedId,
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );
        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(Video.with(aVideo)));
        when(genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedGenres));
        when(castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedMembers));
        when(videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());
        mockImageMedia();
        mockAudioVideoMedia();

        final var actualResult = useCase.execute(aCommand);

        Assertions.assertNotNull(actualResult);
        Assertions.assertNotNull(actualResult.id());
        verify(videoGateway).findById(eq(aVideo.getId()));

        verify(videoGateway).update(argThat(actualVideo ->
                Objects.equals(expectedTitle,actualVideo.getTitle())
                        && Objects.equals(expectedDescription,actualVideo.getDescription())
                        && Objects.equals(expectedLaunchedAt,actualVideo.getLaunchedAt())
                        && Objects.equals(expectedDuration,actualVideo.getDuration())
                        && Objects.equals(expectedOpened,actualVideo.isOpened())
                        && Objects.equals(expectedPublished,actualVideo.isPublished())
                        && Objects.equals(expectedRating,actualVideo.getRating())
                        && Objects.equals(expectedCategories,actualVideo.getCategories())
                        && Objects.equals(expectedGenres,actualVideo.getGenres())
                        && Objects.equals(expectedMembers,actualVideo.getCastMembers())
                        && Objects.equals(expectedVideo.name(), actualVideo.getVideo().get().name())
                        && Objects.equals(expectedTrailer.name(), actualVideo.getTrailer().get().name())
                        && Objects.equals(expectedBanner.name(), actualVideo.getBanner().get().name())
                        && Objects.equals(expectedThumb.name(), actualVideo.getThumbnail().get().name())
                        && Objects.equals(expectedThumbHalf.name(), actualVideo.getThumbnailHalf().get().name())
                        && Objects.equals(aVideo.getCreatedAt(), actualVideo.getCreatedAt())
                        && aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt())
        ));
    }
    @Test
    public void givenAnValidCommandWithoutGenres_whenCallsUpdateVideo_shouldReturnVideoID(){

        final var aVideo = Fixture.Videos.systemDesign();
        final var expectedId = aVideo.getId().getValue();
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(Fixture.Categories.aulas().getId());
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.of(
                Fixture.CastMembers.wesley().getId(),
                Fixture.CastMembers.helion().getId()
        );
        final Resource expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final Resource expectedTrailer =  Fixture.Videos.resource(VideoMediaType.TRAILER);
        final Resource expectedBanner =  Fixture.Videos.resource(VideoMediaType.BANNER);
        final Resource expectedThumb =  Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final Resource expectedThumbHalf = Fixture.Videos.resource(VideoMediaType.THUMBNAIL_HALF);

        final var aCommand = UpdateVideoCommand.with(
                expectedId,
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );
        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(Video.with(aVideo)));
        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));
        when(castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedMembers));
        when(videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());
        mockImageMedia();
        mockAudioVideoMedia();

        final var actualResult = useCase.execute(aCommand);

        Assertions.assertNotNull(actualResult);
        Assertions.assertNotNull(actualResult.id());

        verify(videoGateway).findById(eq(aVideo.getId()));

        verify(videoGateway).update(argThat(actualVideo ->
                Objects.equals(expectedTitle,actualVideo.getTitle())
                        && Objects.equals(expectedDescription,actualVideo.getDescription())
                        && Objects.equals(expectedLaunchedAt,actualVideo.getLaunchedAt())
                        && Objects.equals(expectedDuration,actualVideo.getDuration())
                        && Objects.equals(expectedOpened,actualVideo.isOpened())
                        && Objects.equals(expectedPublished,actualVideo.isPublished())
                        && Objects.equals(expectedRating,actualVideo.getRating())
                        && Objects.equals(expectedCategories,actualVideo.getCategories())
                        && Objects.equals(expectedGenres,actualVideo.getGenres())
                        && Objects.equals(expectedMembers,actualVideo.getCastMembers())
                        && Objects.equals(expectedVideo.name(), actualVideo.getVideo().get().name())
                        && Objects.equals(expectedTrailer.name(), actualVideo.getTrailer().get().name())
                        && Objects.equals(expectedBanner.name(), actualVideo.getBanner().get().name())
                        && Objects.equals(expectedThumb.name(), actualVideo.getThumbnail().get().name())
                        && Objects.equals(expectedThumbHalf.name(), actualVideo.getThumbnailHalf().get().name())
                        && Objects.equals(aVideo.getCreatedAt(), actualVideo.getCreatedAt())
                        && aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt())
        ));
    }
    @Test
    public void givenAnValidCommandWithoutCastMembers_whenCallsUpdateVideo_shouldReturnVideoID(){
        final var aVideo = Fixture.Videos.systemDesign();
        final var expectedId = aVideo.getId().getValue();
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(Fixture.Categories.aulas().getId());
        final var expectedGenres = Set.of(Fixture.Genres.tech().getId());
        final var expectedMembers = Set.<CastMemberID>of();

        final Resource expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final Resource expectedTrailer =  Fixture.Videos.resource(VideoMediaType.TRAILER);
        final Resource expectedBanner =  Fixture.Videos.resource(VideoMediaType.BANNER);
        final Resource expectedThumb =  Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final Resource expectedThumbHalf = Fixture.Videos.resource(VideoMediaType.THUMBNAIL_HALF);

        final var aCommand = UpdateVideoCommand.with(
                expectedId,
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );
        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(Video.with(aVideo)));
        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));
        when(genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedGenres));
        when(videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());
        mockImageMedia();
        mockAudioVideoMedia();

        final var actualResult = useCase.execute(aCommand);

        Assertions.assertNotNull(actualResult);
        Assertions.assertNotNull(actualResult.id());

        verify(videoGateway).findById(eq(aVideo.getId()));

        verify(videoGateway).update(argThat(actualVideo ->
                Objects.equals(expectedTitle,actualVideo.getTitle())
                        && Objects.equals(expectedDescription,actualVideo.getDescription())
                        && Objects.equals(expectedLaunchedAt,actualVideo.getLaunchedAt())
                        && Objects.equals(expectedDuration,actualVideo.getDuration())
                        && Objects.equals(expectedOpened,actualVideo.isOpened())
                        && Objects.equals(expectedPublished,actualVideo.isPublished())
                        && Objects.equals(expectedRating,actualVideo.getRating())
                        && Objects.equals(expectedCategories,actualVideo.getCategories())
                        && Objects.equals(expectedGenres,actualVideo.getGenres())
                        && Objects.equals(expectedMembers,actualVideo.getCastMembers())
                        && Objects.equals(expectedVideo.name(), actualVideo.getVideo().get().name())
                        && Objects.equals(expectedTrailer.name(), actualVideo.getTrailer().get().name())
                        && Objects.equals(expectedBanner.name(), actualVideo.getBanner().get().name())
                        && Objects.equals(expectedThumb.name(), actualVideo.getThumbnail().get().name())
                        && Objects.equals(expectedThumbHalf.name(), actualVideo.getThumbnailHalf().get().name())
                        && Objects.equals(aVideo.getCreatedAt(), actualVideo.getCreatedAt())
                        && aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt())
        ));
    }
    @Test
    public void givenAnValidCommandWithoutResources_whenCallsUpdateVideo_shouldReturnVideoID(){

        final var aVideo = Fixture.Videos.systemDesign();
        final var expectedId = aVideo.getId().getValue();
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
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner =  null;
        final Resource expectedThumb =  null;
        final Resource expectedThumbHalf = null;

        final var aCommand = UpdateVideoCommand.with(
                expectedId,
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(Video.with(aVideo)));
        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));
        when(genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedGenres));
        when(castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedMembers));
        when(videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());


        final var actualResult = useCase.execute(aCommand);

        Assertions.assertNotNull(actualResult);
        Assertions.assertNotNull(actualResult.id());

        verify(videoGateway).findById(eq(aVideo.getId()));

        verify(videoGateway).update(argThat(actualVideo ->
                Objects.equals(expectedTitle,actualVideo.getTitle())
                        && Objects.equals(expectedDescription,actualVideo.getDescription())
                        && Objects.equals(expectedLaunchedAt,actualVideo.getLaunchedAt())
                        && Objects.equals(expectedDuration,actualVideo.getDuration())
                        && Objects.equals(expectedOpened,actualVideo.isOpened())
                        && Objects.equals(expectedPublished,actualVideo.isPublished())
                        && Objects.equals(expectedRating,actualVideo.getRating())
                        && Objects.equals(expectedCategories,actualVideo.getCategories())
                        && Objects.equals(expectedGenres,actualVideo.getGenres())
                        && Objects.equals(expectedMembers,actualVideo.getCastMembers())
                        && actualVideo.getVideo().isEmpty()
                        && actualVideo.getTrailer().isEmpty()
                        && actualVideo.getBanner().isEmpty()
                        && actualVideo.getThumbnail().isEmpty()
                        && actualVideo.getThumbnailHalf().isEmpty()
                        && Objects.equals(aVideo.getCreatedAt(), actualVideo.getCreatedAt())
                        && aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt())
        ));
    }
    @Test
    public void givenANullTitle_whenCallsUpdateVideo_shouldReturnDomainException(){
        final var aVideo = Fixture.Videos.systemDesign();
        final var expectedId = aVideo.getId().getValue();
        final var expectedErrorMessage = "'title' should not be null";
        final var expectedErrorCount = 1;
        final String expectedTitle = null;
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();

        final Resource expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final Resource expectedTrailer =  Fixture.Videos.resource(VideoMediaType.TRAILER);
        final Resource expectedBanner =  Fixture.Videos.resource(VideoMediaType.BANNER);
        final Resource expectedThumb =  Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final Resource expectedThumbHalf = Fixture.Videos.resource(VideoMediaType.THUMBNAIL_HALF);

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(Video.with(aVideo)));

        final var aCommand = UpdateVideoCommand.with(
                expectedId,
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );


        final var actualException = Assertions.assertThrows(DomainException.class, () -> {
            useCase.execute(aCommand);
        });

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(videoGateway, times(0)).update(any());

    }

    @Test
    public void givenAnEmptyTitle_whenCallsUpdateVideo_shouldReturnDomainException(){
        final var aVideo = Fixture.Videos.systemDesign();
        final var expectedId = aVideo.getId().getValue();
        final var expectedErrorMessage = "'title' should not be empty";
        final var expectedErrorCount = 1;
        final String expectedTitle = "";
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();

        final Resource expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final Resource expectedTrailer =  Fixture.Videos.resource(VideoMediaType.TRAILER);
        final Resource expectedBanner =  Fixture.Videos.resource(VideoMediaType.BANNER);
        final Resource expectedThumb =  Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final Resource expectedThumbHalf = Fixture.Videos.resource(VideoMediaType.THUMBNAIL_HALF);

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(Video.with(aVideo)));

        final var aCommand = UpdateVideoCommand.with(
                expectedId,
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );


        final var actualException = Assertions.assertThrows(DomainException.class, () -> {
            useCase.execute(aCommand);
        });

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(videoGateway, times(0)).update(any());
    }
    @Test
    public void givenANullRating_whenCallsUpdateVideo_shouldReturnDomainException(){
        final var aVideo = Fixture.Videos.systemDesign();
        final var expectedId = aVideo.getId().getValue();
        final var expectedErrorMessage = "'rating' should not be null";
        final var expectedErrorCount = 1;
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final String expectedRating = null;
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();

        final Resource expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final Resource expectedTrailer =  Fixture.Videos.resource(VideoMediaType.TRAILER);
        final Resource expectedBanner =  Fixture.Videos.resource(VideoMediaType.BANNER);
        final Resource expectedThumb =  Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final Resource expectedThumbHalf = Fixture.Videos.resource(VideoMediaType.THUMBNAIL_HALF);

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(Video.with(aVideo)));

        final var aCommand = UpdateVideoCommand.with(
                expectedId,
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );


        final var actualException = Assertions.assertThrows(DomainException.class, () -> {
            useCase.execute(aCommand);
        });

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(videoGateway, times(0)).update(any());

    }

    @Test
    public void givenAnInvalidRating_whenCallsUpdateVideo_shouldReturnDomainException(){
        final var aVideo = Fixture.Videos.systemDesign();
        final var expectedId = aVideo.getId().getValue();
        final var expectedErrorMessage = "'rating' should not be null";
        final var expectedErrorCount = 1;
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = "INVALIDRATING";
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();

        final Resource expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final Resource expectedTrailer =  Fixture.Videos.resource(VideoMediaType.TRAILER);
        final Resource expectedBanner =  Fixture.Videos.resource(VideoMediaType.BANNER);
        final Resource expectedThumb =  Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final Resource expectedThumbHalf = Fixture.Videos.resource(VideoMediaType.THUMBNAIL_HALF);

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(Video.with(aVideo)));

        final var aCommand = UpdateVideoCommand.with(
                expectedId,
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );


        final var actualException = Assertions.assertThrows(DomainException.class, () -> {
            useCase.execute(aCommand);
        });

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(videoGateway, times(0)).update(any());

    }

    @Test
    public void givenANullLanchedAt_whenCallsUpdateVideo_shouldReturnDomainException(){
        final var aVideo = Fixture.Videos.systemDesign();
        final var expectedId = aVideo.getId().getValue();
        final var expectedErrorMessage = "'launchedAt' should not be null";
        final var expectedErrorCount = 1;
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final Integer expectedLaunchedAt = null;
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();

        final Resource expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final Resource expectedTrailer =  Fixture.Videos.resource(VideoMediaType.TRAILER);
        final Resource expectedBanner =  Fixture.Videos.resource(VideoMediaType.BANNER);
        final Resource expectedThumb =  Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final Resource expectedThumbHalf = Fixture.Videos.resource(VideoMediaType.THUMBNAIL_HALF);

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(Video.with(aVideo)));

        final var aCommand = UpdateVideoCommand.with(
                expectedId,
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );


        final var actualException = Assertions.assertThrows(DomainException.class, () -> {
            useCase.execute(aCommand);
        });

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(videoGateway, times(0)).update(any());

    }

    @Test
    public void givenAnValidCommandAndSomeCategoriesDoesNotExists_whenCallsUpdateVideo_shouldReturnDomainException(){

        final var aVideo = Fixture.Videos.systemDesign();
        final var expectedId = aVideo.getId().getValue();
        final var expectedErrorCount = 1;
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var aCategory = Fixture.Categories.aulas().getId();
        final var expectedCategories = Set.of(aCategory);
        final var expectedGenres = Set.of(Fixture.Genres.tech().getId());
        final var expectedMembers = Set.of(
                Fixture.CastMembers.wesley().getId(),
                Fixture.CastMembers.helion().getId()
        );
        final var expectedErrorMessage = "Some categories could not be found: %s".formatted(aCategory.getValue());

        final Resource expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final Resource expectedTrailer =  Fixture.Videos.resource(VideoMediaType.TRAILER);
        final Resource expectedBanner =  Fixture.Videos.resource(VideoMediaType.BANNER);
        final Resource expectedThumb =  Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final Resource expectedThumbHalf = Fixture.Videos.resource(VideoMediaType.THUMBNAIL_HALF);

        final var aCommand = UpdateVideoCommand.with(
                expectedId,
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(Video.with(aVideo)));
        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>());
        when(genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedGenres));
        when(castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedMembers));




        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            useCase.execute(aCommand);
        });

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        verify(categoryGateway, times(1)).existsByIds(any());
        verify(genreGateway, times(1)).existsByIds(any());
        verify(castMemberGateway, times(1)).existsByIds(any());
        verify(videoGateway, times(0)).update(any());
    }
    @Test
    public void givenAnValidCommandAndSomeGenresDoesNotExists_whenCallsUpdateVideo_shouldReturnDomainException(){

        final var aVideo = Fixture.Videos.systemDesign();
        final var expectedId = aVideo.getId().getValue();
        final var expectedErrorCount = 1;
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var aCategory = Fixture.Categories.aulas().getId();
        final var aGenre = Fixture.Genres.tech().getId();
        final var expectedCategories = Set.of(aCategory);
        final var expectedGenres = Set.of(aGenre);
        final var expectedMembers = Set.of(
                Fixture.CastMembers.wesley().getId(),
                Fixture.CastMembers.helion().getId()
        );
        final var expectedErrorMessage = "Some genres could not be found: %s".formatted(aGenre.getValue());

        final Resource expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final Resource expectedTrailer =  Fixture.Videos.resource(VideoMediaType.TRAILER);
        final Resource expectedBanner =  Fixture.Videos.resource(VideoMediaType.BANNER);
        final Resource expectedThumb =  Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final Resource expectedThumbHalf = Fixture.Videos.resource(VideoMediaType.THUMBNAIL_HALF);

        final var aCommand = UpdateVideoCommand.with(
                expectedId,
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );
        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(Video.with(aVideo)));
        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));
        when(genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>());
        when(castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedMembers));




        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            useCase.execute(aCommand);
        });

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        verify(categoryGateway, times(1)).existsByIds(any());
        verify(genreGateway, times(1)).existsByIds(any());
        verify(castMemberGateway, times(1)).existsByIds(any());
        verify(videoGateway, times(0)).update(any());
    }

    @Test
    public void givenAnValidCommandAndSomeCastMembersDoesNotExists_whenCallsUpdateVideo_shouldReturnDomainException(){

        final var aVideo = Fixture.Videos.systemDesign();
        final var expectedId = aVideo.getId().getValue();
        final var expectedErrorCount = 1;
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var aCategory = Fixture.Categories.aulas().getId();
        final var aGenre = Fixture.Genres.tech().getId();
        final var aMember = Fixture.CastMembers.wesley().getId();
        final var expectedCategories = Set.of(aCategory);
        final var expectedGenres = Set.of(aGenre);
        final var expectedMembers = Set.of(
                aMember,
                Fixture.CastMembers.helion().getId()
        );
        final var expectedErrorMessage = "Some cast members could not be found: %s".formatted(aMember.getValue());

        final Resource expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final Resource expectedTrailer =  Fixture.Videos.resource(VideoMediaType.TRAILER);
        final Resource expectedBanner =  Fixture.Videos.resource(VideoMediaType.BANNER);
        final Resource expectedThumb =  Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final Resource expectedThumbHalf = Fixture.Videos.resource(VideoMediaType.THUMBNAIL_HALF);

        final var aCommand = UpdateVideoCommand.with(
                expectedId,
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );
        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(Video.with(aVideo)));
        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));
        when(genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedGenres));
        when(castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(Set.of(Fixture.CastMembers.helion().getId())));




        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            useCase.execute(aCommand);
        });

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        verify(categoryGateway, times(1)).existsByIds(any());
        verify(genreGateway, times(1)).existsByIds(any());
        verify(castMemberGateway, times(1)).existsByIds(any());
        verify(videoGateway, times(0)).update(any());
    }
    @Test
    public void givenAnValidCommand_whenCallsUpdateVideoThrowsException_shouldCallClearResources(){

        final var aVideo = Fixture.Videos.systemDesign();
        final var expectedId = aVideo.getId().getValue();
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

        final Resource expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final Resource expectedTrailer =  Fixture.Videos.resource(VideoMediaType.TRAILER);
        final Resource expectedBanner =  Fixture.Videos.resource(VideoMediaType.BANNER);
        final Resource expectedThumb =  Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final Resource expectedThumbHalf = Fixture.Videos.resource(VideoMediaType.THUMBNAIL_HALF);

        final var expectedErrorMessage = "An error on update video was observed [videoId:";

        final var aCommand = UpdateVideoCommand.with(
                expectedId,
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );
        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(Video.with(aVideo)));
        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));
        when(genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedGenres));
        when(castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedMembers));
        when(videoGateway.update(any()))
                .thenThrow(new RuntimeException("Internal Server Error"));

        mockImageMedia();
        mockAudioVideoMedia();

        final var actualException =Assertions.assertThrows(InternalErrorException.class, () -> {
            useCase.execute(aCommand);
        });
        Assertions.assertNotNull(actualException);
        Assertions.assertTrue(actualException.getMessage().startsWith(expectedErrorMessage));
        verify(mediaResourceGateway,times(0)).clearResources(any());

    }

   private void mockAudioVideoMedia(){
       when(mediaResourceGateway.storeAudioVideo(any(), any())).thenAnswer(t -> {
           final var resource = t.getArgument(1, VideoResource.class);
           return Fixture.Videos.audioVideo(resource.getType());
       });
   }

    private void mockImageMedia(){
        when(mediaResourceGateway.storeImage(any(), any())).thenAnswer(t -> {
            final var resource = t.getArgument(1, VideoResource.class);
            return Fixture.Videos.image(resource.getType());
        });
    }
}
