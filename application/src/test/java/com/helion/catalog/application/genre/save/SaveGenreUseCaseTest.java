package com.helion.catalog.application.genre.save;

import com.helion.catalog.application.UseCaseTest;
import com.helion.catalog.domain.exceptions.DomainException;
import com.helion.catalog.domain.genre.GenreGateway;
import com.helion.catalog.domain.utils.InstantUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class SaveGenreUseCaseTest extends UseCaseTest {

    @InjectMocks
    private SaveGenreUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Test
    public void givenAValidInput_whenCallsSave_shouldPersistIt() {

        final var expectedName = "Business";
        final var expectedIsActive = true;
        final var expectedCategories = Set.of("c1", "c2");
        final var expectedId = UUID.randomUUID().toString();
        final var expectedDates = InstantUtils.now();

        Mockito.when(genreGateway.save(any())).thenAnswer(returnsFirstArg());

        final var input =
                new SaveGenreUseCase.Input(expectedId, expectedName, expectedCategories, expectedIsActive, expectedDates, expectedDates, null);

        final var actualOutput = this.useCase.execute(input);

        verify(genreGateway, times(1)).save(any());

        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId, actualOutput.id());
    }

    @Test
    public void givenANullInput_whenCallsSave_shouldReturnsError() {

        final var expectedErrorCount = 1;
        final var expectedMessage = "'SaveGenreUseCase.Input' cannot be null";
        final SaveGenreUseCase.Input input = null;

        final var actualError = assertThrows(DomainException.class, () -> this.useCase.execute(input));

        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedMessage, actualError.getErrors().get(0).message());
        verify(genreGateway, times(0)).save(any());
    }

    @Test
    public void givenInvalidId_whenCallsSave_shouldReturnError() {
        // given
        final String expectedID = null;
        final var expectedName = "Business";
        final var expectedIsActive = true;
        final var expectedCategories = Set.of("c1", "c2");
        final var expectedDates = InstantUtils.now();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'id' should not be empty";

        // when
        final var input =
                new SaveGenreUseCase.Input(expectedID, expectedName,  expectedCategories, expectedIsActive,expectedDates, expectedDates, expectedDates);

        final var actualError = Assertions.assertThrows(
                DomainException.class,
                () -> this.useCase.execute(input)
        );

        // then
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());

        verify(genreGateway, times(0)).save(any());
    }

    @Test
    public void givenInvalidName_whenCallsSave_shouldReturnError() {
        // given
        final var expectedID = UUID.randomUUID().toString();
        final String expectedName = " ";
        final var expectedIsActive = true;
        final var expectedCategories = Set.of("c1", "c2");
        final var expectedDates = InstantUtils.now();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        // when
        final var input =
                new SaveGenreUseCase.Input(expectedID, expectedName, expectedCategories, expectedIsActive, expectedDates, expectedDates, expectedDates);

        final var actualError = Assertions.assertThrows(
                DomainException.class,
                () -> this.useCase.execute(input)
        );

        // then
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());

        verify(genreGateway, times(0)).save(any());
    }

}
