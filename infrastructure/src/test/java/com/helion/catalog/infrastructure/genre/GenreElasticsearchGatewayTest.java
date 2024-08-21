package com.helion.catalog.infrastructure.genre;

import com.helion.catalog.AbstractElasticsearchTest;
import com.helion.catalog.domain.Fixture;
import com.helion.catalog.domain.genre.Genre;
import com.helion.catalog.domain.genre.GenreSearchQuery;
import com.helion.catalog.infrastructure.genre.persistence.GenreDocument;
import com.helion.catalog.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.shaded.org.apache.commons.lang3.StringUtils;

import java.util.Set;

public class GenreElasticsearchGatewayTest extends AbstractElasticsearchTest {

    @Autowired
    private GenreElasticsearchGateway genreGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void testInjection(){
        Assertions.assertNotNull(this.genreGateway);
        Assertions.assertNotNull(this.genreRepository);
    }


    @Test
    public void givenValidGenre_whenCallsSave_shouldPersistIt(){
        final var business = Fixture.Genres.marketing();

        final var actualOutput = this.genreGateway.save(business);

        Assertions.assertEquals(business, actualOutput);

        final var  actualGenre = genreRepository.findById(business.id()).get();

        Assertions.assertEquals(business.id(), actualGenre.id());
        Assertions.assertEquals(business.name(), actualGenre.name());
        Assertions.assertEquals(business.categories(), actualGenre.categories());
        Assertions.assertEquals(business.active(), actualGenre.active());
        Assertions.assertEquals(business.createdAt(), actualGenre.createdAt());
        Assertions.assertEquals(business.updatedAt(), actualGenre.updatedAt());
    }

    @Test
    public void givenValidId_whenCallsDeleteById_shouldDeleteIt(){
        final var business = Fixture.Genres.business();
        this.genreRepository.save(GenreDocument.from(business));

        final var expectedId = business.id();
        Assertions.assertTrue(this.genreRepository.existsById(expectedId));

        this.genreGateway.deleteById(expectedId);

        Assertions.assertFalse(this.genreRepository.existsById(expectedId));
    }

    @Test
    public void givenInValidId_whenCallsDeleteById_shouldBeOk(){
        final var expectedId = "any";

        Assertions.assertDoesNotThrow(() -> this.genreGateway.deleteById(expectedId));

    }

    @Test
    public void givenValidId_whenCallsFindById_shouldRetrieveIt(){
        final var business = Fixture.Genres.business();
        this.genreRepository.save(GenreDocument.from(business));

        final var expectedId = business.id();
        Assertions.assertTrue(this.genreRepository.existsById(expectedId));

        final var actualGenre = this.genreGateway.findById(expectedId).get();

        Assertions.assertEquals(business.id(), actualGenre.id());
        Assertions.assertEquals(business.name(), actualGenre.name());
        Assertions.assertEquals(business.categories(), actualGenre.categories());
        Assertions.assertEquals(business.active(), actualGenre.active());
        Assertions.assertEquals(business.createdAt(), actualGenre.createdAt());
        Assertions.assertEquals(business.updatedAt(), actualGenre.updatedAt());
    }

    @Test
    public void givenInValidId_whenCallsFindById_shouldBeEmpty(){
        final var expectedId = "any";

        final var actualOutput = this.genreGateway.findById(expectedId);

        Assertions.assertTrue(actualOutput.isEmpty());

    }

    @Test
    public void givenEmptyGenres_whenCallsFindAll_shouldReturnEmptyList(){

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;
        final var expectedCategories = Set.<String>of();

        final var aQuery = new GenreSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection, expectedCategories);

