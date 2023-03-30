package com.helion.admin.catalog.application.genre.delete;

import com.helion.admin.catalog.application.UseCaseTest;
import com.helion.admin.catalog.domain.category.Category;
import com.helion.admin.catalog.domain.category.CategoryGateway;
import com.helion.admin.catalog.domain.genre.Genre;
import com.helion.admin.catalog.domain.genre.GenreGateway;
import com.helion.admin.catalog.domain.genre.GenreID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class DeleteGenreUseCaseTest extends UseCaseTest {


    @InjectMocks
    private DefaultDeleteGenreUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(genreGateway);
    }

    @Test
    public void givenAValidGenreID_whenCallsDeleteCategory_shouldBeDeleted(){
        final var aGenre = Genre.newGenre("Ação", true);

        final var expectedId = aGenre.getId();
        doNothing().when(genreGateway).deleteById(eq(expectedId));
        Assertions.assertDoesNotThrow(() ->  useCase.execute(expectedId.getValue()));

        Mockito.verify(genreGateway, times(1)).deleteById(expectedId);

    }

    @Test
    public void givenAinValidGenreID_whenCallsDeleteCategory_shouldBeOk(){

        final var expectedId = GenreID.from("inalidID");
        doNothing().when(genreGateway).deleteById(eq(expectedId));
        Assertions.assertDoesNotThrow(() ->  useCase.execute(expectedId.getValue()));

        Mockito.verify(genreGateway, times(1)).deleteById(expectedId);

    }

    @Test
    public void givenAValidGenreID_whenCallsDeleteCategoryAndGatewayThrowsUnexpectedError_shouldReceiveAnException(){

        final var aGenre = Genre.newGenre("Ação", true);
        final var expectedId = aGenre.getId();

        doThrow(new IllegalStateException("Gateway Error"))
                .when(genreGateway).deleteById(any());

        Assertions.assertThrows(IllegalStateException.class, () ->  useCase.execute(expectedId.getValue()));

        Mockito.verify(genreGateway, times(1)).deleteById(expectedId);

    }
}
