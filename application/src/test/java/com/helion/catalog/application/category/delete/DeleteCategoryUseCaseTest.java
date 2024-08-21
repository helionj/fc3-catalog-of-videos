package com.helion.catalog.application.category.delete;

import com.helion.catalog.application.UseCaseTest;
import com.helion.catalog.domain.Fixture;
import com.helion.catalog.domain.category.CategoryGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class DeleteCategoryUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DeleteCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Test
    public void givenValidId_whenCallsDelete_shouldBeOk(){

        final var aulas = Fixture.Categories.aulas();
        final var expectedId = aulas.id();

        doNothing().when(categoryGateway).deleteById(anyString());

        Assertions.assertDoesNotThrow(() -> this.useCase.execute(expectedId));

        verify(this.categoryGateway, times(1)).deleteById(eq(expectedId));
    }

    @Test
    public void givenInValidId_whenCallsDelete_shouldBeOk(){

        final String expectedId = null;

        //doNothing().when(categoryGateway).deleteById(anyString());

        Assertions.assertDoesNotThrow(() -> this.useCase.execute(expectedId));

        verify(this.categoryGateway, times(0)).deleteById(eq(expectedId));
    }

}