        final var actualOutput =this.genreGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualOutput.meta().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.meta().perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.meta().total());
        Assertions.assertEquals(expectedTotal, actualOutput.data().size());

    }

    @ParameterizedTest
    @CsvSource({
            "mar,0,10,1,1,Marketing",
            "bus,0,10,1,1,Business",

    })
    public void givenValidTerm_whenCallsFindAll_shouldReturnFilteredList(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final int expectedTotal,
            final String expectedName

    ){


        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var  expectedCategories = Set.<String>of();
        mockGenres();
        final var aQuery = new GenreSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection, expectedCategories);

        final var actualOutput =this.genreGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualOutput.meta().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.meta().perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.meta().total());
        Assertions.assertEquals(expectedItemsCount, actualOutput.data().size());
        Assertions.assertEquals(expectedName, actualOutput.data().get(0).name());

    }

    @ParameterizedTest
    @CsvSource({
            "c123,0,10,1,1,Marketing",
            "c456,0,10,1,1,Technology",
            ",0,10,3,3,Business",

    })
    public void givenValidCategories_whenCallsFindAll_shouldReturnFilteredList(
            final String categories,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final int expectedTotal,
            final String expectedName
        ){

        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var  expectedCategories = categories == null ? Set.<String>of() : Set.<String>of(categories);
        mockGenres();
        final var aQuery = new GenreSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection, expectedCategories);

        final var actualOutput =this.genreGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualOutput.meta().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.meta().perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.meta().total());
        Assertions.assertEquals(expectedItemsCount, actualOutput.data().size());
        Assertions.assertEquals(expectedName, actualOutput.data().get(0).name());

    }


    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,3,3,Business",
            "name,desc,0,10,3,3,Technology",
            "created_at,asc,0,10,3,3,Technology",
            "created_at,desc,0,10,3,3,Marketing"

    })
    public void givenValidSortAndDirection_whenCallsFindAll_shouldReturnSortedList(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final int expectedTotal,
            final String expectedName
    ){

        final String expectedTerms = "";
        final var  expectedCategories = Set.<String>of();


        mockGenres();
        final var aQuery = new GenreSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection, expectedCategories);

        final var actualOutput =this.genreGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualOutput.meta().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.meta().perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.meta().total());
        Assertions.assertEquals(expectedItemsCount, actualOutput.data().size());
        Assertions.assertEquals(expectedName, actualOutput.data().get(0).name());

    }

    @ParameterizedTest
    @CsvSource({
            "0,1,1,3,Business",
            "1,1,1,3,Marketing",
            "2,1,1,3,Technology",
            "3,1,0,3, "

    })
    public void givenValidPage_whenCallsFindAll_shouldReturnPagedList(

            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final int expectedTotal,
            final String expectedName
        ) {

        final String expectedTerms = "";
        final String expectedSort = "name";
        final String expectedDirection = "asc";
        final var  expectedCategories = Set.<String>of();


        mockGenres();
        final var aQuery = new GenreSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection, expectedCategories);


        final var actualOutput = this.genreGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualOutput.meta().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.meta().perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.meta().total());
        Assertions.assertEquals(expectedItemsCount, actualOutput.data().size());

        if (StringUtils.isNotEmpty(expectedName)) {
            Assertions.assertEquals(expectedName, actualOutput.data().get(0).name());
        }
    }

    @Test
    public void givenValidIds_whenCallsFindAllById_shouldReturnElements(){


        final var business = this.genreRepository.save(GenreDocument.from(Fixture.Genres.business()));
        final var tech =this.genreRepository.save(GenreDocument.from(Fixture.Genres.tech()));

        final var expectedIds = Set.of(business.id(), tech.id());
        final var expectedSize = 2;

        final var actualOutput =this.genreGateway.findAllById(expectedIds);

        Assertions.assertEquals(expectedSize, actualOutput.size());
        Assertions.assertTrue(expectedIds.containsAll(actualOutput.stream().map(Genre::id).toList()));


    }

    @Test
    public void givenNullIds_whenCallsFindAllById_shouldReturnEmpty(){


        final Set<String> expectedIds = null;

        final var actualOutput =this.genreGateway.findAllById(expectedIds);
        Assertions.assertTrue(actualOutput.isEmpty());


    }
    @Test
    public void givenEmptyIds_whenCallsFindAllById_shouldReturnEmpty(){


        final Set<String> expectedIds = Set.of();

        final var actualOutput =this.genreGateway.findAllById(expectedIds);
        Assertions.assertTrue(actualOutput.isEmpty());


    }



    private void mockGenres(){
        this.genreRepository.save(GenreDocument.from(Fixture.Genres.tech()));
        this.genreRepository.save(GenreDocument.from(Fixture.Genres.business()));
        this.genreRepository.save(GenreDocument.from(Fixture.Genres.marketing()));

    }

}
