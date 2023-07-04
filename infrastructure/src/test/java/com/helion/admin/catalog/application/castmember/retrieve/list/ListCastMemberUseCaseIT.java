package com.helion.admin.catalog.application.castmember.retrieve.list;

import com.helion.admin.catalog.IntegrationTest;
import com.helion.admin.catalog.domain.Fixture;
import com.helion.admin.catalog.domain.castmember.CastMember;
import com.helion.admin.catalog.domain.castmember.CastMemberGateway;
import com.helion.admin.catalog.domain.category.pagination.Pagination;
import com.helion.admin.catalog.domain.category.pagination.SearchQuery;
import com.helion.admin.catalog.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.helion.admin.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;

@IntegrationTest
public class ListCastMemberUseCaseIT{

    @Autowired
    private ListCastMemberUseCase useCase;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @SpyBean
    private CastMemberGateway castMemberGateway;


    @Test
    public void givenAValidQuery_whenCallsListCastMembers_shouldReturnCastMemberd(){
        final var aMember1 = CastMember.newMember(Fixture.name(), Fixture.CastMembers.type());
        final var aMember2 = CastMember.newMember(Fixture.name(), Fixture.CastMembers.type());
        final var aMember3 = CastMember.newMember(Fixture.name(), Fixture.CastMembers.type());

        final var aMembers = List.of(aMember1, aMember2, aMember3);

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 3;

        final var expectedItems = aMembers.stream()
                .map(CastMemberListOutput::from)
                .toList();

        final var membersJpa = aMembers.stream()
                .map(CastMemberJpaEntity::from)
                .toList();

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        this.castMemberRepository.saveAllAndFlush(membersJpa);

        Assertions.assertEquals(3, this.castMemberRepository.count());

        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, aMembers.size(), aMembers);
        final var expectedResult = expectedPagination.map(CastMemberListOutput::from);

        final var actualResult = useCase.execute(aQuery);
        Assertions.assertEquals(expectedItems, actualResult.items());
        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedResult, actualResult);
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(aMembers.size(), actualResult.total());

        Mockito.verify(castMemberGateway, times(1)).findAll(eq(aQuery));
    }
    @Test
    public void givenAValidQuery_whenHasNoResults_shouldReturnEmpty(){
        final var aMembers = List.<CastMember>of();
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 0;

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);


        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, aMembers.size(), aMembers);
        final var expectedResult = expectedPagination.map(CastMemberListOutput::from);

        Assertions.assertEquals(0, this.castMemberRepository.count());
        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedResult, actualResult);
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(aMembers.size(), actualResult.total());
    }

}
