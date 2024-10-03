package com.helion.catalog.infrastructure.graphql;

import com.helion.catalog.IntegrationTest;
import com.helion.catalog.WebGraphQlSecurityInterceptor;
import com.helion.catalog.application.category.list.ListCategoryOutput;
import com.helion.catalog.application.category.list.ListCategoryUseCase;
import com.helion.catalog.application.category.save.SaveCategoryUseCase;
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

@IntegrationTest
public class CategoryGraphQLControllerIT {
    @MockBean
    private ListCategoryUseCase listCategoryUseCase;

    @MockBean
    private SaveCategoryUseCase saveCategoryUseCase;

    @Autowired
    private WebGraphQlHandler webGraphQlHandler;

    @Autowired
    private WebGraphQlSecurityInterceptor interceptor;

    @Test
    public void givenAnonymousUser_whenQueries_shouldReturnUnauthorized() {
        interceptor.setAuthorities();
        final var document = "query categories { categories { id } }";
        final var graphQlTesters = WebGraphQlTester.create(webGraphQlHandler);
        graphQlTesters.document(document).execute()
                .errors().expect(err -> "Unauthorized".equals(err.getMessage()) && "categories".equals(err.getPath()))
                .verify();
    }

    @Test
    public void givenUserWithAdminRole_whenQueries_shouldReturnResult() {
        interceptor.setAuthorities(Roles.ROLE_ADMIN);

        final var categories = List.of(
                ListCategoryOutput.from(Fixture.Categories.lives()),
                ListCategoryOutput.from(Fixture.Categories.aulas())
        );

        final var expectedIds = categories.stream().map(ListCategoryOutput::id).toList();

        when(this.listCategoryUseCase.execute(any()))
                .thenReturn(new Pagination<>(0, 10, categories.size(), categories));

        final var document = "query categories { categories { id } }";
        final var graphQlTesters = WebGraphQlTester.create(webGraphQlHandler);
        graphQlTesters.document(document).execute()
                .errors().verify()
                .path("categories[*].id").entityList(String.class).isEqualTo(expectedIds);
    }

    @Test
    public void givenUserWithSubscriberRole_whenQueries_shouldReturnResult() {
        interceptor.setAuthorities(Roles.ROLE_SUBSCRIBER);

        final var categories = List.of(
                ListCategoryOutput.from(Fixture.Categories.lives()),
                ListCategoryOutput.from(Fixture.Categories.aulas())
        );

        final var expectedIds = categories.stream().map(ListCategoryOutput::id).toList();

        when(this.listCategoryUseCase.execute(any()))
                .thenReturn(new Pagination<>(0, 10, categories.size(), categories));

        final var document = "query categories { categories { id } }";
        final var graphQlTesters = WebGraphQlTester.create(webGraphQlHandler);
        graphQlTesters.document(document).execute()
                .errors().verify()
                .path("categories[*].id").entityList(String.class).isEqualTo(expectedIds);
    }

    @Test
    public void givenUserWithCategoriesRole_whenQueries_shouldReturnResult() {
        interceptor.setAuthorities(Roles.ROLE_CATEGORIES);

        final var categories = List.of(
                ListCategoryOutput.from(Fixture.Categories.lives()),
                ListCategoryOutput.from(Fixture.Categories.aulas())
        );

        final var expectedIds = categories.stream().map(ListCategoryOutput::id).toList();

        when(this.listCategoryUseCase.execute(any()))
                .thenReturn(new Pagination<>(0, 10, categories.size(), categories));

        final var document = "query categories { categories { id } }";
        final var graphQlTesters = WebGraphQlTester.create(webGraphQlHandler);
        graphQlTesters.document(document).execute()
                .errors().verify()
                .path("categories[*].id").entityList(String.class).isEqualTo(expectedIds);
    }
}
