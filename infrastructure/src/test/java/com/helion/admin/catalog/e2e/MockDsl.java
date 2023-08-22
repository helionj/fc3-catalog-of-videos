package com.helion.admin.catalog.e2e;

import com.helion.admin.catalog.ApiTest;
import com.helion.admin.catalog.domain.Identifier;
import com.helion.admin.catalog.domain.castmember.CastMemberID;
import com.helion.admin.catalog.domain.castmember.CastMemberType;
import com.helion.admin.catalog.domain.category.CategoryID;
import com.helion.admin.catalog.domain.genre.GenreID;
import com.helion.admin.catalog.infrastructure.castmember.models.CastMemberResponse;
import com.helion.admin.catalog.infrastructure.castmember.models.CreateCastMemberRequest;
import com.helion.admin.catalog.infrastructure.castmember.models.UpdateCastMemberRequest;
import com.helion.admin.catalog.infrastructure.category.models.CreateCategoryRequest;
import com.helion.admin.catalog.infrastructure.configuration.json.Json;
import com.helion.admin.catalog.infrastructure.genre.models.CreateGenreRequest;
import com.helion.admin.catalog.infrastructure.genre.models.GenreResponse;
import com.helion.admin.catalog.infrastructure.genre.models.UpdateGenreRequest;
import org.jetbrains.annotations.NotNull;
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


    /**
     * Genre
     */

    default GenreID givenAGenre(final String aName, final List<CategoryID> aCategories, boolean isActive) throws Exception {

        final var aRequestBody = new CreateGenreRequest(aName, mapTo(aCategories, CategoryID::getValue), isActive);
        final var actualId = this.given("/genres", aRequestBody);
        return GenreID.from(actualId);
    }
    default ResultActions listGenres(final int page, final int perPage, final String search) throws Exception {
        return listGenres(page, perPage, search, null, null);
    }

    default ResultActions listGenres(final int page, final int perPage) throws Exception {
        return listGenres(page, perPage, null, null, null);
    }

    default ResultActions listGenres(final int page, final int perPage, final String search, final String sort, final String direction) throws Exception {
        return this.list("/genres", page, perPage, search, sort, direction);
    }


    default GenreResponse retrieveAGenre(final GenreID anId) throws Exception {
        return this.retrieve("/genres/", anId, GenreResponse.class);
    }

    default ResultActions updateAGenre(final GenreID anId, final UpdateGenreRequest aRequest) throws Exception {
        return this.update("/genres/", anId, aRequest);
    }
    default ResultActions deleteAGenre(final GenreID anId) throws Exception {
        return this.delete("/genres/", anId);
    }

    /**
     * Category
     */
    default CategoryID givenACategory(final String aName, final String aDescription, boolean isActive) throws Exception {

        final var aRequestBody = new CreateCategoryRequest(aName, aDescription, isActive);
        final var actualId = this.given("/categories", aRequestBody);
        return CategoryID.from(actualId);
    }

    default ResultActions listCategories(final int page, final int perPage, final String search) throws Exception {
        return listCategories(page, perPage, search, null, null);
    }

    default ResultActions listCategories(final int page, final int perPage) throws Exception {
        return listCategories(page, perPage, null, null, null);
    }

    default ResultActions listCategories(final int page, final int perPage, final String search, final String sort, final String direction) throws Exception {
        return this.list("/categories", page, perPage, search, sort, direction);
    }

    /**
     * CastMember
     */

    default CastMemberID givenACastMember(final String aName, final CastMemberType aType) throws Exception {

        final var aRequestBody = new CreateCastMemberRequest(aName, aType);
        final var actualId = this.given("/cast_members", aRequestBody);
        return CastMemberID.from(actualId);
    }

    default ResultActions givenACastMemberResult(final String aName, final CastMemberType aType) throws Exception {

        final var aRequestBody = new CreateCastMemberRequest(aName, aType);
        return this.givenResult("/cast_members", aRequestBody);
    }

    default CastMemberResponse retrieveACastMember(final CastMemberID anId) throws Exception {
        return this.retrieve("/cast_members/", anId, CastMemberResponse.class);
    }

    default ResultActions retrieveACastMemberResult(final CastMemberID anId) throws Exception {
        return this.retrieveResult("/cast_members/", anId);
    }

    default ResultActions listCastMembers(final int page, final int perPage, final String search) throws Exception {
        return listCastMembers(page, perPage, search, null, null);
    }

    default ResultActions listCastMembers(final int page, final int perPage) throws Exception {
        return listCastMembers(page, perPage, null, null, null);
    }

    default ResultActions listCastMembers(final int page, final int perPage, final String search, final String sort, final String direction) throws Exception {
       return this.list("/cast_members", page, perPage, search, sort, direction);
    }

    default ResultActions updateACastMember(final CastMemberID anId, final UpdateCastMemberRequest aRequest) throws Exception {
        return this.update("/cast_members/", anId, aRequest);
    }

    default ResultActions deleteACastMember(final CastMemberID anId) throws Exception {
        return this.delete("/cast_members/", anId);
    }


    private String given(final String url, final Object body) throws Exception {

        final var aRequest = post(url)
                .with(ApiTest.ADMIN_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(body));
        final var actualId = this.mvc().perform(aRequest)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse().getHeader("Location")
                .replace("%s/".formatted(url), "");
        return actualId;
    }

    private ResultActions givenResult(final String url, final Object body) throws Exception {

        final var aRequest = post(url)
                .with(ApiTest.ADMIN_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(body));
        return  this.mvc().perform(aRequest);

    }



    private <T> T retrieve(final String url, final Identifier anId, final Class<T> clazz) throws Exception {
        final var aRequest = get(url + anId.getValue())
                .with(ApiTest.ADMIN_JWT)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8);

        final var json = this.mvc().perform(aRequest)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        return Json.readValue(json, clazz);
    }

    private ResultActions retrieveResult(final String url, final Identifier anId) throws Exception {
        final var aRequest = get(url + anId.getValue())
                .with(ApiTest.ADMIN_JWT)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8);

        return this.mvc().perform(aRequest);

    }

    default <A, D> List<D> mapTo(List<A> actual, final Function<A, D> mapper ){
        return actual.stream()
                .map(mapper)
                .toList();
    }


    private ResultActions update(final String url, final Identifier anId, final Object aRequestBody) throws Exception {
        final var aRequest = put(url + anId.getValue())
                .with(ApiTest.ADMIN_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(aRequestBody));

        return this.mvc().perform(aRequest);
    }

    private ResultActions delete(final String url, final Identifier anId) throws Exception {
        final var aRequest = MockMvcRequestBuilders.delete(url + anId.getValue())
                .with(ApiTest.ADMIN_JWT)
                .contentType(MediaType.APPLICATION_JSON);

        return this.mvc().perform(aRequest);
    }

    private ResultActions list(String url, int page, int perPage, String search, String sort, String direction) throws Exception {
        final var aRequest = get(url)
                .with(ApiTest.ADMIN_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("page",String.valueOf(page))
                .queryParam("perPage", String.valueOf(perPage))
                .queryParam("search", search)
                .queryParam("sort", sort)
                .queryParam("dir", direction);

        return this.mvc().perform(aRequest);
    }





}
