package com.helion.admin.catalog.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.helion.admin.catalog.ControllerTest;
import com.helion.admin.catalog.application.castmember.create.CreateCastMemberOutput;
import com.helion.admin.catalog.application.castmember.create.CreateCastMemberUseCase;
import com.helion.admin.catalog.application.castmember.delete.DeleteCastMemberUseCase;
import com.helion.admin.catalog.application.castmember.retrieve.get.CastMemberOutput;
import com.helion.admin.catalog.application.castmember.retrieve.get.GetCastMemberByIdUseCase;
import com.helion.admin.catalog.application.castmember.retrieve.list.CastMemberListOutput;
import com.helion.admin.catalog.application.castmember.retrieve.list.ListCastMemberUseCase;
import com.helion.admin.catalog.application.castmember.update.UpdateCastMemberOutput;
import com.helion.admin.catalog.application.castmember.update.UpdateCastMemberUseCase;
import com.helion.admin.catalog.domain.Fixture;
import com.helion.admin.catalog.domain.castmember.CastMember;
import com.helion.admin.catalog.domain.castmember.CastMemberID;
import com.helion.admin.catalog.domain.castmember.CastMemberType;
import com.helion.admin.catalog.domain.category.pagination.Pagination;
import com.helion.admin.catalog.domain.category.pagination.SearchQuery;
import com.helion.admin.catalog.domain.exceptions.NotFoundException;
import com.helion.admin.catalog.domain.exceptions.NotificationException;
import com.helion.admin.catalog.domain.validation.Error;
import com.helion.admin.catalog.domain.validation.handler.Notification;
import com.helion.admin.catalog.infrastructure.api.castmember.CastMemberAPI;
import com.helion.admin.catalog.infrastructure.castmember.models.CreateCastMemberRequest;
import com.helion.admin.catalog.infrastructure.castmember.models.UpdateCastMemberRequest;
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

@ControllerTest(controllers = CastMemberAPI.class)
public class CastMemberAPITest {


    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateCastMemberUseCase createCastMemberUseCase;

    @MockBean
    private GetCastMemberByIdUseCase getCastMemberByIdUseCase;


    @MockBean
    private UpdateCastMemberUseCase updateCastMemberUseCase;

    @MockBean
    private DeleteCastMemberUseCase deleteCastMemberUseCase;

    @MockBean
    private ListCastMemberUseCase listCastMemberUseCase;

    @Test
    public void givenValidCommand_whenCallsCreateCastMember_shouldReturnCastMemberID() throws Exception {

        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();
        final var expectedId = "123";

        final var aInput = new CreateCastMemberRequest(expectedName, expectedType);

        Mockito.when(createCastMemberUseCase.execute(any()))
                .thenReturn(CreateCastMemberOutput.from(expectedId));

        final var request = post("/cast_members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        final var response = this.mvc.perform(request).andDo(print());

        response.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/cast_members/" +expectedId))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", Matchers.equalTo(expectedId)));

