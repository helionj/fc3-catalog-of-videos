package com.helion.admin.catalog.domain.genre;

import com.helion.admin.catalog.domain.category.CategoryID;
import com.helion.admin.catalog.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class GenreTest {

    @Test
    public void givenAValidParams_whenCallNewGenre_shouldInstantiateAGenre() {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = 0;


        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        Assertions.assertNotNull(actualGenre);
        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories,actualGenre.getCategories().size());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAnInValidNullName_whenCallNewGenreAndValidate_shouldReceiveAnError() {
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";


        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            Genre.newGenre(expectedName, expectedIsActive);
        });

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnEmptyName_whenCallNewGenreAndValidate_shouldReceiveAnError() {
        final var expectedName = "";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";


        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            Genre.newGenre(expectedName, expectedIsActive);
        });

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidNameGreaterThan255_whenCallNewGenreAndValidate_shouldReceiveAnError() {
        final var expectedName = """
                É importante questionar o quanto o desafiador cenário globalizado afeta positivamente a 
                correta previsão do remanejamento dos quadros funcionais. Todas estas questões, devidamente 
                ponderadas, levantam dúvidas sobre se a revolução dos costumes representa uma abertura ponderada
                """;
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must between 3 and 255 characters";


        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            Genre.newGenre(expectedName, expectedIsActive);
        });

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAActiveGenre_whenCallDeactivate_ShouldReceiveOk(){
        final var expectedName = "Ação";
        final var expectedIsActive = false;


        final var aGenre = Genre.newGenre(expectedName, true);

        final var createdAt = aGenre.getCreatedAt();
        final var updatedAt = aGenre.getUpdatedAt();

        Assertions.assertEquals(true, aGenre.isActive());
        Assertions.assertNull(aGenre.getDeletedAt());

        aGenre.deactivate();
        Assertions.assertNotNull(aGenre.getId());
        Assertions.assertEquals(expectedName, aGenre.getName());
        Assertions.assertEquals(expectedIsActive, aGenre.isActive());
        Assertions.assertNotNull(aGenre.getCreatedAt());
        Assertions.assertEquals(aGenre.getCreatedAt(), createdAt);
        Assertions.assertTrue(aGenre.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNotNull(aGenre.getDeletedAt());

    }

    @Test
    public void givenAnInActiveGenre_whenCallActivate_ShouldReceiveOk(){
        final var expectedName = "Ação";
        final var expectedIsActive = true;


        final var aGenre = Genre.newGenre(expectedName, false);

        final var createdAt = aGenre.getCreatedAt();
        final var updatedAt = aGenre.getUpdatedAt();

        Assertions.assertEquals(false, aGenre.isActive());
        Assertions.assertNotNull(aGenre.getDeletedAt());

        aGenre.activate();
        Assertions.assertNotNull(aGenre.getId());
        Assertions.assertEquals(expectedName, aGenre.getName());
        Assertions.assertEquals(expectedIsActive, aGenre.isActive());
        Assertions.assertNotNull(aGenre.getCreatedAt());
        Assertions.assertEquals(aGenre.getCreatedAt(), createdAt);
        Assertions.assertTrue(aGenre.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(aGenre.getDeletedAt());
    }

    @Test
    public void givenAnInactiveGenre_whenCallUpdateWhithActivate_ShouldReturnGenreUpdated() {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(CategoryID.from("123"));


        final var actualGenre = Genre.newGenre("nome", false);

        final var createdAt = actualGenre.getCreatedAt();
        final var updatedAt = actualGenre.getUpdatedAt();

        Assertions.assertNotNull(actualGenre);
        Assertions.assertEquals(false, actualGenre.isActive());
        Assertions.assertNotNull(actualGenre.getDeletedAt());

        actualGenre.update(expectedName, expectedIsActive, expectedCategories);
        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertEquals(actualGenre.getCreatedAt(), createdAt);
        Assertions.assertTrue(actualGenre.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAActiveGenre_whenCallUpdateWhithDeactivate_ShouldReturnGenreUpdated() {
        final var expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = List.of(CategoryID.from("123"));


        final var actualGenre = Genre.newGenre("nome", true);

        final var createdAt = actualGenre.getCreatedAt();
        final var updatedAt = actualGenre.getUpdatedAt();

        Assertions.assertNotNull(actualGenre);
        Assertions.assertEquals(true, actualGenre.isActive());
        Assertions.assertNull(actualGenre.getDeletedAt());

        actualGenre.update(expectedName, expectedIsActive, expectedCategories);
        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertEquals(actualGenre.getCreatedAt(), createdAt);
        Assertions.assertTrue(actualGenre.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNotNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidGenre_whenCallUpdateWithEmptyName_ShouldReceiveNotificationException() {
        final var expectedName = "";
        final var expectedIsActive = false;
        final var expectedCategories = List.of(CategoryID.from("123"));
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var actualGenre = Genre.newGenre("nome", true);

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            actualGenre.update(expectedName, expectedIsActive, expectedCategories);
        });

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    }

    @Test
    public void givenAValidGenre_whenCallUpdateWithNullName_ShouldReceiveNotificationException() {
        final String expectedName = null;
        final var expectedIsActive = false;
        final var expectedCategories = List.of(CategoryID.from("123"));
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var actualGenre = Genre.newGenre("nome", true);

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            actualGenre.update(expectedName, expectedIsActive, expectedCategories);
        });

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    }

    @Test
    public void givenAValidGenre_whenCallUpdateWithNullCategories_ShouldReceiveOk() {
        final String expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = new ArrayList<CategoryID>();

        final var actualGenre = Genre.newGenre("nome", false);

        Assertions.assertDoesNotThrow(() -> {
            actualGenre.update(expectedName, expectedIsActive, expectedCategories);
        });

        final var createdAt = actualGenre.getCreatedAt();
        final var updatedAt = actualGenre.getUpdatedAt();

        Assertions.assertNotNull(actualGenre);
        Assertions.assertEquals(true, actualGenre.isActive());
        Assertions.assertNull(actualGenre.getDeletedAt());

        actualGenre.update(expectedName, expectedIsActive, null);
        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertEquals(actualGenre.getCreatedAt(), createdAt);
        Assertions.assertTrue(actualGenre.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidGenreWithEmptyCategories_whenCallAddACategory_ShouldReceiveOk() {
        final String expectedName = "Ação";
        final var expectedIsActive = true;
        final var seriesID = CategoryID.from("123");
        final var moviesID = CategoryID.from("456");
        final var expectedCategories = List.of(seriesID, moviesID);


        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);


        Assertions.assertEquals(0, actualGenre.getCategories().size());

        final var createdAt = actualGenre.getCreatedAt();
        final var updatedAt = actualGenre.getUpdatedAt();

        actualGenre.addCategory(seriesID);
        actualGenre.addCategory(moviesID);


        Assertions.assertEquals(2, actualGenre.getCategories().size());

        Assertions.assertNotNull(actualGenre);
        Assertions.assertEquals(true, actualGenre.isActive());
        Assertions.assertNull(actualGenre.getDeletedAt());

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertEquals(actualGenre.getCreatedAt(), createdAt);
        Assertions.assertTrue(actualGenre.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(actualGenre.getDeletedAt());

    }

    @Test
    public void givenAValidGenreWithTwoCategories_whenCallRemoveCategory_ShouldReceiveOk() {
        final String expectedName = "Ação";
        final var expectedIsActive = true;
        final var seriesID = CategoryID.from("123");
        final var moviesID = CategoryID.from("456");
        final var expectedCategories = List.of(moviesID);

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);
        actualGenre.update(expectedName, expectedIsActive, List.of(seriesID, moviesID));
        final var createdAt = actualGenre.getCreatedAt();
        final var updatedAt = actualGenre.getUpdatedAt();

        Assertions.assertEquals(2, actualGenre.getCategories().size());

        actualGenre.removeCategory(seriesID);

        Assertions.assertEquals(1, actualGenre.getCategories().size());



        Assertions.assertNotNull(actualGenre);
        Assertions.assertEquals(true, actualGenre.isActive());
        Assertions.assertNull(actualGenre.getDeletedAt());

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertEquals(actualGenre.getCreatedAt(), createdAt);
        Assertions.assertTrue(actualGenre.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(actualGenre.getDeletedAt());

    }

    @Test
    public void givenAnInvalidNullAsCategoryID_whenCallAddACategory_ShouldReceiveOk() {
        final String expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = new ArrayList<CategoryID>();

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);
        final var createdAt = actualGenre.getCreatedAt();
        final var updatedAt = actualGenre.getUpdatedAt();

        Assertions.assertEquals(0, actualGenre.getCategories().size());

        actualGenre.addCategory(null);

        Assertions.assertEquals(0, actualGenre.getCategories().size());



        Assertions.assertNotNull(actualGenre);
        Assertions.assertEquals(true, actualGenre.isActive());
        Assertions.assertNull(actualGenre.getDeletedAt());

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertEquals(actualGenre.getCreatedAt(), createdAt);
        Assertions.assertEquals(actualGenre.getUpdatedAt(), updatedAt);
        Assertions.assertNull(actualGenre.getDeletedAt());

    }

    @Test
    public void givenAValidGenreWithTwoCategories_whenCallRemoveCategoryWithNullCategory_ShouldReceiveOk() {
        final String expectedName = "Ação";
        final var expectedIsActive = true;
        final var seriesID = CategoryID.from("123");
        final var moviesID = CategoryID.from("456");
        final var expectedCategories = List.of(seriesID, moviesID);

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);
        actualGenre.update(expectedName, expectedIsActive, List.of(seriesID, moviesID));
        final var createdAt = actualGenre.getCreatedAt();
        final var updatedAt = actualGenre.getUpdatedAt();

        Assertions.assertEquals(2, actualGenre.getCategories().size());

        actualGenre.removeCategory(null);

        Assertions.assertEquals(2, actualGenre.getCategories().size());



        Assertions.assertNotNull(actualGenre);
        Assertions.assertEquals(true, actualGenre.isActive());
        Assertions.assertNull(actualGenre.getDeletedAt());
        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertEquals(actualGenre.getCreatedAt(), createdAt);
        Assertions.assertEquals(actualGenre.getUpdatedAt(),updatedAt);
        Assertions.assertNull(actualGenre.getDeletedAt());

    }





}
