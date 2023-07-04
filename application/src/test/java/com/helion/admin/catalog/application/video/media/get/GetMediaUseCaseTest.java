package com.helion.admin.catalog.application.video.media.get;

import com.helion.admin.catalog.application.UseCaseTest;
import com.helion.admin.catalog.domain.Fixture;
import com.helion.admin.catalog.domain.exceptions.NotFoundException;
import com.helion.admin.catalog.domain.video.MediaResourceGateway;
import com.helion.admin.catalog.domain.video.VideoID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

public class GetMediaUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultGetMediaUseCase useCase;

    @Mock
    private MediaResourceGateway mediaResourceGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(mediaResourceGateway);
    }

    @Test
    public void givenVideoIdAndType_whenIsValidCmd_shouldReturnResource(){

        final var expectedId = VideoID.unique();
        final var expectedType = Fixture.Videos.mediaType();
        final var expectedResource = Fixture.Videos.resource(expectedType);

        when(mediaResourceGateway.getResource(expectedId, expectedType))
                .thenReturn(Optional.of(expectedResource));

        final var aCommand = GetMediaCommand.with(expectedId.getValue(), expectedType.name());

        final var actualResult =  this.useCase.execute(aCommand);

        Assertions.assertEquals(expectedResource.name(), actualResult.name());
        Assertions.assertEquals(expectedResource.content(), actualResult.content());
        Assertions.assertEquals(expectedResource.contentType(), actualResult.contentType());

    }

    @Test
    public void givenVideoIdAndType_whenIsNotFound_shouldReturnNotFoundException(){

        final var expectedId = VideoID.unique();
        final var expectedType = Fixture.Videos.mediaType();

        when(mediaResourceGateway.getResource(expectedId, expectedType))
                .thenReturn(Optional.empty());

        final var aCommand = GetMediaCommand.with(expectedId.getValue(), expectedType.name());

        Assertions.assertThrows(NotFoundException.class, () -> {
            this.useCase.execute(aCommand);
        });

    }

    @Test
    public void givenVideoIdAndType_whenTypeDoesntExists_shouldReturnNotFoundException(){

        final var expectedId = VideoID.unique();
        final var expectedErrorMessage = "Media Type QUALQUER doesn't exists";
        final var aCommand = GetMediaCommand.with(expectedId.getValue(), "QUALQUER");

        final var expectedException= Assertions.assertThrows(NotFoundException.class, () -> {
            this.useCase.execute(aCommand);
        });

        Assertions.assertEquals(expectedErrorMessage, expectedException.getMessage());

    }

}
