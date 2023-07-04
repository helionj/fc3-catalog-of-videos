package com.helion.admin.catalog.e2e.castmember;

import com.helion.admin.catalog.E2ETest;
import com.helion.admin.catalog.domain.Fixture;
import com.helion.admin.catalog.domain.castmember.CastMemberID;
import com.helion.admin.catalog.domain.castmember.CastMemberType;
import com.helion.admin.catalog.domain.genre.GenreID;
import com.helion.admin.catalog.e2e.MockDsl;
import com.helion.admin.catalog.infrastructure.castmember.models.UpdateCastMemberRequest;
import com.helion.admin.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@E2ETest
@Testcontainers
public class CastMemberE2ETest implements MockDsl {

    @Autowired
    private CastMemberRepository castMemberRepository;

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
    public void asACatalogAdminIShouldBeAbleToCreateANewCastMemberWithValidValues() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());

        Assertions.assertEquals(0, castMemberRepository.count());

        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();


        final var actualId= givenACastMember(expectedName, expectedType);

        final var actualMember = retrieveACastMember(actualId);

        Assertions.assertEquals(expectedName, actualMember.name());
        Assertions.assertEquals(expectedType, actualMember.type());
        Assertions.assertNotNull(actualMember.createdAt());
        Assertions.assertNotNull(actualMember.updatedAt());
        Assertions.assertEquals(actualMember.createdAt(), actualMember.updatedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSeeATreatedErrorByCreatingANewCastMemberWithInValidValues() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());

        Assertions.assertEquals(0, castMemberRepository.count());

        final String expectedName = null;
        final var expectedType = Fixture.CastMembers.type();
        final var expectedErrorMessage = "'name' should not be null";


        givenACastMemberResult(expectedName, expectedType)
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedErrorMessage)));

    }
    @Test
    public void asACatalogAdminIShouldBeAbleToNavigateThruAllMembers() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, castMemberRepository.count());
        givenACastMember("Vin Diesel", CastMemberType.ACTOR);
        givenACastMember("Quentin Tarantino", CastMemberType.DIRECTOR);
        givenACastMember("Jason Momoa", CastMemberType.ACTOR);

        listCastMembers(0, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Jason Momoa")));


        listCastMembers(1, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(1)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Quentin Tarantino")));


        listCastMembers(2, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(2)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Vin Diesel")));

        listCastMembers(3, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(3)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(0)));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSearchThruAllMembers() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, castMemberRepository.count());
        givenACastMember("Vin Diesel", CastMemberType.ACTOR);
        givenACastMember("Quentin Tarantino", CastMemberType.DIRECTOR);
        givenACastMember("Jason Momoa", CastMemberType.ACTOR);

        listCastMembers(0, 1,"vin")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(1)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Vin Diesel")));

    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSortThruAllMembersByNameDesc() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, castMemberRepository.count());
        givenACastMember("Vin Diesel", CastMemberType.ACTOR);
        givenACastMember("Quentin Tarantino", CastMemberType.DIRECTOR);
        givenACastMember("Jason Momoa", CastMemberType.ACTOR);

        listCastMembers(0, 3,"","name", "desc")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(3)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(3)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Vin Diesel")))
                .andExpect(jsonPath("$.items[1].name", equalTo("Quentin Tarantino")))
                .andExpect(jsonPath("$.items[2].name", equalTo("Jason Momoa")));

    }

    @Test
    public void asACatalogAdminIShouldBeAbleToGetACastMemberById() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());

        Assertions.assertEquals(0, castMemberRepository.count());

        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();


        final var actualId= givenACastMember(expectedName, expectedType);
        givenACastMember(Fixture.name(), Fixture.CastMembers.type());
        givenACastMember(Fixture.name(), Fixture.CastMembers.type());

        final var actualMember = retrieveACastMember(actualId);

        Assertions.assertEquals(expectedName, actualMember.name());
        Assertions.assertEquals(expectedType, actualMember.type());
        Assertions.assertNotNull(actualMember.createdAt());
        Assertions.assertNotNull(actualMember.updatedAt());
        Assertions.assertEquals(actualMember.createdAt(), actualMember.updatedAt());
    }
    @Test
    public void asACatalogAdminIShouldBeAbleToSeeATreatedErrorByGettingANotFoundError() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());

        Assertions.assertEquals(0, castMemberRepository.count());


        givenACastMember(Fixture.name(), Fixture.CastMembers.type());
        givenACastMember(Fixture.name(), Fixture.CastMembers.type());

        retrieveACastMemberResult(CastMemberID.from("123"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo("CastMember with ID 123 was not found")));

    }

    @Test
    public void asACatalogAdminIShouldBeAbleToUpdateACastMemberByItsIdentifier() throws Exception {

        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, castMemberRepository.count());

        final var expectedName = Fixture.name();
        final var expectedType = CastMemberType.DIRECTOR;


        final var actualId= givenACastMember(Fixture.name(), CastMemberType.DIRECTOR);



        final var aRequestBody = new UpdateCastMemberRequest(expectedName, expectedType);
        updateACastMember(actualId, aRequestBody).andExpect(status().isOk());

        final var actualCastMember = castMemberRepository.findById(actualId.getValue()).get();
        Assertions.assertEquals(expectedName, actualCastMember.getName());

        Assertions.assertEquals(expectedType, actualCastMember.getType());
        Assertions.assertNotNull(actualCastMember.getCreatedAt());
        Assertions.assertNotNull(actualCastMember.getUpdatedAt());
        Assertions.assertTrue(actualCastMember.getCreatedAt().isBefore(actualCastMember.getUpdatedAt()));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSeeATreatedErrorUpdatingACastMemberWithInvalidValues() throws Exception {

        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, castMemberRepository.count());
        final var expectedErrorMessage = "'name' should not be null";
        final String expectedName = null;
        final var expectedType = CastMemberType.DIRECTOR;


        final var actualId= givenACastMember(Fixture.name(), CastMemberType.DIRECTOR);

        final var aRequestBody = new UpdateCastMemberRequest(expectedName, expectedType);
        updateACastMember(actualId, aRequestBody).andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

    }

    @Test
    public void asACatalogAdminIShouldBeAbleToDeleteACastMemberByItsIdentifier() throws Exception {

        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, castMemberRepository.count());


        final var actualId= givenACastMember(Fixture.name(), Fixture.CastMembers.type());
        Assertions.assertEquals(1, castMemberRepository.count());
        deleteACastMember(actualId).andExpect(status().isNoContent());
        Assertions.assertEquals(0, castMemberRepository.count());
        Assertions.assertFalse(this.castMemberRepository.existsById(actualId.getValue()));
    }
    @Test
    public void asACatalogAdminIShouldNotSeeAnErrorByDeletingNotExistingCastMember() throws Exception {

        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, castMemberRepository.count());


        deleteAGenre(GenreID.from("123")).andExpect(status().isNoContent());

        Assertions.assertEquals(0, castMemberRepository.count());

    }
}
