package com.helion.admin.catalog.e2e.genre;

import com.helion.admin.catalog.E2ETest;
import com.helion.admin.catalog.domain.category.CategoryID;
import com.helion.admin.catalog.domain.genre.GenreID;
import com.helion.admin.catalog.e2e.MockDsl;
import com.helion.admin.catalog.infrastructure.genre.models.UpdateGenreRequest;
import com.helion.admin.catalog.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@E2ETest
@Testcontainers
public class GenreE2ETest implements MockDsl {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private MockMvc mvc;

    @Container
    private static final MySQLContainer MYSQL_CONTAINER =
            new MySQLContainer("mysql:latest")
                    .withPassword("123456")
                    .withUsername("root")
                    .withDatabaseName("adm_videos");

    @DynamicPropertySource
    public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
        final var mappedPort = MYSQL_CONTAINER.getMappedPort(3306);
        System.out.printf("Container is running on port: %s\n", mappedPort);
        registry.add("mysql.port", () -> mappedPort);
    }

    @Override
    public MockMvc mvc() {
        return this.mvc;
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToCreateANewGenreWithValidValues() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());

        Assertions.assertEquals(0, genreRepository.count());

        final var expectedName = "Action";
        final var expectedCategories = List.<CategoryID>of();
        final var expectedIsActive = true;

        final var actualId= givenAGenre(expectedName, expectedCategories, expectedIsActive);

        final var actualGenre = retrieveAGenre(actualId);

        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertTrue(expectedCategories.size() == actualGenre.categories().size()
                && expectedCategories.containsAll(actualGenre.categories()));
        Assertions.assertEquals(expectedIsActive, actualGenre.active());
        Assertions.assertNotNull(actualGenre.createdAt());
        Assertions.assertNotNull(actualGenre.updatedAt());
        Assertions.assertNull(actualGenre.deletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToCreateANewGenreWithCategories() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());

        Assertions.assertEquals(0, genreRepository.count());

        final var filmes = givenACategory("Filmes", null, true);

        final var expectedName = "Action";
        final var expectedCategories = List.of(filmes);
        final var expectedIsActive = true;

        final var actualId= givenAGenre(expectedName, expectedCategories, expectedIsActive);

        final var actualGenre = genreRepository.findById(actualId.getValue()).get();

        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertTrue(expectedCategories.size() == actualGenre.getCategoriesIDs().size()
                && expectedCategories.containsAll(actualGenre.getCategoriesIDs()));
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToNavigateThruAllGenres() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());
        givenAGenre("Action", List.<CategoryID>of(), true);
        givenAGenre("Sports", List.<CategoryID>of(), true);
        givenAGenre("Drama", List.<CategoryID>of(), true);

        listGenres(0, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Action")));

        listGenres(1, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(1)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Drama")));
        listGenres(2, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(2)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Sports")));
        listGenres(3, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(3)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(0)));

    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSearchBetweenToAllCategories() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        givenAGenre("Action", List.<CategoryID>of(), true);
        givenAGenre("Sports", List.<CategoryID>of(), true);
        givenAGenre("Drama", List.<CategoryID>of(), true);

        listGenres(0,1,"spo")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(1)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Sports")));



    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSortAllGenresByNameDesc() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        givenAGenre("Action", List.<CategoryID>of(), true);
        givenAGenre("Sports", List.<CategoryID>of(), true);
        givenAGenre("Drama", List.<CategoryID>of(), true);

        listGenres(0,3,"", "name", "desc")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(3)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(3)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Sports")))
                .andExpect(jsonPath("$.items[1].name", equalTo("Drama")))
                .andExpect(jsonPath("$.items[2].name", equalTo("Action")));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToGetAGenreByItsIdentifier() throws Exception {

        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var filmes = givenACategory("Filmes", null, true);
        final var expectedName = "Action";
        final var expectedCategories = List.of(filmes);
        final var expectedIsActive = true;

        final var actualId= givenAGenre(expectedName,expectedCategories, expectedIsActive);

        final var actualGenre = retrieveAGenre(actualId);

        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertTrue(mapTo(expectedCategories, CategoryID::getValue).size() ==  actualGenre.categories().size()
            && mapTo(expectedCategories, CategoryID::getValue).containsAll(actualGenre.categories()));
        Assertions.assertEquals(expectedIsActive, actualGenre.active());
        Assertions.assertNotNull(actualGenre.createdAt());
        Assertions.assertNotNull(actualGenre.updatedAt());
        Assertions.assertNull(actualGenre.deletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSeeATreatedErrorGettingANotFoundGenre() throws Exception {

        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());




        final var aRequest = get("/genres/123")
                .contentType(MediaType.APPLICATION_JSON);

        final var json = this.mvc.perform(aRequest)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo("Genre with ID 123 was not found")));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToUpdateAGenreByItsIdentifier() throws Exception {

        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var filmes = givenACategory("Filmes", null, true);
        final var expectedName = "Ação";
        final var expectedCategories = List.of(filmes);
        final var expectedIsActive = true;


        final var actualId= givenAGenre("Action",expectedCategories, true);


        final var aRequestBody = new UpdateGenreRequest(expectedName, mapTo(expectedCategories,CategoryID::getValue), expectedIsActive);
        updateAGenre(actualId, aRequestBody).andExpect(status().isOk());

        final var actualGenre = genreRepository.findById(actualId.getValue()).get();
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertTrue(expectedCategories.size() == actualGenre.getCategories().size()
            &&expectedCategories.containsAll(actualGenre.getCategoriesIDs()));
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertTrue(actualGenre.getCreatedAt().isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToInactivateAGenreByItsIdentifier() throws Exception {

        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var filmes = givenACategory("Filmes", null, true);
        final var expectedName = "Ação";
        final var expectedCategories = List.of(filmes);
        final var expectedIsActive = false;

        final var actualId= givenAGenre(expectedName, expectedCategories, true);

        final var aRequestBody = new UpdateGenreRequest(expectedName, mapTo(expectedCategories,CategoryID::getValue), expectedIsActive);
        updateAGenre(actualId,aRequestBody);

        final var actualGenre = genreRepository.findById(actualId.getValue()).get();
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertTrue(expectedCategories.size() == actualGenre.getCategoriesIDs().size()
            && expectedCategories.containsAll(actualGenre.getCategoriesIDs()));
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertTrue(actualGenre.getCreatedAt().isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNotNull(actualGenre.getDeletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToActivateAGenreByItsIdentifier() throws Exception {

        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var filmes = givenACategory("Filmes", null, true);
        final var expectedName = "Ação";
        final var expectedCategories = List.of(filmes);
        final var expectedIsActive = true;

        final var actualId= givenAGenre(expectedName, expectedCategories, false);

        final var aRequestBody = new UpdateGenreRequest(expectedName, mapTo(expectedCategories,CategoryID::getValue), expectedIsActive);
        updateAGenre(actualId,aRequestBody);

        final var actualGenre = genreRepository.findById(actualId.getValue()).get();
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertTrue(expectedCategories.size() == actualGenre.getCategoriesIDs().size()
                && expectedCategories.containsAll(actualGenre.getCategoriesIDs()));
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertTrue(actualGenre.getCreatedAt().isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());

    }
    @Test
    public void asACatalogAdminIShouldBeAbleToDeleteAGenreByItsIdentifier() throws Exception {

        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());


        final var filmes= givenACategory("Filmes", null, true);
        final var actualId= givenAGenre("Açã0", List.of(filmes), true);
        Assertions.assertEquals(1, genreRepository.count());
        deleteAGenre(actualId).andExpect(status().isNoContent());
        Assertions.assertEquals(0, genreRepository.count());
        Assertions.assertFalse(this.genreRepository.existsById(actualId.getValue()));
    }
    @Test
    public void asACatalogAdminIShouldNotSeeAnErrorByDeletingNotExistingGenre() throws Exception {

        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());


        deleteAGenre(GenreID.from("123")).andExpect(status().isNoContent());

        Assertions.assertEquals(0, genreRepository.count());

    }












}
