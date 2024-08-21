package com.helion.catalog.infrastructure.graphql;

import com.helion.catalog.GraphQLControllerTest;
import com.helion.catalog.application.genre.list.ListGenreUseCase;
import com.helion.catalog.application.genre.save.SaveGenreUseCase;
import com.helion.catalog.domain.Fixture;
import com.helion.catalog.domain.pagination.Pagination;
import com.helion.catalog.domain.utils.IdUtils;
import com.helion.catalog.domain.utils.InstantUtils;
import com.helion.catalog.infrastructure.genre.GqlGenrePresenter;
import com.helion.catalog.infrastructure.genre.models.GqlGenre;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@GraphQLControllerTest(controllers=GenreGraphQLController.class)
public class GenreGraphQLControllerTest {

    @MockBean
    private ListGenreUseCase listGenreUseCase;
    @MockBean
    private SaveGenreUseCase saveGenreUseCase;

    @Autowired
    private GraphQlTester graphql;



    @Test
    public void givenDefaultArgumentsWhenCallsGenres_shouldReturn(){

        final var genres =
                List.of(
                        ListGenreUseCase.Output.from(Fixture.Genres.business()),
                        ListGenreUseCase.Output.from(Fixture.Genres.marketing())
                );
        final var expectedGenres = genres.stream().map(GqlGenrePresenter::present).toList();
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedSort = "name";
        final var expectedSearch = "";
        final var expectedDirection = "asc";
        final var expectedCategories = Set.of();

        when(this.listGenreUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, genres.size(), genres));

        final var query = """
                {
                    genres {
                        id
                        name
                        categories
                        active
                        createdAt
                        updatedAt
                        deletedAt
              
                    }
                }
                """;
        final var res = this.graphql.document(query).execute();

        final var actualGenres = res.path("genres")
                .entityList(GqlGenre.class)
                .get();
        Assertions.assertTrue(actualGenres.size() == expectedGenres.size()
                && actualGenres.containsAll(expectedGenres));

        final var capturer = ArgumentCaptor.forClass(ListGenreUseCase.Input.class);
        verify(this.listGenreUseCase,times(1)).execute(capturer.capture());

        final var actualQuery = capturer.getValue();

        Assertions.assertEquals(expectedPage, actualQuery.page());
        Assertions.assertEquals(expectedPerPage, actualQuery.perPage());
        Assertions.assertEquals(expectedSort, actualQuery.sort());
        Assertions.assertEquals(expectedDirection, actualQuery.direction());
        Assertions.assertEquals(expectedSearch, actualQuery.terms());
        Assertions.assertEquals(expectedCategories, actualQuery.categories());
    }

    @Test
    public void givenCustomArgumentsWhenCallsListGenres_shouldReturn(){

        final var genres =
                List.of(
                        ListGenreUseCase.Output.from(Fixture.Genres.business()),
                        ListGenreUseCase.Output.from(Fixture.Genres.marketing())
                );
        final var expectedGenres = genres.stream().map(GqlGenrePresenter::present).toList();
        final var expectedPage = 2;
        final var expectedPerPage = 15;
        final var expectedSort = "id";
        final var expectedSearch = "asd";
        final var expectedDirection = "desc";
        final var expectedCategories = Set.of("c1");

        when(this.listGenreUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, genres.size(), genres));

        final var query = """
                query AllQueries($search: String, $page: Int, $perPage: Int,  $sort: String, $direction: String, $categories: [String]) {
                    genres(search: $search, page: $page, perPage: $perPage, sort: $sort, direction: $direction, categories: $categories) {
                        id
                        name
                        categories
                        active
                        createdAt
                        updatedAt
                        deletedAt
                    }
                }
                """;
        final var res = this.graphql.document(query)
                .variable("search", expectedSearch)
                .variable("page", expectedPage)
                .variable("perPage", expectedPerPage)
                .variable("sort", expectedSort)
                .variable("direction", expectedDirection)
                .variable("categories", expectedCategories)
                .execute();

        final var actualGenres = res.path("genres")
                .entityList(GqlGenre.class)
                .get();
        Assertions.assertTrue(actualGenres.size() == expectedGenres.size()
                && actualGenres.containsAll(expectedGenres));

        final var capturer = ArgumentCaptor.forClass(ListGenreUseCase.Input.class);
        verify(this.listGenreUseCase,times(1)).execute(capturer.capture());

        final var actualQuery = capturer.getValue();

        Assertions.assertEquals(expectedPage, actualQuery.page());
        Assertions.assertEquals(expectedPerPage, actualQuery.perPage());
        Assertions.assertEquals(expectedSort, actualQuery.sort());
        Assertions.assertEquals(expectedDirection, actualQuery.direction());
        Assertions.assertEquals(expectedSearch, actualQuery.terms());
        Assertions.assertEquals(expectedCategories, actualQuery.categories());
    }


    @Test
    public void givenGenreInputWhenCallsSaveGenreMutation_shouldPersistAndReturn() {

        final var expectedId = IdUtils.uniqueId();
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = Set.of("c1", "c2");
        final var expectedDates = InstantUtils.now();

        final var input = Map.of(
                "id", expectedId,
                "name", expectedName,
                "active", String.valueOf(expectedIsActive),
                "categories", expectedCategories,
                "createdAt", expectedDates.toString(),
                "updatedAt", expectedDates.toString()
        );

        final var query = """
                mutation SaveGenre($input: GenreInput!) {
                    genre: saveGenre(input: $input) {
                        id
                    }
                }
                """;

        doReturn(new SaveGenreUseCase.Output(expectedId)).when(saveGenreUseCase).execute(any());

        // when
        this.graphql.document(query)
                .variable("input", input)
                .execute()
                .path("genre.id").entity(String.class).isEqualTo(expectedId);

        // then
        final var capturer = ArgumentCaptor.forClass(SaveGenreUseCase.Input.class);

        verify(this.saveGenreUseCase, times(1)).execute(capturer.capture());

        final var actualGenre = capturer.getValue();
        Assertions.assertEquals(expectedId, actualGenre.id());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedIsActive, actualGenre.active());
        Assertions.assertEquals(expectedCategories, actualGenre.categories());
        Assertions.assertEquals(expectedDates, actualGenre.createdAt());
        Assertions.assertEquals(expectedDates, actualGenre.updatedAt());
        Assertions.assertNull(actualGenre.deletedAt());
    }

}
