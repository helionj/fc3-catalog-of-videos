package com.helion.catalog.application.category.list;

import com.helion.catalog.application.UseCaseTest;
import com.helion.catalog.domain.Fixture;
import com.helion.catalog.domain.category.CategoryGateway;
import com.helion.catalog.domain.category.CategorySearchQuery;
import com.helion.catalog.domain.pagination.Pagination;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ListCategoryUseCaseTest extends UseCaseTest {

    @InjectMocks
    private ListCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidQuery_whenCallsListCategories_shouldReturCategories() {
        final var categories = List.of(
                Fixture.Categories.aulas(), Fixture.Categories.lives()
        );

        final var expectedItems = categories.stream().map(ListCategoryOutput::from).toList();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "algo";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 2;

        final var aQuery =
                new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var pagination =
                new Pagination<>(expectedPage, expectedPerPage, categories.size(), categories);


        when(this.categoryGateway.findAll(any())).thenReturn(pagination);

        final var actualOutput = this.useCase.execute(aQuery);

        Assertions.assertEquals(expectedPage, actualOutput.meta().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.meta().perPage());
        Assertions.assertEquals(expectedItemsCount, actualOutput.data().size());
        Assertions.assertTrue(expectedItems.size() == actualOutput.data().size() && expectedItems.containsAll(actualOutput.data()));


    }

}
