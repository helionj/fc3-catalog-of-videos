package com.helion.admin.catalog.application.genre.update;

import com.helion.admin.catalog.application.UseCaseTest;
import com.helion.admin.catalog.domain.category.CategoryGateway;
import com.helion.admin.catalog.domain.category.CategoryID;
import com.helion.admin.catalog.domain.exceptions.NotificationException;
import com.helion.admin.catalog.domain.genre.Genre;
import com.helion.admin.catalog.domain.genre.GenreGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class UpdateGenreUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUpdateGenreUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Mock
    private CategoryGateway categoryGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(genreGateway, categoryGateway);
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateGenre_ShouldReturnGenreID(){
        final var aGenre = Genre.newGenre("Action", true);
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();
        final var expectedID = aGenre.getId();

        final var aCommand = UpdateGenreCommand.with(expectedID.getValue(), expectedName, expectedIsActive, asString(expectedCategories));


        when(genreGateway.findById(any())).thenReturn(Optional.of(Genre.with(aGenre)));
        when(genreGateway.update(any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand);

        Assertions.assertNotNull(actualOutput);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(genreGateway, times(1)).findById(eq(expectedID));
        Mockito.verify(genreGateway, times(1)).update(Mockito.argThat(updatedGenre ->
                Objects.equals(expectedName, updatedGenre.getName())
                        && Objects.equals(expectedIsActive, updatedGenre.isActive())
                        && Objects.equals(expectedCategories, updatedGenre.getCategories())
                        && Objects.equals(expectedID, updatedGenre.getId())
                        && Objects.equals(aGenre.getCreatedAt(), updatedGenre.getCreatedAt())
                        && aGenre.getUpdatedAt().isBefore(updatedGenre.getUpdatedAt())
                        && Objects.isNull(updatedGenre.getDeletedAt())
        ));
    }

    @Test
    public void givenAValidCommandWithInactiveGenre_whenCallsUpdateGenre_ShouldReturnGenreID(){
        final var aGenre = Genre.newGenre("Action", true);
        final var expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();
        final var expectedID = aGenre.getId();

        final var aCommand = UpdateGenreCommand.with(expectedID.getValue(), expectedName, expectedIsActive, asString(expectedCategories));


        when(genreGateway.findById(any())).thenReturn(Optional.of(Genre.with(aGenre)));
        when(genreGateway.update(any())).thenAnswer(returnsFirstArg());

        Assertions.assertEquals(true, aGenre.isActive());
        Assertions.assertNull(aGenre.getDeletedAt());
        final var actualOutput = useCase.execute(aCommand);

        Assertions.assertNotNull(actualOutput);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(genreGateway, times(1)).findById(eq(expectedID));
        Mockito.verify(genreGateway, times(1)).update(Mockito.argThat(updatedGenre ->
                Objects.equals(expectedName, updatedGenre.getName())
                        && Objects.equals(expectedIsActive, updatedGenre.isActive())
                        && Objects.equals(expectedCategories, updatedGenre.getCategories())
                        && Objects.equals(expectedID, updatedGenre.getId())
                        && Objects.equals(aGenre.getCreatedAt(), updatedGenre.getCreatedAt())
                        && aGenre.getUpdatedAt().isBefore(updatedGenre.getUpdatedAt())
                        && Objects.nonNull(updatedGenre.getDeletedAt())
        ));
    }

    @Test
    public void givenAValidCommandWithCategories_whenCallsUpdateGenre_ShouldReturnGenreID(){
        final var aGenre = Genre.newGenre("Action", true);
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(
                CategoryID.from("123"),
                CategoryID.from("456")
        );
        final var expectedID = aGenre.getId();

        final var aCommand = UpdateGenreCommand.with(expectedID.getValue(), expectedName, expectedIsActive, asString(expectedCategories));

        when(genreGateway.findById(any())).thenReturn(Optional.of(Genre.with(aGenre)));

        when(categoryGateway.existsByIds(any())).thenReturn(expectedCategories);

        when(genreGateway.update(any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand);

        Assertions.assertNotNull(actualOutput);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(genreGateway, times(1)).findById(eq(expectedID));
        Mockito.verify(categoryGateway, times(1)).existsByIds(eq(expectedCategories));
        Mockito.verify(genreGateway, times(1)).update(Mockito.argThat(updatedGenre ->
                Objects.equals(expectedName, updatedGenre.getName())
                        && Objects.equals(expectedIsActive, updatedGenre.isActive())
                        && Objects.equals(expectedCategories, updatedGenre.getCategories())
                        && Objects.equals(expectedID, updatedGenre.getId())
                        && Objects.equals(aGenre.getCreatedAt(), updatedGenre.getCreatedAt())
                        && aGenre.getUpdatedAt().isBefore(updatedGenre.getUpdatedAt())
                        && Objects.isNull(updatedGenre.getDeletedAt())
        ));
    }

    @Test
    public void givenAinValidName_whenCallsUpdateGenre_ShouldReturnNotificationException(){
        final var aGenre = Genre.newGenre("Action", true);
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();
        final var expectedID = aGenre.getId();

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateGenreCommand.with(expectedID.getValue(), expectedName, expectedIsActive, asString(expectedCategories));

        when(genreGateway.findById(any())).thenReturn(Optional.of(Genre.with(aGenre)));

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());

        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(genreGateway, times(1)).findById(eq(expectedID));

    }

    @Test
    public void givenAinValidName_whenCallsUpdateGenreAndSomeCategoriesDoesNotExists_ShouldReturnNotificationException(){
        final var filmes = CategoryID.from("123");
        final var series = CategoryID.from("456");
        final var documentarios = CategoryID.from("789");

        final var aGenre = Genre.newGenre("Action", true);
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.of(
                filmes, series, documentarios
        );
        final var expectedID = aGenre.getId();

        final var expectedErrorMessageOne = "Some categories could not be found: 456, 789";
        final var expectedErrorMessageTwo = "'name' should not be null";
        final var expectedErrorCount = 2;

        final var aCommand = UpdateGenreCommand.with(expectedID.getValue(), expectedName, expectedIsActive, asString(expectedCategories));

        when(genreGateway.findById(any())).thenReturn(Optional.of(Genre.with(aGenre)));
        when(categoryGateway.existsByIds(any())).thenReturn(List.of(filmes));

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());

        Assertions.assertEquals(expectedErrorMessageOne, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorMessageTwo, actualException.getErrors().get(1).message());

        Mockito.verify(genreGateway, times(1)).findById(eq(expectedID));
        Mockito.verify(categoryGateway, times(1)).existsByIds(eq(expectedCategories));

    }




}
