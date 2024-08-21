package com.helion.catalog.application.genre.delete;

import com.helion.catalog.application.UseCaseTest;
import com.helion.catalog.domain.Fixture;
import com.helion.catalog.domain.genre.GenreGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class DeleteGenreUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DeleteGenreUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Test
    public void givenValidId_whenCallsDelete_shouldBeOk(){

        final var business = Fixture.Genres.business();
        final var expectedId = business.id();

        doNothing().when(genreGateway).deleteById(anyString());

        Assertions.assertDoesNotThrow(() -> this.useCase.execute(new DeleteGenreUseCase.Input(expectedId)));

        verify(this.genreGateway, times(1)).deleteById(eq(expectedId));
    }

    @Test
    public void givenInValidId_whenCallsDelete_shouldBeOk(){

        final String expectedId = null;

        //doNothing().when(genreGateway).deleteById(anyString());

        Assertions.assertDoesNotThrow(() -> this.useCase.execute(new DeleteGenreUseCase.Input(expectedId)));

        verify(this.genreGateway, times(0)).deleteById(eq(expectedId));
    }

}
