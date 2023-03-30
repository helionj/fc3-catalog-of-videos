package com.helion.admin.catalog.application.genre.retrieve.get;

import com.helion.admin.catalog.application.UseCaseTest;
import com.helion.admin.catalog.domain.category.CategoryID;
import com.helion.admin.catalog.domain.exceptions.NotFoundException;
import com.helion.admin.catalog.domain.genre.Genre;
import com.helion.admin.catalog.domain.genre.GenreGateway;
import com.helion.admin.catalog.domain.genre.GenreID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class GetGenreByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultGetGenreByIdUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(genreGateway);
    }

    @Test
    public void givenAnValidId_whenCallsGetGenreById_shouldReturnsAGenre(){

        final var expectedName = "Ação";
        final var expectedCategories = List.<CategoryID>of();
        final var isActive = true;
        final var aGenre = Genre.newGenre(expectedName, isActive);
        final var expectedId = aGenre.getId();

        when(genreGateway.findById(eq(expectedId))).thenReturn(Optional.of(aGenre.clone()));
        final var actualGenre =  useCase.execute(expectedId.getValue());

        Assertions.assertEquals(expectedId, actualGenre.id());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedCategories, actualGenre.categories());
        Assertions.assertEquals(isActive, actualGenre.isActive());
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.createdAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), actualGenre.updatedAt());
        Assertions.assertEquals(aGenre.getDeletedAt(), actualGenre.deletedAt());
        Assertions.assertEquals(GenreOutput.from(aGenre), actualGenre);
        Mockito.verify(genreGateway, times(1)).findById(expectedId);

    }

    @Test
    public void givenAnValidId_whenCallsGetGenreByIdWithCategories_shouldReturnsAGenreWithCategories(){

        final var expectedName = "Ação";
        final var expectedCategories = List.<CategoryID>of(
                CategoryID.from("123"),
                CategoryID.from("456")
        );
        final var isActive = true;
        final var aGenre = Genre.newGenre(expectedName, isActive);
        aGenre.addCategories(expectedCategories);
        final var expectedId = aGenre.getId();

        when(genreGateway.findById(eq(expectedId))).thenReturn(Optional.of(aGenre.clone()));
        final var actualGenre =  useCase.execute(expectedId.getValue());

        Assertions.assertEquals(expectedId, actualGenre.id());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(asString(expectedCategories), actualGenre.categories());
        Assertions.assertEquals(isActive, actualGenre.isActive());
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.createdAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), actualGenre.updatedAt());
        Assertions.assertEquals(aGenre.getDeletedAt(), actualGenre.deletedAt());
        Assertions.assertEquals(GenreOutput.from(aGenre), actualGenre);
        Mockito.verify(genreGateway, times(1)).findById(expectedId);

    }

    @Test
    public void givenAiNValidId_whenCallsGetGenreById_shouldReturnsNotFound(){

        final var expectedId = GenreID.from("123");
        final var expectedErrorMessage = "Genre with ID 123 was not found";
        when(genreGateway.findById(eq(expectedId))).thenReturn(Optional.empty());
        final var actualException = Assertions.assertThrows(NotFoundException.class,
                () -> useCase.execute(expectedId.getValue()));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenAnValidId_whenGatewayThrowsException_shouldReturnsException(){
        final var aCategory = Genre.newGenre("Action", true);
        final var expectedErrorMessage = "Gateway Error";
        final var expectedId = aCategory.getId();
        when(genreGateway.findById(eq(expectedId))).thenThrow(new IllegalStateException("Gateway Error"));

        final var actualException = Assertions.assertThrows(IllegalStateException.class, () ->  useCase.execute(expectedId.getValue()));
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
        Mockito.verify(genreGateway, times(1)).findById(expectedId);

    }

    private List<String> asString(List<CategoryID> categories) {
        return categories.stream()
                .map(CategoryID::getValue)
                .toList();
    }

}
