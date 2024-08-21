package com.helion.catalog.domain.category;

import com.helion.catalog.domain.UnitTest;
import com.helion.catalog.domain.exceptions.DomainException;
import com.helion.catalog.domain.utils.InstantUtils;
import com.helion.catalog.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class CategoryTest extends UnitTest {

    @Test
    public void givenAValidParamsWhenCallWith_thenInstantiateAnewCategory() {
       final var expectedName = "Filmes";
       final var expectedDescription = "A categoria mais assistida";
       final var expectedIsActive = true;
       final var expectedId = UUID.randomUUID().toString();
       final var expectedDates = InstantUtils.now();
       final var actualCategory = Category.with(expectedId,
               expectedName, expectedDescription, expectedIsActive, expectedDates, expectedDates, null);

       Assertions.assertNotNull(actualCategory);
       Assertions.assertEquals(expectedId, actualCategory.id());
       Assertions.assertEquals(expectedName, actualCategory.name());
       Assertions.assertEquals(expectedDescription, actualCategory.description());
       Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
       Assertions.assertEquals(expectedDates, actualCategory.createdAt());
       Assertions.assertEquals(expectedDates, actualCategory.updatedAt());
       Assertions.assertNull(actualCategory.deletedAt());

    }
    @Test
    public void givenAValidParamsWhenCallWithCategory_thenInstantiateAnewCategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedId = UUID.randomUUID().toString();
        final var expectedDates = InstantUtils.now();
        final var aCategory = Category.with(expectedId,
                expectedName, expectedDescription, expectedIsActive, expectedDates, expectedDates, null);
        final var actualCategory = Category.with(aCategory);

        Assertions.assertNotNull(actualCategory);
        Assertions.assertEquals(aCategory.id(), actualCategory.id());
        Assertions.assertEquals(aCategory.name(), actualCategory.name());
        Assertions.assertEquals(aCategory.description(), actualCategory.description());
        Assertions.assertEquals(aCategory.isActive(), actualCategory.isActive());
        Assertions.assertEquals(aCategory.createdAt(), actualCategory.createdAt());
        Assertions.assertEquals(aCategory.updatedAt(), actualCategory.updatedAt());
        Assertions.assertNull(actualCategory.deletedAt());

    }

    @Test
    public void givenAnInvalidNullNameWhenCallWithAndValidate_thenShouldReturnAnException() {
        final String expectedName = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

       final var expectedDescription = "A categoria mais assistida";
       final var expectedIsActive = true;
       final var expectedId = UUID.randomUUID().toString();
       final var expectedDates = InstantUtils.now();
       final var actualCategory = Category.with(expectedId,
               expectedName, expectedDescription, expectedIsActive, expectedDates, expectedDates, null);

        final var actualException = Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());


    }


    @Test
    public void givenAnInvalidEmptyNameWhenCallWithAndValidate_thenShouldReturnAnException() {
        final String expectedName = " ";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedId = UUID.randomUUID().toString();
        final var expectedDates = InstantUtils.now();
        final var actualCategory = Category.with(expectedId,
                expectedName, expectedDescription, expectedIsActive, expectedDates, expectedDates, null);

        final var actualException = Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());


    }

    @Test
    public void givenAnInvalidNullIdWhenCallWithAndValidate_thenShouldReturnAnException() {
        final String expectedId = null;

        final var expectedName = "Filmes";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'id' should not be empty";

        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var expectedDates = InstantUtils.now();
        final var actualCategory = Category.with(expectedId,
                expectedName, expectedDescription, expectedIsActive, expectedDates, expectedDates, null);

        final var actualException = Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());


    }


    @Test
    public void givenAnInvalidEmptyIdWhenCallWithAndValidate_thenShouldReturnAnException() {
        final var expectedId = " ";
        final var expectedName = "Filmes";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'id' should not be empty";

        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var expectedDates = InstantUtils.now();
        final var actualCategory = Category.with(expectedId,
                expectedName, expectedDescription, expectedIsActive, expectedDates, expectedDates, null);

        final var actualException = Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());


    }





}
