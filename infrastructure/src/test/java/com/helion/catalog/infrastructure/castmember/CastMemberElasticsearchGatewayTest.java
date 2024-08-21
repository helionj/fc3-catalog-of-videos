package com.helion.catalog.infrastructure.castmember;

import com.helion.catalog.AbstractElasticsearchTest;
import com.helion.catalog.domain.Fixture;
import com.helion.catalog.domain.castmember.CastMember;
import com.helion.catalog.domain.castmember.CastMemberSearchQuery;
import com.helion.catalog.infrastructure.castmember.persistence.CastMemberDocument;
import com.helion.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.shaded.org.apache.commons.lang3.StringUtils;

import java.util.Set;

public class CastMemberElasticsearchGatewayTest extends AbstractElasticsearchTest {

    @Autowired
    private CastMemberElasticsearchGateway castMemberGateway;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @Test
    public void testInjection(){
        Assertions.assertNotNull(this.castMemberGateway);
        Assertions.assertNotNull(this.castMemberRepository);
    }

    @Test
    public void givenValidCastMember_whenCallsSave_shouldPersistIt(){
        final var actor = Fixture.CastMembers.actor();

        final var actualOutput = this.castMemberGateway.save(actor);

        Assertions.assertEquals(actor, actualOutput);

        final var  actualCastMember = castMemberRepository.findById(actor.id()).get();

        Assertions.assertEquals(actor.id(), actualCastMember.id());
        Assertions.assertEquals(actor.name(), actualCastMember.name());
        Assertions.assertEquals(actor.type(), actualCastMember.type());
        Assertions.assertEquals(actor.createdAt(), actualCastMember.createdAt());
        Assertions.assertEquals(actor.updatedAt(), actualCastMember.updatedAt());
    }

    @Test
    public void givenValidId_whenCallsDeleteById_shouldDeleteIt(){
        final var actor = Fixture.CastMembers.actor();
        this.castMemberRepository.save(CastMemberDocument.from(actor));

        final var expectedId = actor.id();
        Assertions.assertTrue(this.castMemberRepository.existsById(expectedId));

        this.castMemberGateway.deleteById(expectedId);

        Assertions.assertFalse(this.castMemberRepository.existsById(expectedId));
    }

    @Test
    public void givenInValidId_whenCallsDeleteById_shouldBeOk(){
        final var expectedId = "any";

        Assertions.assertDoesNotThrow(() -> this.castMemberGateway.deleteById(expectedId));

    }

    @Test
    public void givenValidId_whenCallsFindById_shouldRetrieveIt(){
        final var actor = Fixture.CastMembers.actor();
        this.castMemberRepository.save(CastMemberDocument.from(actor));

        final var expectedId = actor.id();
        Assertions.assertTrue(this.castMemberRepository.existsById(expectedId));

        final var actualCastMember = this.castMemberGateway.findById(expectedId).get();

        Assertions.assertEquals(actor.id(), actualCastMember.id());
        Assertions.assertEquals(actor.name(), actualCastMember.name());
        Assertions.assertEquals(actor.type(), actualCastMember.type());
        Assertions.assertEquals(actor.createdAt(), actualCastMember.createdAt());
        Assertions.assertEquals(actor.updatedAt(), actualCastMember.updatedAt());
    }

    @Test
    public void givenInValidId_whenCallsFindById_shouldBeEmpty(){
        final var expectedId = "any";

        final var actualOutput = this.castMemberGateway.findById(expectedId);

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

        final var aQuery = new CastMemberSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualOutput =this.castMemberGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualOutput.meta().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.meta().perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.meta().total());
        Assertions.assertEquals(expectedTotal, actualOutput.data().size());

    }

    @ParameterizedTest
    @CsvSource({
            "pau,0,10,1,1,Paulo Actor",
            "mau,0,10,1,1,Mauro Actor",

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

        mockCastMembers();
        final var aQuery = new CastMemberSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualOutput =this.castMemberGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualOutput.meta().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.meta().perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.meta().total());
        Assertions.assertEquals(expectedItemsCount, actualOutput.data().size());
        Assertions.assertEquals(expectedName, actualOutput.data().get(0).name());

    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,3,3,José Director",
            "name,desc,0,10,3,3,Paulo Actor",
            "created_at,asc,0,10,3,3,Paulo Actor",
            "created_at,desc,0,10,3,3,José Director"

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


        mockCastMembers();
        final var aQuery = new CastMemberSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualOutput =this.castMemberGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualOutput.meta().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.meta().perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.meta().total());
        Assertions.assertEquals(expectedItemsCount, actualOutput.data().size());
        Assertions.assertEquals(expectedName, actualOutput.data().get(0).name());

    }

    @ParameterizedTest
    @CsvSource({
            "0,1,1,3,José Director",
            "1,1,1,3,Mauro Actor",
            "2,1,1,3,Paulo Actor",
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

        mockCastMembers();
        final var aQuery = new CastMemberSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualOutput =this.castMemberGateway.findAll(aQuery);

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


        final var actor1 = this.castMemberRepository.save(CastMemberDocument.from(Fixture.CastMembers.actor()));
        final var actor2 =this.castMemberRepository.save(CastMemberDocument.from(Fixture.CastMembers.actor2()));

        final var expectedIds = Set.of(actor1.id(), actor2.id());
        final var expectedSize = 2;

        final var actualOutput =this.castMemberGateway.findAllById(expectedIds);

        Assertions.assertEquals(expectedSize, actualOutput.size());
        Assertions.assertTrue(expectedIds.containsAll(actualOutput.stream().map(CastMember::id).toList()));


    }

    @Test
    public void givenNullIds_whenCallsFindAllById_shouldReturnEmpty(){


        final Set<String> expectedIds = null;

        final var actualOutput =this.castMemberGateway.findAllById(expectedIds);
        Assertions.assertTrue(actualOutput.isEmpty());


    }
    @Test
    public void givenEmptyIds_whenCallsFindAllById_shouldReturnEmpty(){


        final Set<String> expectedIds = Set.of();

        final var actualOutput =this.castMemberGateway.findAllById(expectedIds);
        Assertions.assertTrue(actualOutput.isEmpty());


    }

    private void mockCastMembers(){
        this.castMemberRepository.save(CastMemberDocument.from(Fixture.CastMembers.actor()));
        this.castMemberRepository.save(CastMemberDocument.from(Fixture.CastMembers.actor2()));
        this.castMemberRepository.save(CastMemberDocument.from(Fixture.CastMembers.director()));

    }

}
