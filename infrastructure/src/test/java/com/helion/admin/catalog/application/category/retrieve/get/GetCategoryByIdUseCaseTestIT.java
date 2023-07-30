package com.helion.admin.catalog.application.category.retrieve.get;

import com.helion.admin.catalog.IntegrationTest;
import com.helion.admin.catalog.domain.category.Category;
import com.helion.admin.catalog.domain.category.CategoryGateway;
import com.helion.admin.catalog.domain.category.CategoryID;
import com.helion.admin.catalog.domain.exceptions.DomainException;
import com.helion.admin.catalog.domain.exceptions.NotFoundException;
import com.helion.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.helion.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Arrays;

import static org.mockito.Mockito.times;

@IntegrationTest
public class GetCategoryByIdUseCaseTestIT {

    @Autowired
    private GetCategoryByIdUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    public void givenAnValidId_whenCallsGetCategoryById_shouldReturnsACategory(){

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var isActive = true;
        final var aCategory = Category.newCategory(expectedName, expectedDescription, isActive);
        final var expectedId = aCategory.getId();

        Assertions.assertEquals(0, categoryRepository.count());
        save(aCategory);
        Assertions.assertEquals(1, categoryRepository.count());
        final var actualCategory =  useCase.execute(expectedId.getValue());

        Assertions.assertEquals(expectedId, actualCategory.id());
        Assertions.assertEquals(expectedName, actualCategory.name());
        Assertions.assertEquals(expectedDescription, actualCategory.description());
        Assertions.assertEquals(isActive, actualCategory.isActive());
        //Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.createdAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), actualCategory.updatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(), actualCategory.deletedAt());
        Assertions.assertEquals(CategoryOutput.from(aCategory), actualCategory);
        Mockito.verify(categoryGateway, times(1)).findById(expectedId);

    }

    @Test
    public void givenAiNValidId_whenCallsGetCategoryById_shouldReturnsNotFound(){

        final var expectedId = CategoryID.from("123");
        final var expectedErrorMessage = "Category with ID 123 was not found";

        final var actualException = Assertions.assertThrows(NotFoundException.class,
                () -> useCase.execute(expectedId.getValue()));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenAnValidId_whenGatewayThrowsException_shouldReturnsException(){
        final var aCategory = Category.newCategory("Movies", null, true);
        final var expectedErrorMessage = "Gateway Error";
        final var expectedId = aCategory.getId();

        Mockito.doThrow(new IllegalStateException("Gateway Error")).when(categoryGateway).findById(expectedId);

        final var actualException = Assertions.assertThrows(IllegalStateException.class, () ->  useCase.execute(expectedId.getValue()));
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
        Mockito.verify(categoryGateway, times(1)).findById(expectedId);

    }

    private void save(final Category... aCategory) {
        categoryRepository.saveAllAndFlush(
                Arrays.stream(aCategory)
                        .map(CategoryJpaEntity::from)
                        .toList()
        );
    }

}
