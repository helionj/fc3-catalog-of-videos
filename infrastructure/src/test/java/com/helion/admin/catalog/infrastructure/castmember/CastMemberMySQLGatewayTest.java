package com.helion.admin.catalog.infrastructure.castmember;

import com.helion.admin.catalog.MySQLGatewayTest;
import com.helion.admin.catalog.domain.castmember.CastMember;
import com.helion.admin.catalog.domain.castmember.CastMemberID;
import com.helion.admin.catalog.domain.castmember.CastMemberType;
import com.helion.admin.catalog.domain.category.pagination.SearchQuery;
import com.helion.admin.catalog.infrastructure.Fixture;
import com.helion.admin.catalog.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.helion.admin.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@MySQLGatewayTest
public class CastMemberMySQLGatewayTest {

    @Autowired
    private CastMemberMySQLGateway castMemberMySQLGateway;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @Test
    public void testDependencies(){
        Assertions.assertNotNull(castMemberMySQLGateway);
        Assertions.assertNotNull(castMemberRepository);
    }

    @Test
    public void givenAValidCastMember_whenCreateACastMember_shouldPersistIt(){
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();

        final var aMember = CastMember.newMember(expectedName, expectedType);

        final var expectedId = aMember.getId();

        Assertions.assertEquals(0, castMemberRepository.count());
        final var actualMember = castMemberMySQLGateway.create(CastMember.with(aMember));

        Assertions.assertEquals(1, castMemberRepository.count());

        Assertions.assertEquals(expectedId, actualMember.getId());
        Assertions.assertEquals(expectedName, actualMember.getName());
        Assertions.assertEquals(expectedType, actualMember.getType());
        Assertions.assertEquals(aMember.getCreatedAt(), actualMember.getCreatedAt());
        Assertions.assertEquals(aMember.getCreatedAt(), actualMember.getUpdatedAt());

        final var persistedMember = castMemberRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(aMember.getName(), persistedMember.getName());
        Assertions.assertEquals(expectedType, persistedMember.getType());

        Assertions.assertEquals(aMember.getCreatedAt(), persistedMember.getCreatedAt());
        Assertions.assertEquals(aMember.getUpdatedAt(), persistedMember.getUpdatedAt());

    }

    @Test
    public void givenAValidCastMember_whenUpdateACastMember_shouldRefreshIt(){
        final var expectedName = Fixture.name();
        final var expectedType = CastMemberType.ACTOR;

        final var aMember = CastMember.newMember("Vinny", CastMemberType.DIRECTOR);

        final var expectedId = aMember.getId();
        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        Assertions.assertEquals(1, castMemberRepository.count());
        final var updatedMember = aMember.update(expectedName, expectedType);
        final var actualMember = castMemberMySQLGateway.update(CastMember.with(updatedMember));

        Assertions.assertEquals(1, castMemberRepository.count());

        Assertions.assertEquals(expectedId, actualMember.getId());
        Assertions.assertEquals(expectedName, actualMember.getName());
        Assertions.assertEquals(expectedType, actualMember.getType());
        Assertions.assertEquals(updatedMember.getCreatedAt(), actualMember.getCreatedAt());
        Assertions.assertTrue(updatedMember.getCreatedAt().isBefore(actualMember.getUpdatedAt()));

        final var persistedMember = castMemberRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(updatedMember.getName(), persistedMember.getName());
        Assertions.assertEquals(expectedType, persistedMember.getType());

        Assertions.assertEquals(updatedMember.getCreatedAt(), persistedMember.getCreatedAt());
        Assertions.assertEquals(updatedMember.getUpdatedAt(), persistedMember.getUpdatedAt());

    }

