package com.helion.admin.catalog.application.category.delete;

import com.helion.admin.catalog.IntegrationTest;
import com.helion.admin.catalog.domain.category.Category;
import com.helion.admin.catalog.domain.category.CategoryGateway;
import com.helion.admin.catalog.domain.category.CategoryID;
import com.helion.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.helion.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@IntegrationTest
public class DeleteCategoryUseCaseTestIT {

    @Autowired
    private DeleteCategoryUseCase useCase;
    @Autowired
    private CategoryRepository categoryRepository;
    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    public void givenAnValidId_whenCallsDeleteCategory_shouldBeOK(){
        final var aCategory = Category.newCategory("Movies", "Os filmes mais vistos", true);

        Assertions.assertEquals(0, categoryRepository.count());
        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));
        Assertions.assertEquals(1, categoryRepository.count());

        final var expectedId = aCategory.getId();

        Assertions.assertDoesNotThrow(() ->  useCase.execute(expectedId.getValue()));
        Assertions.assertEquals(0, categoryRepository.count());


    }

    @Test
    public void givenAiNValidId_whenCallsDeleteCategory_shouldBeOK(){
        final var expectedId = CategoryID.from("123");

        Assertions.assertDoesNotThrow(() ->  useCase.execute(expectedId.getValue()));

        Mockito.verify(categoryGateway, times(1)).deleteById(expectedId);
    }

   @Test
    public void givenAnValidId_whenGatewayThrowsException_shouldReturnsException(){
        final var aCategory = Category.newCategory("Movies", null, true);

        final var expectedId = aCategory.getId();
        doThrow(new IllegalStateException("Gateway Error")).when(categoryGateway).deleteById(eq(expectedId));
        Assertions.assertThrows(IllegalStateException.class, () ->  useCase.execute(expectedId.getValue()));

        Mockito.verify(categoryGateway, times(1)).deleteById(expectedId);
    }
}
