package com.helion.admin.catalog.application.genre.update;

import com.helion.admin.catalog.IntegrationTest;
import com.helion.admin.catalog.domain.category.Category;
import com.helion.admin.catalog.domain.category.CategoryGateway;
import com.helion.admin.catalog.domain.category.CategoryID;
import com.helion.admin.catalog.domain.exceptions.NotificationException;
import com.helion.admin.catalog.domain.genre.Genre;
import com.helion.admin.catalog.domain.genre.GenreGateway;
import com.helion.admin.catalog.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@IntegrationTest
public class UpdateGenreUseCaseIT {

    @Autowired
    private UpdateGenreUseCase useCase;

    @Autowired
    private GenreGateway genreGateway;

    @Autowired
    private CategoryGateway categoryGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void givenAValidCommand_whenCallsUpdateGenre_ShouldReturnGenreID(){
        final var filmes = categoryGateway.create(Category.newCategory("Filmes", null, true));
        final var aGenre = Genre.newGenre("Action", true);

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        aGenre.addCategories(List.of(filmes.getId()));
        genreGateway.create(aGenre);

        Assertions.assertEquals(1, genreRepository.count());
        final var expectedCategories = List.<CategoryID>of();
        final var expectedID = aGenre.getId();

        final var aCommand = UpdateGenreCommand.with(expectedID.getValue(), expectedName, expectedIsActive, asString(expectedCategories));


        final var actualOutput = useCase.execute(aCommand);

        Assertions.assertNotNull(actualOutput);

        Assertions.assertNotNull(actualOutput.id());

        final var actualGenre = genreRepository.findById(actualOutput.id()).get();
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertTrue(expectedCategories.size() == actualGenre.getCategoriesIDs().size()
                && expectedCategories.containsAll(actualGenre.getCategoriesIDs()));
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertTrue(actualGenre.getCreatedAt().isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidCommandWithInactiveGenre_whenCallsUpdateGenre_ShouldReturnGenreID(){
        final var aGenre =  genreGateway.create(Genre.newGenre("Action", true));
        final var expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();
        final var expectedID = aGenre.getId();

        final var aCommand = UpdateGenreCommand.with(expectedID.getValue(), expectedName, expectedIsActive, asString(expectedCategories));


        Assertions.assertEquals(true, aGenre.isActive());
        Assertions.assertNull(aGenre.getDeletedAt());
        final var actualOutput = useCase.execute(aCommand);

        Assertions.assertNotNull(actualOutput);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final var actualGenre = genreRepository.findById(actualOutput.id()).get();
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertTrue(expectedCategories.size() == actualGenre.getCategoriesIDs().size()
                && expectedCategories.containsAll(actualGenre.getCategoriesIDs()));
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNotNull(actualGenre.getDeletedAt());

    }

    @Test
    public void givenAValidCommandWithCategories_whenCallsUpdateGenre_ShouldReturnGenreID(){
        final var filmes = categoryGateway.create(Category.newCategory("Filmes", null, true));
        final var series = categoryGateway.create(Category.newCategory("Séries", null, true));

        final var aGenre = genreGateway.create(Genre.newGenre("Action", true));
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(
               filmes.getId(), series.getId()
        );
        final var expectedID = aGenre.getId();

        final var aCommand = UpdateGenreCommand.with(expectedID.getValue(), expectedName, expectedIsActive, asString(expectedCategories));

        final var actualOutput = useCase.execute(aCommand);

        Assertions.assertNotNull(actualOutput);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final var actualGenre = genreRepository.findById(actualOutput.id()).get();
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertTrue(expectedCategories.size() == actualGenre.getCategoriesIDs().size()
                && expectedCategories.containsAll(actualGenre.getCategoriesIDs()));
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertTrue(actualGenre.getCreatedAt().isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAinValidName_whenCallsUpdateGenre_ShouldReturnNotificationException(){
        final var aGenre = genreGateway.create(Genre.newGenre("Action", true));
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();
        final var expectedID = aGenre.getId();

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateGenreCommand.with(expectedID.getValue(), expectedName, expectedIsActive, asString(expectedCategories));

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());

        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    }

    @Test
    public void givenAinValidName_whenCallsUpdateGenreAndSomeCategoriesDoesNotExists_ShouldReturnNotificationException(){
        final var filmes = categoryGateway.create(Category.newCategory("Filmes", null, true));
        final var series = CategoryID.from("456");
        final var documentarios = CategoryID.from("789");

        final var aGenre = genreGateway.create(Genre.newGenre("Action", true));
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.of(
                filmes.getId(), series, documentarios
        );
        final var expectedID = aGenre.getId();

        final var expectedErrorMessageOne = "Some categories could not be found: 456, 789";
        final var expectedErrorMessageTwo = "'name' should not be null";
        final var expectedErrorCount = 2;

        final var aCommand = UpdateGenreCommand.with(expectedID.getValue(), expectedName, expectedIsActive, asString(expectedCategories));

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());

        Assertions.assertEquals(expectedErrorMessageOne, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorMessageTwo, actualException.getErrors().get(1).message());


    }
    private List<String> asString(List<CategoryID> categories) {
        return categories.stream()
                .map(CategoryID::getValue)
                .toList();
    }
}
