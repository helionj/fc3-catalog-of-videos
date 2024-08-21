package com.helion.catalog.domain.genre;

import com.helion.catalog.domain.UnitTest;
import com.helion.catalog.domain.exceptions.DomainException;
import com.helion.catalog.domain.utils.InstantUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

public class GenreTest extends UnitTest {

    @Test
    public void givenAValidParamsWhenCallWith_thenInstantiateAnewGenre() {
        final var expectedName = "Business";
        final var expectedIsActive = true;
        final var expectedCategories = Set.of("c1", "c2");
        final var expectedId = UUID.randomUUID().toString();
        final var expectedDates = InstantUtils.now();
        final var actualGenre = Genre.with(expectedId,
                expectedName, expectedCategories, expectedIsActive, expectedDates, expectedDates, null);

        Assertions.assertNotNull(actualGenre);
        Assertions.assertEquals(expectedId, actualGenre.id());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedCategories, actualGenre.categories());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedDates, actualGenre.createdAt());
        Assertions.assertEquals(expectedDates, actualGenre.updatedAt());
        Assertions.assertNull(actualGenre.deletedAt());

    }
    @Test
    public void givenAValidParamsWhenCallWithGenre_thenInstantiateAnewGenre() {
        final var expectedName = "Business";
        final var expectedIsActive = true;
        final var expectedCategories = Set.of("c1", "c2");
        final var expectedId = UUID.randomUUID().toString();
        final var expectedDates = InstantUtils.now();
        final var aGenre = Genre.with(expectedId,
                expectedName, expectedCategories, expectedIsActive, expectedDates, expectedDates, null);
        final var actualGenre = Genre.with(aGenre);

        Assertions.assertNotNull(actualGenre);
        Assertions.assertEquals(aGenre.id(), actualGenre.id());
        Assertions.assertEquals(aGenre.name(), actualGenre.name());
        Assertions.assertEquals(aGenre.categories(), actualGenre.categories());
        Assertions.assertEquals(aGenre.isActive(), actualGenre.isActive());
        Assertions.assertEquals(aGenre.createdAt(), actualGenre.createdAt());
        Assertions.assertEquals(aGenre.updatedAt(), actualGenre.updatedAt());
        Assertions.assertNull(actualGenre.deletedAt());

    }

    @Test
    public void givenAnInvalidNullNameWhenCallWithAndValidate_thenShouldReturnAnException() {
        final String expectedName = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedCategories = Set.of("c1", "c2");
        final var expectedIsActive = true;
        final var expectedId = UUID.randomUUID().toString();
        final var expectedDates = InstantUtils.now();

        final var actualException =
                Assertions.assertThrows(DomainException.class, () -> Genre.with(expectedId,
                expectedName, expectedCategories, expectedIsActive, expectedDates, expectedDates, null));
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());


    }


    @Test
    public void givenAnInvalidEmptyNameWhenCallWithAndValidate_thenShouldReturnAnException() {
        final String expectedName = " ";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedCategories = Set.of("c1", "c2");
        final var expectedIsActive = true;
        final var expectedId = UUID.randomUUID().toString();
        final var expectedDates = InstantUtils.now();


        final var actualException =
                Assertions.assertThrows(DomainException.class, () -> Genre.with(expectedId,
                expectedName, expectedCategories, expectedIsActive, expectedDates, expectedDates, null));
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());


    }

    @Test
    public void givenAnInvalidNullIdWhenCallWithAndValidate_thenShouldReturnAnException() {
        final String expectedId = null;

        final var expectedName = "Business";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'id' should not be empty";

        final var expectedCategories = Set.of("c1", "c2");

        final var expectedIsActive = true;

        final var expectedDates = InstantUtils.now();

        final var actualException = Assertions.assertThrows(DomainException.class, () -> Genre.with(expectedId,
                expectedName, expectedCategories, expectedIsActive, expectedDates, expectedDates, null));
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());


    }


    @Test
    public void givenAnInvalidEmptyIdWhenCallWithAndValidate_thenShouldReturnAnException() {
        final var expectedId = " ";
        final var expectedName = "Business";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'id' should not be empty";

        final var expectedCategories = Set.of("c1", "c2");

        final var expectedIsActive = true;

        final var expectedDates = InstantUtils.now();

        final var actualException =
                Assertions.assertThrows(DomainException.class, () -> Genre.with(expectedId,
                        expectedName, expectedCategories, expectedIsActive, expectedDates, expectedDates, null));
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());


    }
    @Test
    public void givenNullCategoriesWhenCallWith_thenShouldInstantiateAGenreWithNullCategories() {
        final var expectedId =  UUID.randomUUID().toString();
        final var expectedName = "Business";

        Set<String> expectedCategories = null;

        final var expectedIsActive = true;

        final var expectedDates = InstantUtils.now();
        final var actualGenre = Genre.with(expectedId,
                expectedName, expectedCategories, expectedIsActive, expectedDates, expectedDates,null);

        Assertions.assertNotNull(actualGenre);
        Assertions.assertEquals(expectedId, actualGenre.id());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertTrue(actualGenre.categories().isEmpty());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedDates, actualGenre.createdAt());
        Assertions.assertEquals(expectedDates, actualGenre.updatedAt());
        Assertions.assertNull(actualGenre.deletedAt());


    }
}
