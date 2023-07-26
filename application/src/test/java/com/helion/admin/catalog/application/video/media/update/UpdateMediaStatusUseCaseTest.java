package com.helion.admin.catalog.application.video.media.update;

import com.helion.admin.catalog.application.UseCaseTest;
import com.helion.admin.catalog.domain.Fixture;
import com.helion.admin.catalog.domain.video.MediaStatus;
import com.helion.admin.catalog.domain.video.Video;
import com.helion.admin.catalog.domain.video.VideoGateway;
import com.helion.admin.catalog.domain.video.VideoMediaType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

public class UpdateMediaStatusUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUpdateMediaStatusUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(videoGateway);
    }

    @Test
    public void givenCommandUpdateStatusForVideo_whenIsValid_shouldUpdateStatusAndEncodedLocation(){

        final var expectedStatus = MediaStatus.COMPLETED;
        final var expectedFolder = "encoded-media";
        final var expectedFileName = "filename.mp4";
        final var expectedType = VideoMediaType.VIDEO;
        final var expectedMedia = Fixture.Videos.audioVideo(expectedType);

        final var aVideo = Fixture.Videos.systemDesign().updateVideoMedia(expectedMedia);

        final var expectedId = aVideo.getId();

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(aVideo));

        when(videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var aCmd = UpdateMediaStatusCommand.with(
                expectedStatus,
                expectedId.getValue(),
                expectedMedia.id(),
                expectedFolder,
                expectedFileName
        );

        this.useCase.execute(aCmd);

        final var captor = ArgumentCaptor.forClass(Video.class);

        verify(videoGateway, times(1)).findById(eq(expectedId));
        verify(videoGateway, times(1)).update(captor.capture());

        final var actualVideo = captor.getValue();

        Assertions.assertTrue(actualVideo.getTrailer().isEmpty());

        final var actualVideoMedia = actualVideo.getVideo().get();

        Assertions.assertEquals(expectedMedia.id(), actualVideoMedia.id());
        Assertions.assertEquals(expectedMedia.rawLocation(), actualVideoMedia.rawLocation());
        Assertions.assertEquals(expectedMedia.checksum(), actualVideoMedia.checksum());
        Assertions.assertEquals(expectedStatus, actualVideoMedia.status());
        Assertions.assertEquals(expectedFolder.concat("/").concat(expectedFileName), actualVideoMedia.encodedLocation());
    }
    @Test
    public void givenCommandUpdateStatusForTrailer_whenIsValid_shouldUpdateStatusAndEncodedLocation(){

        final var expectedStatus = MediaStatus.COMPLETED;
        final var expectedFolder = "encoded-media";
        final var expectedFileName = "filename.mp4";
        final var expectedType = VideoMediaType.TRAILER;
        final var expectedMedia = Fixture.Videos.audioVideo(expectedType);

        final var aVideo = Fixture.Videos.systemDesign().updateTrailerMedia(expectedMedia);

        final var expectedId = aVideo.getId();

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(aVideo));

        when(videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var aCmd = UpdateMediaStatusCommand.with(
                expectedStatus,
                expectedId.getValue(),
                expectedMedia.id(),
                expectedFolder,
                expectedFileName
        );

        this.useCase.execute(aCmd);

        final var captor = ArgumentCaptor.forClass(Video.class);

        verify(videoGateway, times(1)).findById(eq(expectedId));

        verify(videoGateway, times(1)).update(captor.capture());

        final var actualVideo = captor.getValue();

        Assertions.assertTrue(actualVideo.getVideo().isEmpty());

        final var actualVideoMedia = actualVideo.getTrailer().get();

        Assertions.assertEquals(expectedMedia.id(), actualVideoMedia.id());
        Assertions.assertEquals(expectedMedia.rawLocation(), actualVideoMedia.rawLocation());
        Assertions.assertEquals(expectedMedia.checksum(), actualVideoMedia.checksum());
        Assertions.assertEquals(expectedStatus, actualVideoMedia.status());
        Assertions.assertEquals(expectedFolder.concat("/").concat(expectedFileName), actualVideoMedia.encodedLocation());
    }

    @Test
    public void givenCommandUpdateStatusForTrailer_whenIsInValid_shouldDoNothing(){

        final var expectedStatus = MediaStatus.COMPLETED;
        final var expectedFolder = "encoded-media";
        final var expectedFileName = "filename.mp4";
        final var expectedType = VideoMediaType.TRAILER;
        final var expectedMedia = Fixture.Videos.audioVideo(expectedType);

        final var aVideo = Fixture.Videos.systemDesign().updateTrailerMedia(expectedMedia);

        final var expectedId = aVideo.getId();

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(aVideo));

        final var aCmd = UpdateMediaStatusCommand.with(
                expectedStatus,
                expectedId.getValue(),
                "fake_id",
                expectedFolder,
                expectedFileName
        );

        this.useCase.execute(aCmd);


        verify(videoGateway, times(0)).update(any());

    }

    @Test
    public void givenCommandUpdateStatusForVideo_whenIsValidForProcessing_shouldUpdateStatusAndEncodedLocation(){

        final var expectedStatus = MediaStatus.PROCESSING;
        final String expectedFolder = null;
        final String expectedFileName = null;
        final var expectedType = VideoMediaType.VIDEO;
        final var expectedMedia = Fixture.Videos.audioVideo(expectedType);

        final var aVideo = Fixture.Videos.systemDesign().updateVideoMedia(expectedMedia);

        final var expectedId = aVideo.getId();

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(aVideo));

        when(videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var aCmd = UpdateMediaStatusCommand.with(
                expectedStatus,
                expectedId.getValue(),
                expectedMedia.id(),
                expectedFolder,
                expectedFileName
        );

        this.useCase.execute(aCmd);

        final var captor = ArgumentCaptor.forClass(Video.class);

        verify(videoGateway, times(1)).findById(eq(expectedId));
        verify(videoGateway, times(1)).update(captor.capture());

        final var actualVideo = captor.getValue();

        Assertions.assertTrue(actualVideo.getTrailer().isEmpty());

        final var actualVideoMedia = actualVideo.getVideo().get();

        Assertions.assertEquals(expectedMedia.id(), actualVideoMedia.id());
        Assertions.assertEquals(expectedMedia.rawLocation(), actualVideoMedia.rawLocation());
        Assertions.assertEquals(expectedMedia.checksum(), actualVideoMedia.checksum());
        Assertions.assertEquals(expectedStatus, actualVideoMedia.status());
        Assertions.assertTrue(actualVideoMedia.encodedLocation().isBlank());
    }

    @Test
    public void givenCommandUpdateStatusForTrailer_whenIsValidForProcessing_shouldUpdateStatusAndEncodedLocation(){

        final var expectedStatus = MediaStatus.PROCESSING;
        final String expectedFolder = "";
        final String expectedFileName = "";
        final var expectedType = VideoMediaType.TRAILER;
        final var expectedMedia = Fixture.Videos.audioVideo(expectedType);

        final var aVideo = Fixture.Videos.systemDesign().updateTrailerMedia(expectedMedia);

        final var expectedId = aVideo.getId();

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(aVideo));

        when(videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var aCmd = UpdateMediaStatusCommand.with(
                expectedStatus,
                expectedId.getValue(),
                expectedMedia.id(),
                expectedFolder,
                expectedFileName
        );

        this.useCase.execute(aCmd);

        final var captor = ArgumentCaptor.forClass(Video.class);

        verify(videoGateway, times(1)).findById(eq(expectedId));

        verify(videoGateway, times(1)).update(captor.capture());

        final var actualVideo = captor.getValue();

        Assertions.assertTrue(actualVideo.getVideo().isEmpty());

        final var actualVideoMedia = actualVideo.getTrailer().get();

        Assertions.assertEquals(expectedMedia.id(), actualVideoMedia.id());
        Assertions.assertEquals(expectedMedia.rawLocation(), actualVideoMedia.rawLocation());
        Assertions.assertEquals(expectedMedia.checksum(), actualVideoMedia.checksum());
        Assertions.assertEquals(expectedStatus, actualVideoMedia.status());
        Assertions.assertTrue(actualVideoMedia.encodedLocation().isBlank());
    }


}
