package com.helion.catalog.infrastructure.graphql;

import com.helion.catalog.IntegrationTest;
import com.helion.catalog.WebGraphQlSecurityInterceptor;
import com.helion.catalog.application.castmember.list.ListCastMemberOutput;
import com.helion.catalog.application.castmember.list.ListCastMemberUseCase;
import com.helion.catalog.application.castmember.save.SaveCastMemberUseCase;
import com.helion.catalog.domain.Fixture;
import com.helion.catalog.domain.pagination.Pagination;
import com.helion.catalog.infrastructure.configuration.security.Roles;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.server.WebGraphQlHandler;
import org.springframework.graphql.test.tester.WebGraphQlTester;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@IntegrationTest()
public class CastMemberGraphQLControllerIT {

    @MockBean
    private ListCastMemberUseCase listCastMemberUseCase;

    @MockBean
    private SaveCastMemberUseCase saveCastMemberUseCase;

    @Autowired
    private WebGraphQlHandler webGraphQlHandler;

    @Autowired
    private WebGraphQlSecurityInterceptor interceptor;


    @Test
    public void givenAnonymousUser_whenQueries_shouldReturnUnauthorized() {
        interceptor.setAuthorities();
        final var document = "query castMembers { castMembers { id } }";
        final var graphQlTesters = WebGraphQlTester.create(webGraphQlHandler);
        graphQlTesters.document(document).execute()
                .errors().expect(err -> "Unauthorized".equals(err.getMessage()) && "castMembers".equals(err.getPath()))
                .verify();
    }

    @Test
    public void givenUserWithAdminRole_whenQueries_shouldReturnResult() {
        interceptor.setAuthorities(Roles.ROLE_ADMIN);
        final var castMembers = List.of(
                ListCastMemberOutput.from(Fixture.CastMembers.actor()),
                ListCastMemberOutput.from(Fixture.CastMembers.actor2())
        );

        final var expectedIds = castMembers.stream().map(ListCastMemberOutput::id).toList();

        when(this.listCastMemberUseCase.execute(any()))
                .thenReturn(new Pagination<>(0, 10, castMembers.size(), castMembers));

        final var document = "query castMembers { castMembers { id } }";

        final var graphQlTesters = WebGraphQlTester.create(webGraphQlHandler);
        graphQlTesters.document(document).execute()
                .errors().verify()
                .path("castMembers[*].id").entityList(String.class).isEqualTo(expectedIds);
    }

    @Test
    public void givenUserWithSubscriberRole_whenQueries_shouldReturnResult() {
        interceptor.setAuthorities(Roles.ROLE_SUBSCRIBER);

        final var castMembers = List.of(
                ListCastMemberOutput.from(Fixture.CastMembers.actor()),
                ListCastMemberOutput.from(Fixture.CastMembers.actor2())
        );

        final var expectedIds = castMembers.stream().map(ListCastMemberOutput::id).toList();

        when(this.listCastMemberUseCase.execute(any()))
                .thenReturn(new Pagination<>(0, 10, castMembers.size(), castMembers));

        final var document = "query castMembers { castMembers { id } }";
        final var graphQlTesters = WebGraphQlTester.create(webGraphQlHandler);
        graphQlTesters.document(document).execute()
                .errors().verify()
                .path("castMembers[*].id").entityList(String.class).isEqualTo(expectedIds);
    }

    @Test
    public void givenUserWithCastMembersRole_whenQueries_shouldReturnResult() {
        interceptor.setAuthorities(Roles.ROLE_CAST_MEMBERS);

        final var castMembers = List.of(
                ListCastMemberOutput.from(Fixture.CastMembers.actor()),
                ListCastMemberOutput.from(Fixture.CastMembers.actor2())
        );

        final var expectedIds = castMembers.stream().map(ListCastMemberOutput::id).toList();

        when(this.listCastMemberUseCase.execute(any()))
                .thenReturn(new Pagination<>(0, 10, castMembers.size(), castMembers));

        final var document = "query castMembers { castMembers { id } }";
        final var graphQlTesters = WebGraphQlTester.create(webGraphQlHandler);
        graphQlTesters.document(document).execute()
                .errors().verify()
                .path("castMembers[*].id").entityList(String.class).isEqualTo(expectedIds);
    }


}
