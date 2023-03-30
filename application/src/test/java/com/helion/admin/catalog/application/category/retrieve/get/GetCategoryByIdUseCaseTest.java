package com.helion.admin.catalog.application.category.retrieve.get;

import com.helion.admin.catalog.application.UseCaseTest;
import com.helion.admin.catalog.domain.category.Category;
import com.helion.admin.catalog.domain.category.CategoryGateway;
import com.helion.admin.catalog.domain.category.CategoryID;
import com.helion.admin.catalog.domain.exceptions.NotFoundException;
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

public class GetCategoryByIdUseCaseTest extends UseCaseTest {
    @InjectMocks
    private DefaultGetCategoryByIdUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(categoryGateway);
    }

    @Test
    public void givenAnValidId_whenCallsGetCategoryById_shouldReturnsACategory(){

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var isActive = true;
        final var aCategory = Category.newCategory(expectedName, expectedDescription, isActive);
        final var expectedId = aCategory.getId();

        when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(aCategory.clone()));
        final var actualCategory =  useCase.execute(expectedId.getValue());

        Assertions.assertEquals(expectedId, actualCategory.id());
        Assertions.assertEquals(expectedName, actualCategory.name());
        Assertions.assertEquals(expectedDescription, actualCategory.description());
        Assertions.assertEquals(isActive, actualCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.createdAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), actualCategory.updatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(), actualCategory.deletedAt());
        Assertions.assertEquals(CategoryOutput.from(aCategory), actualCategory);
        Mockito.verify(categoryGateway, times(1)).findById(expectedId);

    }

    @Test
    public void givenAiNValidId_whenCallsGetCategoryById_shouldReturnsNotFound(){

        final var expectedId = CategoryID.from("123");
        final var expectedErrorMessage = "Category with ID 123 was not found";
        when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.empty());
        final var actualException = Assertions.assertThrows(NotFoundException.class,
                () -> useCase.execute(expectedId.getValue()));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenAnValidId_whenGatewayThrowsException_shouldReturnsException(){
        final var aCategory = Category.newCategory("Movies", null, true);
        final var expectedErrorMessage = "Gateway Error";
        final var expectedId = aCategory.getId();
        when(categoryGateway.findById(eq(expectedId))).thenThrow(new IllegalStateException("Gateway Error"));

        final var actualException = Assertions.assertThrows(IllegalStateException.class, () ->  useCase.execute(expectedId.getValue()));
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
        Mockito.verify(categoryGateway, times(1)).findById(expectedId);

    }
}
