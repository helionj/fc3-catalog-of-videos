package com.helion.catalog.application.castmember.list;

import com.helion.catalog.application.UseCaseTest;
import com.helion.catalog.domain.Fixture;
import com.helion.catalog.domain.castmember.CastMemberGateway;
import com.helion.catalog.domain.castmember.CastMemberSearchQuery;
import com.helion.catalog.domain.pagination.Pagination;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ListCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private ListCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;
    @Test
    public void givenAValidQuery_whenCallsListCastMembers_shouldReturCastMembers() {
        final var castMembers = List.of(
                Fixture.CastMembers.actor(), Fixture.CastMembers.director()
        );

        final var expectedItems = castMembers.stream().map(ListCastMemberOutput::from).toList();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "algo";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 2;

        final var aQuery =
                new CastMemberSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var pagination =
                new Pagination<>(expectedPage, expectedPerPage, castMembers.size(), castMembers);


        when(this.castMemberGateway.findAll(any())).thenReturn(pagination);

        final var actualOutput = this.useCase.execute(aQuery);

        Assertions.assertEquals(expectedPage, actualOutput.meta().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.meta().perPage());
        Assertions.assertEquals(expectedItemsCount, actualOutput.data().size());
        Assertions.assertTrue(expectedItems.size() == actualOutput.data().size() && expectedItems.containsAll(actualOutput.data()));


    }

}