        Mockito.verify(createCastMemberUseCase).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        &&  Objects.equals(expectedType, cmd.type())
        ));
    }

    @Test
    public void givenAnInvalidName_whenCallsCreateCastMember_thenShouldReturnNotification() throws Exception {
        final String expectedName = null;
        final var expectedType = Fixture.CastMembers.type();
        final var expectedErrorMessage = "'name' should not be null";

        final var aInput = new CreateCastMemberRequest(expectedName, expectedType );


        when(createCastMemberUseCase.execute(any()))
                .thenThrow(new NotificationException("Error", Notification.create(new Error(expectedErrorMessage))));

        final var request = post("/cast_members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aInput));

        this.mvc.perform(request).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", Matchers.nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(createCastMemberUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedType, cmd.type())
        ));
    }

    @Test
    public void givenAnValidId_whenCallsGetCastMemberById_shouldReturnsACastMember() throws Exception {

        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();
        final var aMember = CastMember.newMember(expectedName, expectedType);
        final var expectedId = aMember.getId().getValue();

        when(getCastMemberByIdUseCase.execute(any()))
                .thenReturn(CastMemberOutput.from(aMember));

        final var request = get("/cast_members/{id}",expectedId)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)))
                .andExpect(jsonPath("$.name", equalTo(expectedName)))
                .andExpect(jsonPath("$.type", equalTo(expectedType.toString())))
                .andExpect(jsonPath("$.created_at", equalTo(aMember.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updated_at", equalTo(aMember.getUpdatedAt().toString())));

        verify(getCastMemberByIdUseCase,times(1)).execute(eq(expectedId));
    }

    @Test
    public void givenAnInValidId_whenCallsGetCastMemberByIdAndCastMemberDoesNotExists_shouldReturnsNotFound() throws Exception {

        final var expectedId = CastMemberID.from("123");
        final var expectedErrorMessage = "CastMember with ID 123 was not found";

        when(getCastMemberByIdUseCase.execute(any()))
                .thenThrow(NotFoundException.with(CastMember.class, expectedId));

        final var request = get("/cast_members/{id}",expectedId.getValue())
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

        response.andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

        verify(getCastMemberByIdUseCase,times(1)).execute(eq(expectedId.getValue()));
    }

    @Test
    public void givenValidCommand_whenCallsUpdateCastMember_shouldReturnCastMemberID() throws Exception {

        final var expectedName = Fixture.name();
        final var expectedType = CastMemberType.ACTOR;
        final var aMember = CastMember.newMember("vin", CastMemberType.DIRECTOR);
        final var expectedId = aMember.getId();

        final var aCommand = new UpdateCastMemberRequest(expectedName, expectedType);

        Mockito.when(updateCastMemberUseCase.execute(any()))
                .thenReturn(UpdateCastMemberOutput.from(expectedId.getValue()));

        final var request = put("/cast_members/{id}",expectedId.getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(request).andDo(print());

        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", Matchers.equalTo(expectedId.getValue())));

        Mockito.verify(updateCastMemberUseCase).execute(argThat(cmd ->
                Objects.equals(expectedId.getValue(), cmd.id())
                &&Objects.equals(expectedName, cmd.name())
                        &&  Objects.equals(expectedType, cmd.type())
        ));
    }

    @Test
    public void givenAnInvalidName_whenCallsUpdateCastMember_thenShouldReturnNotification() throws Exception {
        final String expectedName = null;
        final var expectedType = Fixture.CastMembers.type();
        final var expectedErrorMessage = "'name' should not be null";
        final var aMember = CastMember.newMember("vin", CastMemberType.DIRECTOR);
        final var expectedId =aMember.getId();
        final var aInput = new UpdateCastMemberRequest(expectedName, expectedType );


        when(updateCastMemberUseCase.execute(any()))
                .thenThrow(new NotificationException("Error", Notification.create(new Error(expectedErrorMessage))));

        final var request = put("/cast_members/{id}", expectedId.getValue())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aInput));

        this.mvc.perform(request).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(updateCastMemberUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedId.getValue(), cmd.id())
                && Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedType, cmd.type())
        ));
    }

    @Test
    public void givenAnInvalidName_whenCallsUpdateCastMember_thenShouldReturnNotFound() throws Exception {

        final var expectedErrorMessage = "CastMember with ID 123 was not found";
        final var expectedId =CastMemberID.from("123");
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();
        final var aCommand = new UpdateCastMemberRequest(expectedName, expectedType );


        when(updateCastMemberUseCase.execute(any()))
                .thenThrow(NotFoundException.with(CastMember.class, expectedId));

        final var request = put("/cast_members/{id}", expectedId.getValue())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        this.mvc.perform(request).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

        verify(updateCastMemberUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedId.getValue(), cmd.id())
                        && Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedType, cmd.type())
        ));
    }

    @Test
    public void givenAnValidId_whenCallsDeleteCastMemberById_shouldReturnNoContent() throws Exception {

        final var expectedId = "123";


        doNothing().when(deleteCastMemberUseCase).execute(any());

        final var request = delete("/cast_members/{id}",expectedId)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

        response.andExpect(status().isNoContent());



        verify(deleteCastMemberUseCase,times(1)).execute(eq(expectedId));
    }

    @Test
    public void  givenAValidParams_whenCallListCastMembers_shouldReturnCastMembers() throws Exception {

        final var expectedPage = 1;
        final var expectedPerPage = 20;
        final var expectedTerms = "Alg";
        final var expectedSort = "type";
        final var expectedDirection = "desc";
        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var aMember = CastMember.newMember(Fixture.name(), Fixture.CastMembers.type());

        final var expectedItems = List.of(CastMemberListOutput.from(aMember));

        final var aQuery = new SearchQuery(
                expectedPage, expectedPerPage,expectedTerms,expectedSort,expectedDirection
        );

        when(listCastMemberUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage,expectedPerPage,expectedTotal,expectedItems));

        final var request = get("/cast_members")
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .queryParam("search", expectedTerms)
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
                .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
                .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
                .andExpect(jsonPath("$.items[0].id", equalTo(aMember.getId().getValue())))
                .andExpect(jsonPath("$.items[0].name", equalTo(aMember.getName())))
                .andExpect(jsonPath("$.items[0].type", equalTo(aMember.getType().toString())))
                .andExpect(jsonPath("$.items[0].created_at", equalTo(aMember.getCreatedAt().toString())));


        verify(listCastMemberUseCase, times(1)).execute(argThat(query ->
                Objects.equals(expectedPage, query.page())
                        && Objects.equals(expectedPerPage, query.perPage())
                        && Objects.equals(expectedDirection, query.direction())
                        && Objects.equals(expectedSort, query.sort())
                        && Objects.equals(expectedTerms, query.terms())
        ));


    }

    @Test
    public void  givenEmptyParams_whenCallListCastMembers_shouldReturnCastMembers() throws Exception {

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var aMember = CastMember.newMember(Fixture.name(), Fixture.CastMembers.type());

        final var expectedItems = List.of(CastMemberListOutput.from(aMember));

        final var aQuery = new SearchQuery(
                expectedPage, expectedPerPage,expectedTerms,expectedSort,expectedDirection
        );

        when(listCastMemberUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage,expectedPerPage,expectedTotal,expectedItems));

        final var request = get("/cast_members")
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
                .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
                .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
                .andExpect(jsonPath("$.items[0].id", equalTo(aMember.getId().getValue())))
                .andExpect(jsonPath("$.items[0].name", equalTo(aMember.getName())))
                .andExpect(jsonPath("$.items[0].type", equalTo(aMember.getType().toString())))
                .andExpect(jsonPath("$.items[0].created_at", equalTo(aMember.getCreatedAt().toString())));


        verify(listCastMemberUseCase, times(1)).execute(argThat(query ->
                Objects.equals(expectedPage, query.page())
                        && Objects.equals(expectedPerPage, query.perPage())
                        && Objects.equals(expectedDirection, query.direction())
                        && Objects.equals(expectedSort, query.sort())
                        && Objects.equals(expectedTerms, query.terms())
        ));


    }


}
