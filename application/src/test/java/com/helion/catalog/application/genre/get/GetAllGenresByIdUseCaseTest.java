package com.helion.catalog.application.genre.get;

import com.helion.catalog.application.UseCaseTest;
import com.helion.catalog.domain.Fixture;
import com.helion.catalog.domain.genre.Genre;
import com.helion.catalog.domain.genre.GenreGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GetAllGenresByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private GetAllGenresByIdUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Test
    public void givenAValidIds_whenCallsGetAllById_shouldReturnIt() {
        final var genres = List.of(
                Fixture.Genres.business(), Fixture.Genres.marketing()
        );

        final var expectedItems = genres.stream().map(GetAllGenresByIdUseCase.Output::new).toList();

        final var expectedIds = genres.stream().map(Genre::id).collect(Collectors.toSet());



        when(this.genreGateway.findAllById(any())).thenReturn(genres);

        final var actualOutput = this.useCase.execute(new GetAllGenresByIdUseCase.Input(expectedIds));

        Assertions.assertTrue(expectedItems.size() == actualOutput.size() && expectedItems.containsAll(actualOutput));

        verify(genreGateway, times(1)).findAllById(any());

    }

    @Test
    public void givenANullIds_whenCallsGetAllById_shouldReturEmpty() {


        final var expectedItems = List.of();

        final Set<String> expectedIds = null;


        final var actualOutput = this.useCase.execute(new GetAllGenresByIdUseCase.Input(expectedIds));

        Assertions.assertTrue(actualOutput.isEmpty());

        verify(genreGateway, times(0)).findAllById(any());
    }

}