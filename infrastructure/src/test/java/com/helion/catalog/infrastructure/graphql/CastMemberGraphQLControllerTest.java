package com.helion.catalog.infrastructure.graphql;

import com.helion.catalog.GraphQLControllerTest;
import com.helion.catalog.application.castmember.list.ListCastMemberOutput;
import com.helion.catalog.application.castmember.list.ListCastMemberUseCase;
import com.helion.catalog.application.castmember.save.SaveCastMemberUseCase;
import com.helion.catalog.domain.Fixture;
import com.helion.catalog.domain.castmember.CastMember;
import com.helion.catalog.domain.castmember.CastMemberSearchQuery;
import com.helion.catalog.domain.castmember.CastMemberType;
import com.helion.catalog.domain.pagination.Pagination;
import com.helion.catalog.domain.utils.IdUtils;
import com.helion.catalog.domain.utils.InstantUtils;
import com.helion.catalog.infrastructure.castmember.GqlCastMemberPresenter;
import com.helion.catalog.infrastructure.castmember.models.GqlCastMember;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;

import java.util.List;
import java.util.Map;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@GraphQLControllerTest(controllers=CastMemberGraphQLController.class)
public class CastMemberGraphQLControllerTest {

    @MockBean
    private ListCastMemberUseCase listCastMemberUseCase;

    @MockBean
    private SaveCastMemberUseCase saveCastMemberUseCase;

    @Autowired
    private GraphQlTester graphql;



    @Test
    public void givenDefaultArgumentsWhenCallsCastMembers_shouldReturn(){

        final var castMembers =
                List.of(
                        ListCastMemberOutput.from(Fixture.CastMembers.actor()),
                        ListCastMemberOutput.from(Fixture.CastMembers.director())
                );

        final var expectedCastMembers = castMembers.stream().map(GqlCastMemberPresenter::present).toList();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedSort = "name";
        final var expectedSearch = "";
        final var expectedDirection = "asc";

        when(this.listCastMemberUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, castMembers.size(), castMembers));

        final var query = """
                {
                    castMembers {
                        id
                        name
                        type
                        createdAt
                        updatedAt
                    }
                }
                """;
        final var res = this.graphql.document(query).execute();

        final var actualCastMembers = res.path("castMembers")
                .entityList(GqlCastMember.class)
                .get();
        Assertions.assertTrue(actualCastMembers.size() == expectedCastMembers.size()
                && actualCastMembers.containsAll(expectedCastMembers));

        final var capturer = ArgumentCaptor.forClass(CastMemberSearchQuery.class);
        verify(this.listCastMemberUseCase,times(1)).execute(capturer.capture());

        final var actualQuery = capturer.getValue();

        Assertions.assertEquals(expectedPage, actualQuery.page());
        Assertions.assertEquals(expectedPerPage, actualQuery.perPage());
        Assertions.assertEquals(expectedSort, actualQuery.sort());
        Assertions.assertEquals(expectedDirection, actualQuery.direction());
        Assertions.assertEquals(expectedSearch, actualQuery.terms());
    }

    @Test
    public void givenCustomArgumentsWhenCallsListCastMembers_shouldReturn(){

        final var castMembers =
                List.of(
                        ListCastMemberOutput.from(Fixture.CastMembers.actor()),
                        ListCastMemberOutput.from(Fixture.CastMembers.director())
                );
        final var expectedCastMembers = castMembers.stream().map(GqlCastMemberPresenter::present).toList();
        final var expectedPage = 2;
        final var expectedPerPage = 15;
        final var expectedSort = "id";
        final var expectedSearch = "asd";
        final var expectedDirection = "desc";

        when(this.listCastMemberUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, castMembers.size(), castMembers));

        final var query = """
                query AllQueries($search: String, $page: Int, $perPage: Int,  $sort: String, $direction: String) {
                    castMembers(search: $search, page: $page, perPage: $perPage, sort: $sort, direction: $direction) {
                        id
                        name
                        type
                        createdAt
                        updatedAt
                    }
                }
                """;
        final var res = this.graphql.document(query)
                .variable("search", expectedSearch)
                .variable("page", expectedPage)
                .variable("perPage", expectedPerPage)
                .variable("sort", expectedSort)
                .variable("direction", expectedDirection)
                .execute();

        final var actualCastMembers = res.path("castMembers")
                .entityList(GqlCastMember.class)
                .get();
        Assertions.assertTrue(actualCastMembers.size() == expectedCastMembers.size()
                && actualCastMembers.containsAll(expectedCastMembers));

        final var capturer = ArgumentCaptor.forClass(CastMemberSearchQuery.class);
        verify(this.listCastMemberUseCase,times(1)).execute(capturer.capture());

        final var actualQuery = capturer.getValue();

        Assertions.assertEquals(expectedPage, actualQuery.page());
        Assertions.assertEquals(expectedPerPage, actualQuery.perPage());
        Assertions.assertEquals(expectedSort, actualQuery.sort());
        Assertions.assertEquals(expectedDirection, actualQuery.direction());
        Assertions.assertEquals(expectedSearch, actualQuery.terms());
    }

    @Test
    public void givenCastMemberInputWhenCallsSaveCastMemberMutation_shouldPersistAndReturn(){

        final var expectedId = IdUtils.uniqueId();
        final var expectedName= "Paulo Ator";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedCreatedAt = InstantUtils.now();
        final var expectedUpdatedAt = InstantUtils.now();

        final var input = Map.of(
                "id", expectedId,
                "name", expectedName,
                "type", expectedType,
                "createdAt", expectedCreatedAt.toString(),
                "updatedAt", expectedUpdatedAt.toString()

        );

        doAnswer(returnsFirstArg()).when(saveCastMemberUseCase).execute(any());

        final var query = """
                mutation SaveCastMember($input: CastMemberInput!){
                    castMember: saveCastMember(input: $input){
                        id
                        name
                        type
                        createdAt
                        updatedAt
                        
                    }
                    
                }
                """;
        final var res = this.graphql.document(query)
                .variable("input", input)
                .execute()
                .path("castMember.id").entity(String.class).isEqualTo(expectedId)
                .path("castMember.name").entity(String.class).isEqualTo(expectedName)
                .path("castMember.type").entity(CastMemberType.class).isEqualTo(expectedType)
                .path("castMember.createdAt").entity(String.class).isEqualTo(expectedCreatedAt.toString());
               // .path("castMember.updatedAt").entity(String.class).isEqualTo(expectedUpdatedAt.toString());





        final var capturer = ArgumentCaptor.forClass(CastMember.class);
        verify(this.saveCastMemberUseCase,times(1)).execute(capturer.capture());

        final var actualCastMember = capturer.getValue();

        Assertions.assertEquals(expectedId, actualCastMember.id());
        Assertions.assertEquals(expectedName, actualCastMember.name());
        Assertions.assertEquals(expectedType.toString(), actualCastMember.type().toString());
        Assertions.assertEquals(expectedCreatedAt, actualCastMember.createdAt());
        Assertions.assertEquals(expectedUpdatedAt, actualCastMember.updatedAt());

    }


}
