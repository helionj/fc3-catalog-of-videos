package com.helion.admin.catalog.application.genre.delete;

import com.helion.admin.catalog.IntegrationTest;
import com.helion.admin.catalog.domain.genre.Genre;
import com.helion.admin.catalog.domain.genre.GenreGateway;
import com.helion.admin.catalog.domain.genre.GenreID;
import com.helion.admin.catalog.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.mockito.Mockito.times;

@IntegrationTest
public class DeleteGenreUseCaseIT {

    @Autowired
    private DeleteGenreUseCase useCase;

    @SpyBean
    private GenreGateway genreGateway;

    @Autowired
    private GenreRepository genreRepository;


    @Test
    public void givenAValidGenreID_whenCallsDeleteCategory_shouldBeDeleted(){
        final var aGenre = genreGateway.create(Genre.newGenre("Ação", true));

        final var expectedId = aGenre.getId();

        Assertions.assertEquals(1, genreRepository.count());

        Assertions.assertDoesNotThrow(() ->  useCase.execute(expectedId.getValue()));

        Assertions.assertEquals(0, genreRepository.count());
        Mockito.verify(genreGateway, times(1)).deleteById(expectedId);

    }

    @Test
    public void givenAinValidGenreID_whenCallsDeleteCategory_shouldBeOk(){

        genreGateway.create(Genre.newGenre("Ação", true));

        final var expectedId = GenreID.from("inalidID");
        Assertions.assertEquals(1, genreRepository.count());
        Assertions.assertDoesNotThrow(() ->  useCase.execute(expectedId.getValue()));
        Assertions.assertEquals(1, genreRepository.count());
        Mockito.verify(genreGateway, times(1)).deleteById(expectedId);

    }


}
