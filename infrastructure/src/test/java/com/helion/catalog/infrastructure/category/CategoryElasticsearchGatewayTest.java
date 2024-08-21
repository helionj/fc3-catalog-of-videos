package com.helion.catalog.infrastructure.category;

import com.helion.catalog.AbstractElasticsearchTest;
import com.helion.catalog.domain.Fixture;
import com.helion.catalog.domain.category.Category;
import com.helion.catalog.domain.category.CategorySearchQuery;
import com.helion.catalog.infrastructure.category.persistence.CategoryDocument;
import com.helion.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.shaded.org.apache.commons.lang3.StringUtils;

import java.util.Set;

public class CategoryElasticsearchGatewayTest extends AbstractElasticsearchTest {

    @Autowired
    private CategoryElasticsearchGateway categoryGateway;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testInjection(){
        Assertions.assertNotNull(this.categoryGateway);
        Assertions.assertNotNull(this.categoryRepository);
    }

    @Test
    public void givenValidCategory_whenCallsSave_shouldPersistIt(){
        final var aulas = Fixture.Categories.aulas();

        final var actualOutput = this.categoryGateway.save(aulas);

        Assertions.assertEquals(aulas, actualOutput);

        final var  actualCategory = categoryRepository.findById(aulas.id()).get();

        Assertions.assertEquals(aulas.id(), actualCategory.id());
        Assertions.assertEquals(aulas.name(), actualCategory.name());
        Assertions.assertEquals(aulas.description(), actualCategory.description());
        Assertions.assertEquals(aulas.isActive(), actualCategory.active());
        Assertions.assertEquals(aulas.createdAt(), actualCategory.createdAt());
        Assertions.assertEquals(aulas.updatedAt(), actualCategory.updatedAt());
        Assertions.assertEquals(aulas.deletedAt(), actualCategory.deletedAt());
    }
    @Test
    public void givenValidId_whenCallsDeleteById_shouldDeleteIt(){
        final var aulas = Fixture.Categories.aulas();
        this.categoryRepository.save(CategoryDocument.from(aulas));

        final var expectedId = aulas.id();
        Assertions.assertTrue(this.categoryRepository.existsById(expectedId));

        this.categoryGateway.deleteById(expectedId);

        Assertions.assertFalse(this.categoryRepository.existsById(expectedId));
    }

    @Test
    public void givenInValidId_whenCallsDeleteById_shouldBeOk(){
        final var expectedId = "any";

        Assertions.assertDoesNotThrow(() -> this.categoryGateway.deleteById(expectedId));

    }

    @Test
    public void givenValidId_whenCallsFindById_shouldRetrieveIt(){
        final var talks = Fixture.Categories.aulas();
        this.categoryRepository.save(CategoryDocument.from(talks));

        final var expectedId = talks.id();
        Assertions.assertTrue(this.categoryRepository.existsById(expectedId));

        final var actualCategory = this.categoryGateway.findById(expectedId).get();

        Assertions.assertEquals(talks.id(), actualCategory.id());
        Assertions.assertEquals(talks.name(), actualCategory.name());
        Assertions.assertEquals(talks.description(), actualCategory.description());
        Assertions.assertEquals(talks.isActive(), actualCategory.isActive());
        Assertions.assertEquals(talks.createdAt(), actualCategory.createdAt());
        Assertions.assertEquals(talks.updatedAt(), actualCategory.updatedAt());
        Assertions.assertEquals(talks.deletedAt(), actualCategory.deletedAt());
    }

    @Test
    public void givenInValidId_whenCallsFindById_shouldBeEmpty(){
        final var expectedId = "any";

        final var actualOutput = this.categoryGateway.findById(expectedId);

        Assertions.assertTrue(actualOutput.isEmpty());

    }

    @Test
    public void givenEmptyCategories_whenCallsFindAll_shouldReturnEmptyList(){

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var aQuery = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualOutput =this.categoryGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualOutput.meta().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.meta().perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.meta().total());
        Assertions.assertEquals(expectedTotal, actualOutput.data().size());

    }

    @ParameterizedTest
    @CsvSource({
        "aul,0,10,1,1,Aulas",
        "liv,0,10,1,1,Lives",

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

        mockCategories();
        final var aQuery = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualOutput =this.categoryGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualOutput.meta().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.meta().perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.meta().total());
        Assertions.assertEquals(expectedItemsCount, actualOutput.data().size());
        Assertions.assertEquals(expectedName, actualOutput.data().get(0).name());

    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,3,3,Aulas",
            "name,desc,0,10,3,3,Talks",
            "created_at,asc,0,10,3,3,Aulas",
            "created_at,desc,0,10,3,3,Lives"

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


        mockCategories();
        final var aQuery = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualOutput =this.categoryGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualOutput.meta().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.meta().perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.meta().total());
        Assertions.assertEquals(expectedItemsCount, actualOutput.data().size());
        Assertions.assertEquals(expectedName, actualOutput.data().get(0).name());

    }

    @ParameterizedTest
    @CsvSource({
            "0,1,1,3,Aulas",
            "1,1,1,3,Lives",
            "2,1,1,3,Talks",
            "3,1,0,3, "

    })
    public void givenValidPage_whenCallsFindAll_shouldReturnPagedList(

            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final int expectedTotal,
            final String expectedName
    ){

        final String expectedTerms = "";
        final String expectedSort = "name";
        final String expectedDirection = "asc";

        mockCategories();
        final var aQuery = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualOutput =this.categoryGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualOutput.meta().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.meta().perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.meta().total());
        Assertions.assertEquals(expectedItemsCount, actualOutput.data().size());

        if(StringUtils.isNotEmpty(expectedName)){
            Assertions.assertEquals(expectedName, actualOutput.data().get(0).name());
        }
    }

    @Test
    public void givenValidIds_whenCallsFindAllById_shouldReturnElements(){


        final var actor1 = this.categoryRepository.save(CategoryDocument.from(Fixture.Categories.aulas()));
        final var actor2 =this.categoryRepository.save(CategoryDocument.from(Fixture.Categories.lives()));

        final var expectedIds = Set.of(actor1.id(), actor2.id());
        final var expectedSize = 2;

        final var actualOutput =this.categoryGateway.findAllById(expectedIds);

        Assertions.assertEquals(expectedSize, actualOutput.size());
        Assertions.assertTrue(expectedIds.containsAll(actualOutput.stream().map(Category::id).toList()));


    }

    @Test
    public void givenNullIds_whenCallsFindAllById_shouldReturnEmpty(){


        final Set<String> expectedIds = null;

        final var actualOutput =this.categoryGateway.findAllById(expectedIds);
        Assertions.assertTrue(actualOutput.isEmpty());


    }
    @Test
    public void givenEmptyIds_whenCallsFindAllById_shouldReturnEmpty(){


        final Set<String> expectedIds = Set.of();

        final var actualOutput =this.categoryGateway.findAllById(expectedIds);
        Assertions.assertTrue(actualOutput.isEmpty());


    }

    private void mockCategories(){
        this.categoryRepository.save(CategoryDocument.from(Fixture.Categories.aulas()));
        this.categoryRepository.save(CategoryDocument.from(Fixture.Categories.talks()));
        this.categoryRepository.save(CategoryDocument.from(Fixture.Categories.lives()));

    }
}
