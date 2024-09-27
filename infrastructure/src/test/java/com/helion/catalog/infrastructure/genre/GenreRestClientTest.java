package com.helion.catalog.infrastructure.genre;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.helion.catalog.AbstractRestClientTest;
import com.helion.catalog.domain.Fixture;
import com.helion.catalog.domain.exceptions.InternalErrorException;
import com.helion.catalog.infrastructure.authentication.ClientCredentialsManager;
import com.helion.catalog.infrastructure.genre.models.GenreDTO;
import io.github.resilience4j.bulkhead.BulkheadFullException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.testcontainers.shaded.com.google.common.net.HttpHeaders;

import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class GenreRestClientTest extends AbstractRestClientTest {

    @Autowired
    private GenreRestClient target;

    @SpyBean
    private ClientCredentialsManager clientCredentialsManager;


    @Test
    public void givenAGenre_whenReceive200FromServer_shouldBeOk() {
        // given
        final var business = Fixture.Genres.business();



        final var responseBody = writeValueAsString(new GenreDTO(
                business.id(),
                business.name(),
                business.categories(),
                business.isActive(),
                business.createdAt(),
                business.updatedAt(),
                business.deletedAt()
        ));

        final var expectedToken = "access-123";
        Mockito.doReturn(expectedToken).when(clientCredentialsManager).retrieve();

        WireMock.stubFor(
                WireMock.get(WireMock.urlPathEqualTo("/api/genres/%s".formatted(business.id())))
                        .withHeader(HttpHeaders.AUTHORIZATION, equalTo("bearer %s".formatted(expectedToken)))
                        .willReturn(aResponse()
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withStatus(200)
                                .withBody(responseBody))

        );

        final var actualGenre = target.genreOfId(business.id()).get();

        Assertions.assertEquals(business.id(),actualGenre.id());
        Assertions.assertEquals(business.name(),actualGenre.name());
        Assertions.assertEquals(business.categories(),actualGenre.categoriesId());
        Assertions.assertEquals(business.isActive(),actualGenre.isActive());
        Assertions.assertEquals(business.createdAt(),actualGenre.createdAt());
        Assertions.assertEquals(business.updatedAt(),actualGenre.updatedAt());
        Assertions.assertEquals(business.deletedAt(), actualGenre.deletedAt());

        WireMock.verify(1, getRequestedFor(urlPathEqualTo("/api/genres/%s".formatted(business.id()))));

    }
    /*
    @Test
    public void givenAGenre_whenReceiveTwoCalls_shouldReturnCachedValue() {
        // given
        final var business = Fixture.Genres.business();



        final var responseBody = writeValueAsString(new GenreDTO(
                business.id(),
                business.name(),
                business.categories(),
                business.isActive(),
                business.createdAt(),
                business.updatedAt(),
                business.deletedAt()
        ));

        final var expectedToken = "access-123";
        Mockito.doReturn(expectedToken).when(clientCredentialsManager).retrieve();

        WireMock.stubFor(
                WireMock.get(WireMock.urlPathEqualTo("/api/genres/%s".formatted(business.id())))
                        .withHeader(HttpHeaders.AUTHORIZATION, equalTo("bearer %s".formatted(expectedToken)))
                        .willReturn(aResponse()
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withStatus(200)
                                .withBody(responseBody))

        );

        target.genreOfId(business.id()).get();
        target.genreOfId(business.id()).get();
        final var actualGenre = target.genreOfId(business.id()).get();

        Assertions.assertEquals(business.id(),actualGenre.id());
        Assertions.assertEquals(business.name(),actualGenre.name());
        Assertions.assertEquals(business.categories(),actualGenre.categoriesId());
        Assertions.assertEquals(business.isActive(),actualGenre.isActive());
        Assertions.assertEquals(business.createdAt(),actualGenre.createdAt());
        Assertions.assertEquals(business.updatedAt(),actualGenre.updatedAt());
        Assertions.assertEquals(business.deletedAt(), actualGenre.deletedAt());

        final var actualCachedValue = cache("admin-genres").get(business.id());

        Assertions.assertEquals(actualGenre, actualCachedValue.get());

        WireMock.verify(1, getRequestedFor(urlPathEqualTo("/api/genres/%s".formatted(business.id()))));

    }*/
    @Test
    public void givenAGenre_whenReceive5xxFromServer_shouldBeReturnError() {
        // given
        final var expectedId = "789";

        final var expectedErrorMessage = "Error observed from genres [resourceId: %s] [status: 500]".formatted(expectedId);

        final var responseBody = writeValueAsString(Map.of("message", "Internal Server Error") );

        final var expectedToken = "access-123";
        Mockito.doReturn(expectedToken).when(clientCredentialsManager).retrieve();

        WireMock.stubFor(
                WireMock.get(WireMock.urlPathEqualTo("/api/genres/%s".formatted(expectedId)))
                        .withHeader(HttpHeaders.AUTHORIZATION, equalTo("bearer %s".formatted(expectedToken)))
                        .willReturn(aResponse()
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withStatus(500)
                                .withBody(responseBody))

        );

        final var actualException = Assertions.assertThrows(InternalErrorException.class, () -> target.genreOfId(expectedId));

        Assertions.assertEquals(expectedErrorMessage,actualException.getMessage());

        WireMock.verify(2, getRequestedFor(urlPathEqualTo("/api/genres/%s".formatted(expectedId))));

    }

    @Test
    public void givenAGenre_whenReceive404FromServer_shouldBeReturnEmpty() {
        // given
        final var expectedId = "9999";


        final var responseBody = writeValueAsString(Map.of("message", "Not Found") );

        final var expectedToken = "access-123";
        Mockito.doReturn(expectedToken).when(clientCredentialsManager).retrieve();

        WireMock.stubFor(
                WireMock.get(WireMock.urlPathEqualTo("/api/genres/%s".formatted(expectedId)))
                        .withHeader(HttpHeaders.AUTHORIZATION, equalTo("bearer %s".formatted(expectedToken)))
                        .willReturn(aResponse()
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withStatus(404)
                                .withBody(responseBody))

        );

        final var actualGenre = target.genreOfId(expectedId);

        Assertions.assertTrue(actualGenre.isEmpty());

        WireMock.verify(1, getRequestedFor(urlPathEqualTo("/api/genres/%s".formatted(expectedId))));
    }




    @Test
    public void givenAGenre_whenReceiveTimeoutFromServer_shouldBeReturnError() {
        final var business = Fixture.Genres.business();
        // given
        final var expectedId = business.id();

        final var expectedErrorMessage = "Timeout observed from genres [resourceId: %s]".formatted(expectedId);

        final var responseBody = writeValueAsString(new GenreDTO(
                        business.id(),
                        business.name(),
                        business.categories(),
                        business.isActive(),
                        business.createdAt(),
                        business.updatedAt(),
                        business.deletedAt()
                )

        );

        final var expectedToken = "access-123";
        Mockito.doReturn(expectedToken).when(clientCredentialsManager).retrieve();

        WireMock.stubFor(
                WireMock.get(WireMock.urlPathEqualTo("/api/genres/%s".formatted(expectedId)))
                        .withHeader(HttpHeaders.AUTHORIZATION, equalTo("bearer %s".formatted(expectedToken)))
                        .willReturn(aResponse()
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withStatus(200)
                                .withFixedDelay(2200)
                                .withBody(responseBody))

        );

        final var actualException = Assertions.assertThrows(InternalErrorException.class, () -> target.genreOfId(expectedId));

        Assertions.assertEquals(expectedErrorMessage,actualException.getMessage());

        WireMock.verify(2, getRequestedFor(urlPathEqualTo("/api/genres/%s".formatted(business.id()))));
    }

    @Test
    public void givenAGenre_whenBulkheadIsFull_shouldBeReturnError() {

        final var expectedErrorMessage ="Bulkhead 'genres' is full and does not permit further calls";

        acquireBulkheadPermission(GENRE);

        final var actualException = Assertions.assertThrows(BulkheadFullException.class, () -> target.genreOfId("777"));

        Assertions.assertEquals(expectedErrorMessage,actualException.getMessage());

        releaseBulkheadPermission(GENRE);

    }
    @Test
    public void givenServerError_whenIsMoreThanThreshold_shouldOpenCircuitBreak(){
        // given
        final var expectedId = "012";

        final var expectedErrorMessage = "Error observed from genres [resourceId: %s] [status: 500]".formatted(expectedId);

        final var responseBody = writeValueAsString(Map.of("message", "Internal Server Error") );

        final var expectedToken = "access-123";
        Mockito.doReturn(expectedToken).when(clientCredentialsManager).retrieve();

        WireMock.stubFor(
                WireMock.get(WireMock.urlPathEqualTo("/api/genres/%s".formatted(expectedId)))
                        .withHeader(HttpHeaders.AUTHORIZATION, equalTo("bearer %s".formatted(expectedToken)))
                        .willReturn(aResponse()
                                .withStatus(500)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(responseBody))

        );

        Assertions.assertThrows(InternalErrorException.class, () -> this.target.genreOfId(expectedId));
        final var actualEx = Assertions.assertThrows(CallNotPermittedException.class, () -> this.target.genreOfId(expectedId));


        checkCircuitBreakerState(GENRE, CircuitBreaker.State.OPEN);

        WireMock.verify(3, getRequestedFor(WireMock.urlPathEqualTo("/api/genres/%s".formatted(expectedId))));
    }

    @Test
    public void givenCall_whenCBIsOpen_shouldReturnERROR(){
        // given
        transitionToOpenState(GENRE);
        final var expectedId = "123";

        final var expectedErrorMessage = "CircuitBreaker 'genres' is OPEN and does not permit further calls";

        final var actualEx = Assertions.assertThrows(CallNotPermittedException.class, () -> this.target.genreOfId(expectedId));


        checkCircuitBreakerState(GENRE, CircuitBreaker.State.OPEN);

        Assertions.assertEquals(expectedErrorMessage, actualEx.getMessage());

        WireMock.verify(0, getRequestedFor(WireMock.urlPathEqualTo("/api/genres/%s".formatted(expectedId))));
    }


}
