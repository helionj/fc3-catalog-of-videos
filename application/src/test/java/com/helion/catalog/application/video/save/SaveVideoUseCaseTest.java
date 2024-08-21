package com.helion.catalog.application.video.save;

import com.helion.catalog.application.UseCaseTest;
import com.helion.catalog.domain.exceptions.DomainException;
import com.helion.catalog.domain.utils.IdUtils;
import com.helion.catalog.domain.utils.InstantUtils;
import com.helion.catalog.domain.video.Rating;
import com.helion.catalog.domain.video.VideoGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.Year;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class SaveVideoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private SaveVideoUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Test
    public void givenAValidInput_whenCallsSave_shouldPersistIt() {

        final var expectedId = IdUtils.uniqueId();
        final var expectedTitle = "System Design Interview";
        final var expectedDescription = """
            Lets design the high-level architecture of youtube - similar to 
            how we'd tackle this in a system design interview.
            """;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCreatedAt = InstantUtils.now();
        final var expectedUpdatedAt = InstantUtils.now();
        final var expectedCategories = Set.of(IdUtils.uniqueId());
        final var expectedGenres = Set.of(IdUtils.uniqueId());
        final var expectedMembers = Set.of(IdUtils.uniqueId());
        final var expectedVideo= "http://video";
        final var expectedTrailer = "http://trailer";
        final var expectedBanner = "http://banner";
        final var expectedThumb = "http://thumbnaill";
        final var expectedThumbHalf =  "http://thumbnaill-half";


        Mockito.when(videoGateway.save(any())).thenAnswer(returnsFirstArg());

        final var input =
                new SaveVideoUseCase.Input(
                        expectedId,
                        expectedTitle,
                        expectedDescription,
                        expectedLaunchedAt.getValue(),
                        expectedDuration,
                        expectedRating.getName(),
                        expectedOpened,
                        expectedPublished,
                        expectedCreatedAt.toString(),
                        expectedUpdatedAt.toString(),
                        expectedBanner,
                        expectedThumb,
                        expectedThumbHalf,
                        expectedTrailer,
                        expectedVideo,
                        expectedCategories,
                        expectedGenres,
                        expectedMembers);

        final var actualOutput = this.useCase.execute(input);

        verify(videoGateway, times(1)).save(any());

        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId, actualOutput.id());
    }

    @Test
    public void givenANullInput_whenCallsSave_shouldReturnsError() {

        final var expectedErrorCount = 1;
        final var expectedMessage = "'SaveVideoUseCase.Input' cannot be null";
        final SaveVideoUseCase.Input input = null;

        final var actualError = assertThrows(DomainException.class, () -> this.useCase.execute(input));

        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedMessage, actualError.getErrors().get(0).message());
        verify(videoGateway, times(0)).save(any());
    }

    @Test
    public void givenInvalidId_whenCallsSave_shouldReturnError() {
        // given

        final String expectedId = null;
        final var expectedTitle = "System Design Interview";
        final var expectedDescription = """
            Lets design the high-level architecture of youtube - similar to 
            how we'd tackle this in a system design interview.
            """;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCreatedAt = InstantUtils.now();
        final var expectedUpdatedAt = InstantUtils.now();
        final var expectedCategories = Set.of(IdUtils.uniqueId());
        final var expectedGenres = Set.of(IdUtils.uniqueId());
        final var expectedMembers = Set.of(IdUtils.uniqueId());
        final var expectedVideo= "http://video";
        final var expectedTrailer = "http://trailer";
        final var expectedBanner = "http://banner";
        final var expectedThumb = "http://thumbnaill";
        final var expectedThumbHalf =  "http://thumbnaill-half";

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'id' should not be empty";

        // when
        final var input =
                new SaveVideoUseCase.Input(
                        expectedId,
                        expectedTitle,
                        expectedDescription,
                        expectedLaunchedAt.getValue(),
                        expectedDuration,
                        expectedRating.getName(),
                        expectedOpened,
                        expectedPublished,
                        expectedCreatedAt.toString(),
                        expectedUpdatedAt.toString(),
                        expectedBanner,
                        expectedThumb,
                        expectedThumbHalf,
                        expectedTrailer,
                        expectedVideo,
                        expectedCategories,
                        expectedGenres,
                        expectedMembers);

        final var actualError = Assertions.assertThrows(
                DomainException.class,
                () -> this.useCase.execute(input)
        );

        // then
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());

        verify(videoGateway, times(0)).save(any());
    }

    @Test
    public void givenInvalidTitle_whenCallsSave_shouldReturnError() {
        // given

        final String expectedId = IdUtils.uniqueId();
        final var expectedTitle = "";
        final var expectedDescription = """
            Lets design the high-level architecture of youtube - similar to 
            how we'd tackle this in a system design interview.
            """;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCreatedAt = InstantUtils.now();
        final var expectedUpdatedAt = InstantUtils.now();
        final var expectedCategories = Set.of(IdUtils.uniqueId());
        final var expectedGenres = Set.of(IdUtils.uniqueId());
        final var expectedMembers = Set.of(IdUtils.uniqueId());
        final var expectedVideo= "http://video";
        final var expectedTrailer = "http://trailer";
        final var expectedBanner = "http://banner";
        final var expectedThumb = "http://thumbnaill";
        final var expectedThumbHalf =  "http://thumbnaill-half";

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'title' should not be empty";

        // when
        final var input =
                new SaveVideoUseCase.Input(
                        expectedId,
                        expectedTitle,
                        expectedDescription,
                        expectedLaunchedAt.getValue(),
                        expectedDuration,
                        expectedRating.getName(),
                        expectedOpened,
                        expectedPublished,
                        expectedCreatedAt.toString(),
                        expectedUpdatedAt.toString(),
                        expectedBanner,
                        expectedThumb,
                        expectedThumbHalf,
                        expectedTrailer,
                        expectedVideo,
                        expectedCategories,
                        expectedGenres,
                        expectedMembers);

        final var actualError = Assertions.assertThrows(
                DomainException.class,
                () -> this.useCase.execute(input)
        );

        // then
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());

        verify(videoGateway, times(0)).save(any());
    }

}
