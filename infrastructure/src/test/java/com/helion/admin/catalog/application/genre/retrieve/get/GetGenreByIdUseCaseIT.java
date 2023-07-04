package com.helion.admin.catalog.application.genre.retrieve.get;

import com.helion.admin.catalog.IntegrationTest;
import com.helion.admin.catalog.domain.category.Category;
import com.helion.admin.catalog.domain.category.CategoryGateway;
import com.helion.admin.catalog.domain.category.CategoryID;
import com.helion.admin.catalog.domain.exceptions.NotFoundException;
import com.helion.admin.catalog.domain.genre.Genre;
import com.helion.admin.catalog.domain.genre.GenreGateway;
import com.helion.admin.catalog.domain.genre.GenreID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static org.mockito.Mockito.times;

@IntegrationTest
public class GetGenreByIdUseCaseIT {
    @Autowired
    private GetGenreByIdUseCase useCase;

    @SpyBean
    private GenreGateway genreGateway;

    @SpyBean
    private CategoryGateway categoryGateway;


    @Test
    public void givenAnValidId_whenCallsGetGenreById_shouldReturnsAGenre(){


        final var expectedName = "Ação";
        final var expectedCategories = List.<CategoryID>of();
        final var isActive = true;

        final var aGenre = genreGateway.create(Genre.newGenre(expectedName, isActive));
        final var expectedId = aGenre.getId();

       ;
        final var actualGenre =  useCase.execute(expectedId.getValue());

        Assertions.assertEquals(expectedId.getValue(), actualGenre.id());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedCategories.size(), actualGenre.categories().size());
        Assertions.assertTrue(expectedCategories.containsAll(actualGenre.categories()));
        Assertions.assertEquals(isActive, actualGenre.isActive());
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.createdAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), actualGenre.updatedAt());
        Assertions.assertEquals(aGenre.getDeletedAt(), actualGenre.deletedAt());
        Assertions.assertEquals(GenreOutput.from(aGenre), actualGenre);

    }

    @Test
    public void givenAnValidId_whenCallsGetGenreByIdWithCategories_shouldReturnsAGenreWithCategories(){

        final var expectedName = "Ação";

        final var filmes = categoryGateway.create(Category.newCategory("Filmes", null, true));
        final var series = categoryGateway.create(Category.newCategory("Séries", null, true));
        final var expectedCategories = List.<CategoryID>of(
                filmes.getId(), series.getId()
        );
        final var isActive = true;
        final var aGenre = Genre.newGenre(expectedName, isActive);
        aGenre.addCategories(expectedCategories);
        genreGateway.create(aGenre);

        final var expectedId = aGenre.getId();

        final var actualGenre =  useCase.execute(expectedId.getValue());

        Assertions.assertEquals(expectedId.getValue(), actualGenre.id());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertTrue(asString(expectedCategories).size() == actualGenre.categories().size()
            && asString(expectedCategories).containsAll(actualGenre.categories()));
        Assertions.assertEquals(isActive, actualGenre.isActive());
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.createdAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), actualGenre.updatedAt());
        Assertions.assertEquals(aGenre.getDeletedAt(), actualGenre.deletedAt());
        Assertions.assertEquals(GenreOutput.from(aGenre), actualGenre);
        Mockito.verify(genreGateway, times(1)).findById(expectedId);

    }

    @Test
    public void givenAiNValidId_whenCallsGetGenreById_shouldReturnsNotFound(){

        final var expectedId = GenreID.from("123");
        final var expectedErrorMessage = "Genre with ID 123 was not found";

        final var actualException = Assertions.assertThrows(NotFoundException.class,
                () -> useCase.execute(expectedId.getValue()));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    private List<String> asString(List<CategoryID> categories) {
        return categories.stream()
                .map(CategoryID::getValue)
                .toList();
    }
}
