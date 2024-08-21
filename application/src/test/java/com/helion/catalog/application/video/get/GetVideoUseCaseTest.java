package com.helion.catalog.application.video.get;

import com.helion.catalog.application.UseCaseTest;
import com.helion.catalog.domain.Fixture;
import com.helion.catalog.domain.video.VideoGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


public class GetVideoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private GetVideoUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Test
    public void givenValidVideo_whenCallGetVideoById_ShouldReturnIt(){
        final var java21 = Fixture.Videos.java21();

        doReturn(Optional.of(java21)).when(videoGateway).findById(anyString());

        final var actualOutput = this.useCase.execute(new GetVideoUseCase.Input(java21.id())).get();

        verify(videoGateway, times(1)).findById(java21.id());

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());
        Assertions.assertEquals(java21.title(), actualOutput.title());
        Assertions.assertEquals(java21.description(), actualOutput.description());
        Assertions.assertEquals(java21.launchedAt().getValue(), actualOutput.launchedAt());
        Assertions.assertEquals(java21.duration(), actualOutput.duration());
        Assertions.assertEquals(java21.opened(), actualOutput.opened());
        Assertions.assertEquals(java21.published(), actualOutput.published());
        Assertions.assertEquals(java21.rating().getName(), actualOutput.rating());
        Assertions.assertEquals(java21.createdAt().toString(), actualOutput.createdAt());
        Assertions.assertEquals(java21.updatedAt().toString(), actualOutput.updatedAt());
        Assertions.assertEquals(java21.categories(), actualOutput.categories());
        Assertions.assertEquals(java21.genres(), actualOutput.genres());
        Assertions.assertEquals(java21.castMembers(), actualOutput.castMembers());
        Assertions.assertEquals(java21.video(),actualOutput.video());
        Assertions.assertEquals(java21.trailer(),actualOutput.trailer());
        Assertions.assertEquals(java21.banner(), actualOutput.banner());
        Assertions.assertEquals(java21.thumb(), actualOutput.thumb());
        Assertions.assertEquals(java21.thumbHalf(), actualOutput.thumbHalf());
    }

    @Test
    public void giveNullInput_whenCallsDelete_shouldBeOk(){

        final GetVideoUseCase.Input input = null;


        final var actualError =Assertions.assertDoesNotThrow(() -> this.useCase.execute(input));

        verify(this.videoGateway, never()).deleteById(any());

        Assertions.assertTrue(actualError.isEmpty());
    }

    @Test
    public void givenInValidId_whenCallsDelete_shouldBeOk(){

        final String expectedId = null;


        final var actualError = Assertions.assertDoesNotThrow(
                () -> this.useCase.execute(new GetVideoUseCase.Input(expectedId)));

        verify(this.videoGateway, never()).deleteById(any());

        Assertions.assertTrue(actualError.isEmpty());

    }
}
