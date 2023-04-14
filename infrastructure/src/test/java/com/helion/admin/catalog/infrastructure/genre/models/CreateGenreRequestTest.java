package com.helion.admin.catalog.infrastructure.genre.models;

import com.helion.admin.catalog.infrastructure.JacksonTest;
import com.helion.admin.catalog.infrastructure.category.models.CreateCategoryRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.util.List;

@JacksonTest
public class CreateGenreRequestTest {

    @Autowired
    private JacksonTester<CreateGenreRequest> json;

    @Test
    public void testMarshall() throws IOException {

        final var expectedName= "Ação";
        final var expectedCategories = List.of("123");
        final var expectedIsActive = true;


        final var request = new CreateGenreRequest(
                expectedName,
                expectedCategories,
                expectedIsActive
        );

        final var actualJson = this.json.write(request);

        Assertions.assertThat(actualJson)

                .hasJsonPathValue("$.name", expectedName)
                .hasJsonPathValue("$.categories_id", expectedCategories)
                .hasJsonPathValue("$.is_active", expectedIsActive);



    }

    @Test
    public void testUnMarshall() throws IOException {

        final var expectedName= "Filmes";
        final var expectedCategory = "123";
        final var expectedIsActive = true;

        final var json = """
                {
                    "name": "%s",
                    "categories_id": ["%s"],
                    "is_active": %s
                }
                """.formatted(
                expectedName,
                expectedCategory,
                expectedIsActive
        );

        final var actualJson = this.json.parse(json);

        Assertions.assertThat(actualJson)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("categories", List.of(expectedCategory))
                .hasFieldOrPropertyWithValue("active", expectedIsActive);
    }
}
