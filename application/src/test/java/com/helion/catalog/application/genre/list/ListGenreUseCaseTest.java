package com.helion.catalog.application.genre.list;

import com.helion.catalog.application.UseCaseTest;
import com.helion.catalog.domain.Fixture;
import com.helion.catalog.domain.genre.GenreGateway;
import com.helion.catalog.domain.pagination.Pagination;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ListGenreUseCaseTest extends UseCaseTest {

    @InjectMocks
    private ListGenreUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Test
    public void givenAValidQuery_whenCallsListCategories_shouldReturCategories() {
        final var genres = List.of(
                Fixture.Genres.business(), Fixture.Genres.marketing()
        );

        final var expectedItems = genres.stream().map(ListGenreUseCase.Output::from).toList();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "algo";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 2;
        final var expectedCategories = Set.of("c1");

        final var aQuery =
                new ListGenreUseCase.Input(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection, expectedCategories);

        final var pagination =
                new Pagination<>(expectedPage, expectedPerPage, genres.size(), genres);


        when(this.genreGateway.findAll(any())).thenReturn(pagination);

        final var actualOutput = this.useCase.execute(aQuery);

        Assertions.assertEquals(expectedPage, actualOutput.meta().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.meta().perPage());
        Assertions.assertEquals(expectedItemsCount, actualOutput.data().size());
        Assertions.assertTrue(expectedItems.size() == actualOutput.data().size() && expectedItems.containsAll(actualOutput.data()));


    }

}
