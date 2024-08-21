package com.helion.catalog.infrastructure.genre.models;

import com.helion.catalog.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.time.Instant;
import java.util.Set;

@JacksonTest
class GenreDTOTest {

    @Autowired
    private JacksonTester<GenreDTO> json;

    @Test
    public void testUnMarshall() throws IOException {

        final var genreResponse = """
                {
                    "id": "d3e195e2fb3f44b08d5eb63736e89a95",
                    "name": "Lecture",
                    "categories_id": [
                        "e6a1143804214129afcdc161d8aaf2ed",
                        "7f3671239b7d497a81b416f3c6a360de"
                    ],
                    "is_active": false,
                    "created_at": "2024-06-28T15:14:45.983066Z",
                    "updated_at": "2024-06-28T15:14:45.983093Z",
                    "deleted_at": "2024-06-28T15:18:45.983093Z"
                }    
                """;
        final var actualGenre = this.json.parse(genreResponse);

        Assertions.assertThat(actualGenre)
                .hasFieldOrPropertyWithValue("id", "d3e195e2fb3f44b08d5eb63736e89a95")
                .hasFieldOrPropertyWithValue("name", "Lecture")
                .hasFieldOrPropertyWithValue("categoriesId", Set.of("e6a1143804214129afcdc161d8aaf2ed","7f3671239b7d497a81b416f3c6a360de"))
                .hasFieldOrPropertyWithValue("isActive", false)
                .hasFieldOrPropertyWithValue("createdAt", Instant.parse("2024-06-28T15:14:45.983066Z"))
                .hasFieldOrPropertyWithValue("updatedAt",Instant.parse("2024-06-28T15:14:45.983093Z"))
                .hasFieldOrPropertyWithValue("deletedAt", Instant.parse("2024-06-28T15:18:45.983093Z"));
    }


}