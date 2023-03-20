package com.helion.admin.catalog.infrastructure.category.models;

import com.helion.admin.catalog.infrastructure.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.time.Instant;

@JacksonTest
public class CreateCategoryRequestTest {

    @Autowired
    private JacksonTester<CreateCategoryRequest> json;

    @Test
    public void testMarshall() throws IOException {

        final var expectedName= "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;


        final var request = new CreateCategoryRequest(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        final var actualJson = this.json.write(request);

        Assertions.assertThat(actualJson)

                .hasJsonPathValue("$.name", expectedName)
                .hasJsonPathValue("$.description", expectedDescription)
                .hasJsonPathValue("$.is_active", expectedIsActive);



    }

    @Test
    public void testUnMarshall() throws IOException {

        final var expectedName= "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var json = """
                {
                    "name": "%s",
                    "description": "%s",
                    "is_active": %s
                }
                """.formatted(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        final var actualJson = this.json.parse(json);

        Assertions.assertThat(actualJson)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("description", expectedDescription)
                .hasFieldOrPropertyWithValue("active", expectedIsActive);
    }
}
