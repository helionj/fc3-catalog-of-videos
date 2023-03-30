package com.helion.admin.catalog.application.genre.retrieve.list;

import com.helion.admin.catalog.application.UseCaseTest;
import com.helion.admin.catalog.application.category.retrieve.list.CategoryListOutput;
import com.helion.admin.catalog.domain.category.Category;
import com.helion.admin.catalog.domain.category.CategoryID;
import com.helion.admin.catalog.domain.category.pagination.Pagination;
import com.helion.admin.catalog.domain.category.pagination.SearchQuery;
import com.helion.admin.catalog.domain.genre.Genre;
import com.helion.admin.catalog.domain.genre.GenreGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;

public class ListGenreUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultListGenreUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(genreGateway);
    }

    @Test
    public void givenAValidQuery_whenCallsListGenres_shouldReturnGenres(){

        final var filmes = CategoryID.from("123");
        final var series =CategoryID.from("456");


        final var categories = List.of(

                CategoryID.from("456")
        );
        final var acaoGenre = Genre.newGenre("Ação", true);
        final var docGenre = Genre.newGenre("Drama", true);
        final var romanticGenre = Genre.newGenre("Romantico", true);
        acaoGenre.addCategory(filmes);
        docGenre.addCategories(List.of(filmes, series));
        romanticGenre.addCategory(filmes);

        final var genres = List.of(
               acaoGenre, docGenre, romanticGenre
        );
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 3;

        final var expectedItems = genres.stream()
                .map(GenreListOutput::from)
                .toList();

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);


        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, genres.size(), genres);
        final var expectedResult = expectedPagination.map(GenreListOutput::from);
        Mockito.when(genreGateway.findAll(eq(aQuery))).thenReturn(expectedPagination);

        final var actualResult = useCase.execute(aQuery);
        Assertions.assertEquals(expectedItems, actualResult.items());
        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedResult, actualResult);
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(genres.size(), actualResult.total());

        Mockito.verify(genreGateway, times(1)).findAll(eq(aQuery));

    }

    @Test
    public void givenAValidQuery_whenHasNoResults_shouldReturnEmpty(){
        final var genres = List.<Genre>of();
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 0;

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);


        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, genres.size(), genres);
        final var expectedResult = expectedPagination.map(GenreListOutput::from);
        Mockito.when(genreGateway.findAll(eq(aQuery))).thenReturn(expectedPagination);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedResult, actualResult);
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(genres.size(), actualResult.total());
    }

    @Test
    public void givenAValidQuery_whenGatewayThrowsException_shouldReturnException(){

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedErrorMessage = "Gateway Error";

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        Mockito.when(genreGateway.findAll(eq(aQuery))).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualException = Assertions.assertThrows(IllegalStateException.class,
                () -> useCase.execute(aQuery));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

    }
}
