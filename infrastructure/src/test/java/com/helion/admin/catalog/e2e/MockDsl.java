package com.helion.admin.catalog.e2e;

import com.helion.admin.catalog.domain.Identifier;
import com.helion.admin.catalog.domain.castmember.CastMemberType;
import com.helion.admin.catalog.domain.category.CategoryID;
import com.helion.admin.catalog.domain.genre.GenreID;
import com.helion.admin.catalog.infrastructure.category.models.CreateCategoryRequest;
import com.helion.admin.catalog.infrastructure.configuration.json.Json;
import com.helion.admin.catalog.infrastructure.genre.models.CreateGenreRequest;
import com.helion.admin.catalog.infrastructure.genre.models.GenreResponse;
import com.helion.admin.catalog.infrastructure.genre.models.UpdateGenreRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.function.Function;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public interface MockDsl {

    MockMvc mvc();


    default CategoryID givenACategory(final String aName, final String aDescription, boolean isActive) throws Exception {

        final var aRequestBody = new CreateCategoryRequest(aName, aDescription, isActive);
        final var actualId = this.given("/categories", aRequestBody);
        return CategoryID.from(actualId);
    }

    default GenreID givenAGenre(final String aName, final List<CategoryID> aCategories, boolean isActive) throws Exception {

        final var aRequestBody = new CreateGenreRequest(aName, mapTo(aCategories, CategoryID::getValue), isActive);
        final var actualId = this.given("/genres", aRequestBody);
        return GenreID.from(actualId);
    }
    private String given(final String url, final Object body) throws Exception {

        final var aRequest = post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(body));
        final var actualId = this.mvc().perform(aRequest)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse().getHeader("Location")
                .replace("%s/".formatted(url), "");
        return actualId;
    }

    default ResultActions listGenres(final int page, final int perPage, final String search) throws Exception {
        return listGenres(page, perPage, search, null, null);
    }

    default ResultActions listGenres(final int page, final int perPage) throws Exception {
        return listGenres(page, perPage, null, null, null);
    }

    default ResultActions listGenres(final int page, final int perPage, final String search, final String sort, final String direction) throws Exception {
        final var aRequest = get("/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("page",String.valueOf(page))
                .queryParam("perPage", String.valueOf(perPage))
                .queryParam("search", search)
                .queryParam("sort", sort)
                .queryParam("dir", direction);

        return this.mvc().perform(aRequest);
    }

    default GenreResponse retrieveAGenre(final GenreID anId) throws Exception {
        return this.retrieve("/genres/", anId, GenreResponse.class);
    }

    private <T> T retrieve(final String url, final Identifier anId, final Class<T> clazz) throws Exception {
        final var aRequest = get(url + anId.getValue())
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8);

        final var json = this.mvc().perform(aRequest)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        return Json.readValue(json, clazz);
    }

    default <A, D> List<D> mapTo(List<A> actual, final Function<A, D> mapper ){
        return actual.stream()
                .map(mapper)
                .toList();
    }

    default ResultActions updateAGenre(final GenreID anId, final UpdateGenreRequest aRequest) throws Exception {
        return this.update("/genres/", anId, aRequest);
    }
    private ResultActions update(final String url, final Identifier anId, final Object aRequestBody) throws Exception {
        final var aRequest = put(url + anId.getValue())
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(aRequestBody));

        return this.mvc().perform(aRequest);
    }

    default ResultActions deleteAGenre(final GenreID anId) throws Exception {
        return this.delete("/genres/", anId);
    }

    private ResultActions delete(final String url, final Identifier anId) throws Exception {
        final var aRequest = MockMvcRequestBuilders.delete(url + anId.getValue())
                .contentType(MediaType.APPLICATION_JSON);

        return this.mvc().perform(aRequest);
    }



}
