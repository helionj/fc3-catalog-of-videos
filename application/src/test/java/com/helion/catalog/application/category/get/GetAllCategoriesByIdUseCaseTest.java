package com.helion.catalog.application.category.get;

import com.helion.catalog.application.UseCaseTest;
import com.helion.catalog.domain.Fixture;
import com.helion.catalog.domain.category.Category;
import com.helion.catalog.domain.category.CategoryGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GetAllCategoriesByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private GetAllCategoriesByIdUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidIds_whenCallsGetAllById_shouldReturnIt() {
        final var categories = List.of(
                Fixture.Categories.aulas(), Fixture.Categories.lives()
        );

        final var expectedItems = categories.stream().map(GetAllCategoriesByIdUseCase.Output::new).toList();

        final var expectedIds = categories.stream().map(Category::id).collect(Collectors.toSet());



        when(this.categoryGateway.findAllById(any())).thenReturn(categories);

        final var actualOutput = this.useCase.execute(new GetAllCategoriesByIdUseCase.Input(expectedIds));

        Assertions.assertTrue(expectedItems.size() == actualOutput.size() && expectedItems.containsAll(actualOutput));

        verify(categoryGateway, times(1)).findAllById(any());

    }

    @Test
    public void givenANullIds_whenCallsGetAllById_shouldReturEmpty() {


        final var expectedItems = List.of();

        final Set<String> expectedIds = null;


        final var actualOutput = this.useCase.execute(new GetAllCategoriesByIdUseCase.Input(expectedIds));

        Assertions.assertTrue(actualOutput.isEmpty());

        verify(categoryGateway, times(0)).findAllById(any());
    }

}