package com.helion.catalog.application.video.delete;

import com.helion.catalog.application.UseCaseTest;
import com.helion.catalog.domain.Fixture;
import com.helion.catalog.domain.video.VideoGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class DeleteVideoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DeleteVideoUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Test
    public void givenValidId_whenCallsDelete_shouldBeOk(){

        final var java21 = Fixture.Videos.java21();
        final var expectedId = java21.id();

        doNothing().when(videoGateway).deleteById(anyString());

        Assertions.assertDoesNotThrow(() -> this.useCase.execute(new DeleteVideoUseCase.Input(expectedId)));

        verify(this.videoGateway, times(1)).deleteById(eq(expectedId));
    }

    @Test
    public void givenInValidId_whenCallsDelete_shouldBeOk(){

        final String expectedId = null;

        //doNothing().when(genreGateway).deleteById(anyString());

        Assertions.assertDoesNotThrow(() -> this.useCase.execute(new DeleteVideoUseCase.Input(expectedId)));

        verify(this.videoGateway, times(0)).deleteById(eq(expectedId));
    }

}
