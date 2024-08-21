package com.helion.catalog.infrastructure.graphql;

import com.helion.catalog.application.category.list.ListCategoryOutput;
import com.helion.catalog.application.category.list.ListCategoryUseCase;
import com.helion.catalog.application.category.save.SaveCategoryUseCase;
import com.helion.catalog.domain.Fixture;
import com.helion.catalog.domain.category.Category;
import com.helion.catalog.domain.category.CategorySearchQuery;
import com.helion.catalog.domain.pagination.Pagination;
import com.helion.catalog.domain.utils.IdUtils;
import com.helion.catalog.domain.utils.InstantUtils;
import com.helion.catalog.GraphQLControllerTest;
import com.helion.catalog.infrastructure.category.GqlCategoryPresenter;
import com.helion.catalog.infrastructure.category.models.GqlCategory;
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

@GraphQLControllerTest(controllers=CategoryGraphQLController.class)
public class CategoryGraphQLControllerTest {

    @MockBean
    private ListCategoryUseCase listCategoryUseCase;

    @MockBean
    private SaveCategoryUseCase saveCategoryUseCase;

    @Autowired
    private GraphQlTester graphql;



    @Test
    public void givenDefaultArgumentsWhenCallsListCategories_shouldReturn(){

        final var categories =
                List.of(
                        ListCategoryOutput.from(Fixture.Categories.aulas()),
                        ListCategoryOutput.from(Fixture.Categories.lives())
                );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedSort = "name";
        final var expectedSearch = "";
        final var expectedDirection = "asc";

        when(this.listCategoryUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, categories.size(), categories));

        final var query = """
                {
                    categories {
                        id
                        name
                        description
                    }
                }
                """;
        final var expectedCategories = categories.stream().map(GqlCategoryPresenter::present).toList();
        final var res = this.graphql.document(query).execute();

        final var actualCategories = res.path("categories")
                .entityList(GqlCategory.class)
                .get();
        Assertions.assertTrue(actualCategories.size() == expectedCategories.size()
                && actualCategories.containsAll(expectedCategories));

        final var capturer = ArgumentCaptor.forClass(CategorySearchQuery.class);
        verify(this.listCategoryUseCase,times(1)).execute(capturer.capture());

        final var actualQuery = capturer.getValue();

        Assertions.assertEquals(expectedPage, actualQuery.page());
        Assertions.assertEquals(expectedPerPage, actualQuery.perPage());
        Assertions.assertEquals(expectedSort, actualQuery.sort());
        Assertions.assertEquals(expectedDirection, actualQuery.direction());
        Assertions.assertEquals(expectedSearch, actualQuery.terms());
    }

    @Test
    public void givenCustomArgumentsWhenCallsListCategories_shouldReturn(){

        final var categories =
                List.of(
                        ListCategoryOutput.from(Fixture.Categories.aulas()),
                        ListCategoryOutput.from(Fixture.Categories.lives())
                );

        final var expectedCategories = categories.stream().map(GqlCategoryPresenter::present).toList();

        final var expectedPage = 2;
        final var expectedPerPage = 15;
        final var expectedSort = "id";
        final var expectedSearch = "asd";
        final var expectedDirection = "desc";

        when(this.listCategoryUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, categories.size(), categories));

        final var query = """
                query AllQueries($search: String, $page: Int, $perPage: Int,  $sort: String, $direction: String) {
                    categories(search: $search, page: $page, perPage: $perPage, sort: $sort, direction: $direction) {
                        id
                        name
                        description
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

        final var actualCategories = res.path("categories")
                .entityList(GqlCategory.class)
                .get();
        Assertions.assertTrue(actualCategories.size() == expectedCategories.size()
                && actualCategories.containsAll(expectedCategories));

        final var capturer = ArgumentCaptor.forClass(CategorySearchQuery.class);
        verify(this.listCategoryUseCase,times(1)).execute(capturer.capture());

        final var actualQuery = capturer.getValue();

        Assertions.assertEquals(expectedPage, actualQuery.page());
        Assertions.assertEquals(expectedPerPage, actualQuery.perPage());
        Assertions.assertEquals(expectedSort, actualQuery.sort());
        Assertions.assertEquals(expectedDirection, actualQuery.direction());
        Assertions.assertEquals(expectedSearch, actualQuery.terms());
    }

    @Test
    public void givenCategoryInputWhenCallsSaveCategoryMutation_shouldPersistAndReturn(){

        final var expectedId = IdUtils.uniqueId();
        final var expectedName= "Aulas";
        final var expectedDescription = "Aulas de Java";
        final var expectedActive = false;
        final var expectedCreatedAt = InstantUtils.now();
        final var expectedUpdatedAt = InstantUtils.now();
        final var expectedDeletedAt = InstantUtils.now();

        final var input = Map.of(
                "id", expectedId,
                "name", expectedName,
                "description", expectedDescription,
                "active", expectedActive,
                "createdAt", expectedCreatedAt.toString(),
                "updatedAt", expectedUpdatedAt.toString(),
                "deletedAt", expectedDeletedAt.toString()

        );

        doAnswer(returnsFirstArg()).when(saveCategoryUseCase).execute(any());

        final var query = """
                mutation SaveCategory($input: CategoryInput!){
                    category: saveCategory(input: $input){
                        id
                        name
                        description
                    }
                    
                }
                """;
        final var res = this.graphql.document(query)
                .variable("input", input)
                .execute()
                .path("category.id").entity(String.class).isEqualTo(expectedId)
                .path("category.name").entity(String.class).isEqualTo(expectedName)
                .path("category.description").entity(String.class).isEqualTo(expectedDescription);




        final var capturer = ArgumentCaptor.forClass(Category.class);
        verify(this.saveCategoryUseCase,times(1)).execute(capturer.capture());

        final var actualCategory = capturer.getValue();

        Assertions.assertEquals(expectedId, actualCategory.id());
        Assertions.assertEquals(expectedName, actualCategory.name());
        Assertions.assertEquals(expectedDescription, actualCategory.description());
        Assertions.assertEquals(expectedActive, actualCategory.isActive());
        Assertions.assertEquals(expectedCreatedAt, actualCategory.createdAt());
        Assertions.assertEquals(expectedUpdatedAt, actualCategory.updatedAt());
        Assertions.assertEquals(expectedDeletedAt, actualCategory.deletedAt());

    }


}
