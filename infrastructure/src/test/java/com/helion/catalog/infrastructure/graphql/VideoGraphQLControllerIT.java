package com.helion.catalog.infrastructure.graphql;

import com.helion.catalog.IntegrationTest;
import com.helion.catalog.WebGraphQlSecurityInterceptor;
import com.helion.catalog.application.castmember.get.GetAllCastMembersByIdUseCase;
import com.helion.catalog.application.category.get.GetAllCategoriesByIdUseCase;
import com.helion.catalog.application.genre.get.GetAllGenresByIdUseCase;
import com.helion.catalog.application.video.list.ListVideoUseCase;
import com.helion.catalog.application.video.save.SaveVideoUseCase;
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
public class VideoGraphQLControllerIT {
    @MockBean
    private ListVideoUseCase listVideoUseCase;

    @MockBean
    private GetAllCastMembersByIdUseCase getAllCastMembersByIdUseCase;

    @MockBean
    private GetAllCategoriesByIdUseCase getAllCategoriesByIdUseCase;

    @MockBean
    private GetAllGenresByIdUseCase getAllGenresByIdUseCase;

    @MockBean
    private SaveVideoUseCase saveVideoUseCase;

    @Autowired
    private WebGraphQlHandler webGraphQlHandler;

    @Autowired
    private WebGraphQlSecurityInterceptor interceptor;

    @Test
    public void givenAnonymousUser_whenQueries_shouldReturnUnauthorized() {
        interceptor.setAuthorities();
        final var document = "query videos { videos { id } }";
        final var graphQlTesters = WebGraphQlTester.create(webGraphQlHandler);
        graphQlTesters.document(document).execute()
                .errors().expect(err -> "Unauthorized".equals(err.getMessage()) && "videos".equals(err.getPath()))
                .verify();
    }

    @Test
    public void givenUserWithAdminRole_whenQueries_shouldReturnResult() {
        interceptor.setAuthorities(Roles.ROLE_ADMIN);

        final var java21 = Fixture.Videos.java21();
        final var systemDesign = Fixture.Videos.systemDesign();

        final var categories = List.of(new GetAllCategoriesByIdUseCase.Output(Fixture.Categories.lives()));
        final var castMembers = List.of(new GetAllCastMembersByIdUseCase.Output(Fixture.CastMembers.actor()));
        final var genres = List.of(new GetAllGenresByIdUseCase.Output(Fixture.Genres.tech()));

        final var expectedVideos = List.of(
                ListVideoUseCase.Output.from(java21),
                ListVideoUseCase.Output.from(systemDesign)
        );

        when(this.listVideoUseCase.execute(any()))
                .thenReturn(new Pagination<>(0, 10, expectedVideos.size(), expectedVideos));

        when(this.getAllCastMembersByIdUseCase.execute(any())).thenReturn(castMembers);
        when(this.getAllCategoriesByIdUseCase.execute(any())).thenReturn(categories);
        when(this.getAllGenresByIdUseCase.execute(any())).thenReturn(genres);

        final var expectedIds = expectedVideos.stream().map(ListVideoUseCase.Output::id).toList();

        when(this.listVideoUseCase.execute(any()))
                .thenReturn(new Pagination<>(0, 10, expectedVideos.size(), expectedVideos));

        final var document = "query videos { videos { id castMembers { id } categories { id } genres { id } } }";
        final var graphQlTesters = WebGraphQlTester.create(webGraphQlHandler);
        graphQlTesters.document(document).execute()
                .errors().verify()
                .path("videos[*].id").entityList(String.class).isEqualTo(expectedIds);
    }

    @Test
    public void givenUserWithSubscriberRole_whenQueries_shouldReturnResult() {
        interceptor.setAuthorities(Roles.ROLE_SUBSCRIBER);

        final var java21 = Fixture.Videos.java21();
        final var systemDesign = Fixture.Videos.systemDesign();

        final var categories = List.of(new GetAllCategoriesByIdUseCase.Output(Fixture.Categories.lives()));
        final var castMembers = List.of(new GetAllCastMembersByIdUseCase.Output(Fixture.CastMembers.actor()));
        final var genres = List.of(new GetAllGenresByIdUseCase.Output(Fixture.Genres.tech()));

        final var expectedVideos = List.of(
                ListVideoUseCase.Output.from(java21),
                ListVideoUseCase.Output.from(systemDesign)
        );

        when(this.listVideoUseCase.execute(any()))
                .thenReturn(new Pagination<>(0, 10, expectedVideos.size(), expectedVideos));

        when(this.getAllCastMembersByIdUseCase.execute(any())).thenReturn(castMembers);
        when(this.getAllCategoriesByIdUseCase.execute(any())).thenReturn(categories);
        when(this.getAllGenresByIdUseCase.execute(any())).thenReturn(genres);

        final var expectedIds = expectedVideos.stream().map(ListVideoUseCase.Output::id).toList();

        when(this.listVideoUseCase.execute(any()))
                .thenReturn(new Pagination<>(0, 10, expectedVideos.size(), expectedVideos));

        final var document = "query videos { videos { id castMembers { id } categories { id } genres { id } } }";
        final var graphQlTesters = WebGraphQlTester.create(webGraphQlHandler);
        graphQlTesters.document(document).execute()
                .errors().verify()
                .path("videos[*].id").entityList(String.class).isEqualTo(expectedIds);
    }

    @Test
    public void givenUserWithVideosRole_whenQueries_shouldReturnResult() {
        interceptor.setAuthorities(Roles.ROLE_VIDEOS);

        final var java21 = Fixture.Videos.java21();
        final var systemDesign = Fixture.Videos.systemDesign();

        final var categories = List.of(new GetAllCategoriesByIdUseCase.Output(Fixture.Categories.lives()));
        final var castMembers = List.of(new GetAllCastMembersByIdUseCase.Output(Fixture.CastMembers.actor()));
        final var genres = List.of(new GetAllGenresByIdUseCase.Output(Fixture.Genres.tech()));

        final var expectedVideos = List.of(
                ListVideoUseCase.Output.from(java21),
                ListVideoUseCase.Output.from(systemDesign)
        );

        when(this.listVideoUseCase.execute(any()))
                .thenReturn(new Pagination<>(0, 10, expectedVideos.size(), expectedVideos));

        when(this.getAllCastMembersByIdUseCase.execute(any())).thenReturn(castMembers);
        when(this.getAllCategoriesByIdUseCase.execute(any())).thenReturn(categories);
        when(this.getAllGenresByIdUseCase.execute(any())).thenReturn(genres);

        final var expectedIds = expectedVideos.stream().map(ListVideoUseCase.Output::id).toList();

        when(this.listVideoUseCase.execute(any()))
                .thenReturn(new Pagination<>(0, 10, expectedVideos.size(), expectedVideos));

        final var document = "query videos { videos { id castMembers { id } categories { id } genres { id } } }";
        final var graphQlTesters = WebGraphQlTester.create(webGraphQlHandler);
        graphQlTesters.document(document).execute()
                .errors().verify()
                .path("videos[*].id").entityList(String.class).isEqualTo(expectedIds);
    }
}