    @Test
    public void givenAPersistedCastMember_whenCallsDeleteById_shouldDeleteCastMember() {
        final var aMember = CastMember.newMember("Vin Diesel", CastMemberType.ACTOR);
        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));
        Assertions.assertEquals(1, castMemberRepository.count());
        castMemberMySQLGateway.deleteById(aMember.getId());
        Assertions.assertEquals(0, castMemberRepository.count());

    }

    @Test
    public void givenAInvalidGenre_whenCallsDeleteById_shouldReturnOk() {

        Assertions.assertEquals(0, castMemberRepository.count());

        castMemberMySQLGateway.deleteById(CastMemberID.from("123"));
        Assertions.assertEquals(0, castMemberRepository.count());

    }

    @Test
    public void givenAPrePersistedGenre_whenCallsFindById_ShouldReturnGenre(){

        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();


        final var aMember = CastMember.newMember(expectedName, expectedType);

        final var expectedId = aMember.getId();

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));
        Assertions.assertEquals(1, castMemberRepository.count());
        final var actualMember = castMemberMySQLGateway.findById(expectedId).get();

        Assertions.assertEquals(expectedId, actualMember.getId());
        Assertions.assertEquals(expectedName, actualMember.getName());
        Assertions.assertEquals(expectedType, actualMember.getType());
        Assertions.assertEquals(aMember.getCreatedAt(), actualMember.getCreatedAt());
        Assertions.assertEquals(aMember.getUpdatedAt(), actualMember.getUpdatedAt());

    }

    @Test
    public void givenAInvalidCastMemberId_whenCallsFindById_ShouldReturnEmpty(){

        final var expectedId = CastMemberID.from("123");


        final var actualMember = castMemberMySQLGateway.findById(expectedId);

        Assertions.assertTrue(actualMember.isEmpty());

    }

    @Test
    public void givenEmptyCastMembers_whenCallFindAll_shouldReturnEmptyList() {
        final var expectedPage=0;
        final var expectedPerPage = 1;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualPage = castMemberMySQLGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedTotal, actualPage.items().size());

    }

    @ParameterizedTest
    @CsvSource({
            "ki,0,10,1,1,Kit Harigthon",
            "vin,0,10,1,1,Vin Diesel",
            "que,0,10,1,1,Quentin Tarantino",
            "jas,0,10,1,1,Jason Momoa",
            "mar,0,10,1,1,Martin Scorsese",
    })
    public void givenAValidTerms_whenCallFindAll_shouldReturnFiltered(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedGenreName

    ) {

        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        mockMembers();

        final var actualPage = castMemberMySQLGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
        Assertions.assertEquals(expectedGenreName, actualPage.items().get(0).getName());

    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,5,5,Jason Momoa",
            "name,desc,0,10,5,5,Vin Diesel",
            "createdAt,asc,0,10,5,5,Kit Harigthon",
            "createdAt,desc,0,10,5,5,Martin Scorsese"
    })
    public void givenAValidSortAndDirection_whenCallFindAll_shouldReturnSorted(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedGenreName

    ) {

        final var expectedTerms = "";


        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        mockMembers();

        final var actualPage = castMemberMySQLGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
        Assertions.assertEquals(expectedGenreName, actualPage.items().get(0).getName());

    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,5,Jason Momoa;Kit Harigthon",
            "1,2,2,5,Martin Scorsese;Quentin Tarantino",
            "2,2,1,5,Vin Diesel"
    })
    public void givenAValidPagination_whenCallFindAll_shouldReturnPaginated(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedGenres

    ) {

        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection= "asc";


        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        mockMembers();

        final var actualPage = castMemberMySQLGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());

        int index = 0;
        for(final var expectedName : expectedGenres.split(";")){
            final var actualName = actualPage.items().get(index).getName();
            Assertions.assertEquals(expectedName, actualName);
            index++;
        }
    }

    private void mockMembers(){
        castMemberRepository.saveAllAndFlush(List.of(
                        CastMemberJpaEntity.from(CastMember.newMember("Kit Harigthon", CastMemberType.ACTOR)),
                        CastMemberJpaEntity.from(CastMember.newMember("Vin Diesel", CastMemberType.ACTOR)),
                        CastMemberJpaEntity.from(CastMember.newMember("Quentin Tarantino", CastMemberType.DIRECTOR)),
                        CastMemberJpaEntity.from(CastMember.newMember("Jason Momoa", CastMemberType.ACTOR)),
                        CastMemberJpaEntity.from(CastMember.newMember("Martin Scorsese", CastMemberType.DIRECTOR))
                ));
    }
}