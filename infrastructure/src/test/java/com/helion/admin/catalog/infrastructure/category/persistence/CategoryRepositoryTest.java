package com.helion.admin.catalog.infrastructure.category.persistence;

import com.helion.admin.catalog.domain.category.Category;
import com.helion.admin.catalog.infrastructure.MySQLGatewayTest;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

@MySQLGatewayTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void givenAnInvalidNullName_whenCallsSave_shouldReturnError() {
        final var expectedProperty = "name";
        final var expectedMessage = "not-null property references a null or transient value : com.helion.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity.name";
        final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var anEntity = CategoryJpaEntity.from(aCategory);
        anEntity.setName(null);
        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class,
                () ->  categoryRepository.saveAndFlush(anEntity));

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());
        Assertions.assertEquals(expectedProperty, actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessage, actualCause.getMessage());
    }

    @Test
    public void givenAnInvalidNullCreatedAt_whenCallsSave_shouldReturnError() {
        final var expectedProperty = "createdAt";
        final var expectedMessage = "not-null property references a null or transient value : com.helion.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity.createdAt";
        final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var anEntity = CategoryJpaEntity.from(aCategory);
        anEntity.setCreatedAt(null);
        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class,
                () ->  categoryRepository.saveAndFlush(anEntity));

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());
        Assertions.assertEquals(expectedProperty, actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessage, actualCause.getMessage());
    }

    @Test
    public void givenAnInvalidNullUpdatedAt_whenCallsSave_shouldReturnError() {
        final var expectedProperty = "updatedAt";
        final var expectedMessage = "not-null property references a null or transient value : com.helion.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity.updatedAt";
        final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var anEntity = CategoryJpaEntity.from(aCategory);
        anEntity.setUpdatedAt(null);
        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class,
                () ->  categoryRepository.saveAndFlush(anEntity));

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());
        Assertions.assertEquals(expectedProperty, actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessage, actualCause.getMessage());
    }

}
