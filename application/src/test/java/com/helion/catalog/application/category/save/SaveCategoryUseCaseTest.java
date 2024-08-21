package com.helion.catalog.application.category.save;

import com.helion.catalog.application.UseCaseTest;
import com.helion.catalog.domain.Fixture;
import com.helion.catalog.domain.category.Category;
import com.helion.catalog.domain.category.CategoryGateway;
import com.helion.catalog.domain.exceptions.DomainException;
import com.helion.catalog.domain.utils.InstantUtils;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class SaveCategoryUseCaseTest extends UseCaseTest {

    @InjectMocks
    private SaveCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidCategory_whenCallsSave_shouldPersistIt() {

        final var aCategory = Fixture.Categories.aulas();

        Mockito.when(categoryGateway.save(any())).thenAnswer(returnsFirstArg());

        this.useCase.execute(aCategory);

        verify(categoryGateway, times(1)).save(aCategory);
    }

    @Test
    public void givenANullCategory_whenCallsSave_shouldReturnsError() {

        final var expectedErrorCount = 1;
        final var expectedMessage = "'aCategory' cannot be null";
        final Category aCategory = null;

        final var actualError = assertThrows(DomainException.class, () -> this.useCase.execute(aCategory));

        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedMessage, actualError.getErrors().get(0).message());
        verify(categoryGateway, times(0)).save(aCategory);
    }

    @Test
    public void givenInValidName_whenCallsSave_shouldReturnError() {
        final var expectedErrorCount = 1;
        final var expectedMessage = "'name' should not be empty";
        final var aCategory = Category.with(
                UUID.randomUUID().toString().replace("-", ""),
                " ",
                "Some Description",
                true,
                InstantUtils.now(),
                InstantUtils.now(),
                null
        );

        final var actualError = assertThrows(DomainException.class, () -> this.useCase.execute(aCategory));

        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedMessage, actualError.getErrors().get(0).message());
        verify(categoryGateway, times(0)).save(aCategory);
    }

    @Test
    public void givenInValidId_whenCallsSave_shouldReturnError() {
        final var expectedErrorCount = 1;
        final var expectedMessage = "'id' should not be empty";
        final var aCategory = Category.with(
                " ",
                "Movies",
                "Some Description",
                true,
                InstantUtils.now(),
                InstantUtils.now(),
                null
        );

        final var actualError = assertThrows(DomainException.class, () -> this.useCase.execute(aCategory));

        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedMessage, actualError.getErrors().get(0).message());
        verify(categoryGateway, times(0)).save(aCategory);
    }

}
