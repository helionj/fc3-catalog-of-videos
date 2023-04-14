package com.helion.admin.catalog.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.helion.admin.catalog.ControllerTest;
import com.helion.admin.catalog.application.genre.create.CreateGenreOutput;
import com.helion.admin.catalog.application.genre.create.CreateGenreUseCase;
import com.helion.admin.catalog.application.genre.delete.DeleteGenreUseCase;
import com.helion.admin.catalog.application.genre.retrieve.get.GenreOutput;
import com.helion.admin.catalog.application.genre.retrieve.get.GetGenreByIdUseCase;
import com.helion.admin.catalog.application.genre.retrieve.list.GenreListOutput;
import com.helion.admin.catalog.application.genre.retrieve.list.ListGenreUseCase;
import com.helion.admin.catalog.application.genre.update.UpdateGenreOutput;
import com.helion.admin.catalog.application.genre.update.UpdateGenreUseCase;
import com.helion.admin.catalog.domain.category.CategoryID;
import com.helion.admin.catalog.domain.category.pagination.Pagination;
import com.helion.admin.catalog.domain.category.pagination.SearchQuery;
import com.helion.admin.catalog.domain.exceptions.NotFoundException;
import com.helion.admin.catalog.domain.exceptions.NotificationException;
import com.helion.admin.catalog.domain.genre.Genre;
import com.helion.admin.catalog.domain.genre.GenreID;
import com.helion.admin.catalog.domain.validation.Error;
import com.helion.admin.catalog.domain.validation.handler.Notification;
import com.helion.admin.catalog.infrastructure.api.genre.GenreAPI;
import com.helion.admin.catalog.infrastructure.genre.models.CreateGenreRequest;
import com.helion.admin.catalog.infrastructure.genre.models.UpdateGenreRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = GenreAPI.class)
public class GenreAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateGenreUseCase createGenreUseCase;

    @MockBean
    private GetGenreByIdUseCase getGenreByIdUseCase;

    @MockBean
    private UpdateGenreUseCase updateGenreUseCase;

    @MockBean
    private DeleteGenreUseCase deleteGenreUseCase;


    @MockBean
    private ListGenreUseCase listGenreUseCase;

    @Test
    public void givenValidCommand_whenCallsCreateGenre_shouldReturnGenreID() throws Exception {

        final var expectedName = "Filmes";
        final var expectedIsActive = true;
        final var expectedCategories = List.<String>of("123", "456");
        final var expectedId = "123";

        final var aInput = new CreateGenreRequest(expectedName, expectedCategories, expectedIsActive );

        Mockito.when(createGenreUseCase.execute(any()))
                .thenReturn(CreateGenreOutput.from(expectedId));

        final var request = post("/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        final var response = this.mvc.perform(request).andDo(print());

        response.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/genres/" +expectedId))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", Matchers.equalTo(expectedId)));

        Mockito.verify(createGenreUseCase).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                &&  Objects.equals(expectedIsActive, cmd.isActive())
                &&  Objects.equals(expectedCategories, cmd.categories())
        ));

    }

    @Test
    public void givenAnInvalidName_whenCallsCreateGenre_thenShouldReturnNotification() throws Exception {
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedCategories = List.<String>of("123", "456");

        final var aInput = new CreateGenreRequest(expectedName, expectedCategories, expectedIsActive );


        when(createGenreUseCase.execute(any()))
                .thenThrow(new NotificationException("Error", Notification.create(new Error(expectedErrorMessage))));

        final var request = post("/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aInput));

        this.mvc.perform(request).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", Matchers.nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(createGenreUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedCategories, cmd.categories())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAnValidId_whenCallsGetGenreById_shouldReturnsAGenre() throws Exception {

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(CategoryID.from("123"), CategoryID.from("456"));
        final var aGenre = Genre.newGenre(expectedName, expectedIsActive);
        aGenre.addCategories(expectedCategories);
        final var expectedId = aGenre.getId().getValue();

        when(getGenreByIdUseCase.execute(any()))
                .thenReturn(GenreOutput.from(aGenre));

        final var request = get("/genres/{id}",expectedId)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)))
                .andExpect(jsonPath("$.name", equalTo(expectedName)))
                .andExpect(jsonPath("$.categories_id", equalTo(asString(expectedCategories))))
                .andExpect(jsonPath("$.is_active", equalTo(expectedIsActive)))
                .andExpect(jsonPath("$.created_at", equalTo(aGenre.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updated_at", equalTo(aGenre.getUpdatedAt().toString())))
                .andExpect(jsonPath("$.deleted_at", equalTo(aGenre.getDeletedAt())));

        verify(getGenreByIdUseCase,times(1)).execute(eq(expectedId));
    }

    @Test
    public void givenAiNValidId_whenCallsGetGenreById_shouldReturnsNotFound() throws Exception{

        final var expectedId = GenreID.from("123");
        final var expectedErrorMessage = "Genre with ID 123 was not found";


        when(getGenreByIdUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Genre.class, expectedId));

        final var request = get("/genres/{id}",expectedId.getValue())
                .contentType(MediaType.APPLICATION_JSON);;

        final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));
    }

    @Test
    public void givenValidCommand_whenCallsUpdateGenre_shouldReturnGenreID() throws Exception {

        final var expectedId = "123";
        final var expectedName = "Ação";
        final var expectedCategories = List.<String>of();
        final var expectedIsActive = true;

        when(updateGenreUseCase.execute(any()))
                .thenReturn(UpdateGenreOutput.from(expectedId));

        final var aCommand = new UpdateGenreRequest(expectedName, expectedCategories, expectedIsActive);

        final var request = put("/genres/{id}",expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));


        final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type",MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)));

        verify(updateGenreUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedCategories, cmd.categories())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenInValidId_whenCallsUpdateGenre_shouldReturnNotFoundException() throws Exception {

        final var expectedId = "not-found";
        final var expectedName = "Açao";
        final var expectedCategories = List.of("123", "456");
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Genre with ID not-found was not found";
        final var expectedErrorCount = 1;


        when(updateGenreUseCase.execute(any()))
                .thenThrow(new NotificationException("Error", Notification.create(new Error(expectedErrorMessage))));

        final var aCommand = new UpdateGenreRequest(expectedName, expectedCategories, expectedIsActive);

        final var request = put("/genres/{id}",expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));


        final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type",MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", Matchers.hasSize(expectedErrorCount)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(updateGenreUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedCategories, cmd.categories())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }
    @Test
    public void givenInValidName_whenCallsUpdateCategory_shouldReturnNotFoundException() throws Exception {

        final var expectedId = "123";
        final var expectedName = "Ação";
        final var expectedCategories = List.of("123", "456");
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;


        when(updateGenreUseCase.execute(any()))
                .thenThrow(new NotificationException("Error", Notification.create(new Error(expectedErrorMessage))));

        final var aCommand = new UpdateGenreRequest(expectedName, expectedCategories, expectedIsActive);

        final var request = put("/genres/{id}",expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));


        final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type",MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors",hasSize(expectedErrorCount)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(updateGenreUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedCategories, cmd.categories())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAnValidId_whenCallsDeleteGenreById_shouldReturnNoContent() throws Exception {

        final var expectedId = "123";


        doNothing().when(deleteGenreUseCase).execute(any());

        final var request = delete("/genres/{id}",expectedId)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

        response.andExpect(status().isNoContent());



        verify(deleteGenreUseCase,times(1)).execute(eq(expectedId));
    }

    @Test
    public void givenAValidParams_whenCallListGenres_shouldReturnGenres() throws Exception{

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "ação";
        final var expectedSort = "name";
        final var expectedDirection = "desc";
        final var expectedItemsCount = 1;
        final var expectedTotal = 1;


        final var aGenre = Genre.newGenre("Ação", true);

        final var expectedItems = List.of(GenreListOutput.from(aGenre));

        final var aQuery = new SearchQuery(
                expectedPage, expectedPerPage,expectedTerms,expectedSort,expectedDirection
        );

        when(listGenreUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage,expectedPerPage,expectedTotal,expectedItems));

        final var request = get("/genres")
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .queryParam("search", expectedTerms)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
                .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
                .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
                .andExpect(jsonPath("$.items[0].id", equalTo(aGenre.getId().getValue())))
                .andExpect(jsonPath("$.items[0].name", equalTo(aGenre.getName())))
                .andExpect(jsonPath("$.items[0].is_active", equalTo(aGenre.isActive())))
                .andExpect(jsonPath("$.items[0].created_at", equalTo(aGenre.getCreatedAt().toString())))
                .andExpect(jsonPath("$.items[0].deleted_at", equalTo(aGenre.getDeletedAt())));


        verify(listGenreUseCase, times(1)).execute(argThat(query ->
                Objects.equals(expectedPage, query.page())
                        && Objects.equals(expectedDirection, query.direction())
                        && Objects.equals(expectedSort, query.sort())
                        && Objects.equals(expectedTerms, query.terms())
        ));

    }

    List<String> asString(List<CategoryID> categoryIDList){
        return categoryIDList.stream()
                .map(CategoryID::getValue)
                .toList();
    }


}
