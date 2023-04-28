package com.helion.admin.catalog.application.castmember.retrieve.list;

import com.helion.admin.catalog.application.Fixture;
import com.helion.admin.catalog.application.UseCaseTest;
import com.helion.admin.catalog.domain.castmember.CastMember;
import com.helion.admin.catalog.domain.castmember.CastMemberGateway;
import com.helion.admin.catalog.domain.category.pagination.Pagination;
import com.helion.admin.catalog.domain.category.pagination.SearchQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;

public class ListCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultListCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGateway);
    }

    @Test
    public void givenAValidQuery_whenCallsListCastMembers_shouldReturnCastMemberd(){
        final var aMember1 = CastMember.newMember(Fixture.name(), Fixture.CastMember.type());
        final var aMember2 = CastMember.newMember(Fixture.name(), Fixture.CastMember.type());
        final var aMember3 = CastMember.newMember(Fixture.name(), Fixture.CastMember.type());

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

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);


        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, aMembers.size(), aMembers);
        final var expectedResult = expectedPagination.map(CastMemberListOutput::from);
        Mockito.when(castMemberGateway.findAll(eq(aQuery))).thenReturn(expectedPagination);

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
        Mockito.when(castMemberGateway.findAll(eq(aQuery))).thenReturn(expectedPagination);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedResult, actualResult);
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(aMembers.size(), actualResult.total());
    }

    @Test
    public void givenAValidQuery_whenGatewayThrowsException_shouldReturnException(){

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedErrorMessage = "Gateway Error";

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        Mockito.when(castMemberGateway.findAll(eq(aQuery))).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualException = Assertions.assertThrows(IllegalStateException.class,
                () -> useCase.execute(aQuery));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

    }
}
