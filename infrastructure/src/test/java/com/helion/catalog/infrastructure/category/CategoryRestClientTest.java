package com.helion.catalog.infrastructure.category;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.helion.catalog.AbstractRestClientTest;
import com.helion.catalog.domain.Fixture;
import com.helion.catalog.domain.exceptions.InternalErrorException;
import com.helion.catalog.infrastructure.authentication.ClientCredentialsManager;
import com.helion.catalog.infrastructure.category.models.CategoryDTO;
import io.github.resilience4j.bulkhead.BulkheadFullException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import wiremock.org.apache.hc.core5.http.HttpHeaders;

import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;


public class CategoryRestClientTest extends AbstractRestClientTest {



    @Autowired
    private CategoryRestClient target;

    @SpyBean
    private ClientCredentialsManager clientCredentialsManager;

    @Test
    public void givenACategory_whenReceive200FromServer_shouldBeOk() {
        // given
        final var aulas = Fixture.Categories.aulas();



        final var responseBody = writeValueAsString(new CategoryDTO(
                aulas.id(),
                aulas.name(),
                aulas.description(),
                aulas.isActive(),
                aulas.createdAt(),
                aulas.updatedAt(),
                aulas.deletedAt()
        ));

        final var expectedToken = "access-123";
        Mockito.doReturn(expectedToken).when(clientCredentialsManager).retrieve();

        WireMock.stubFor(
                WireMock.get(WireMock.urlPathEqualTo("/api/categories/%s".formatted(aulas.id())))
                        .willReturn(aResponse()
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withHeader(HttpHeaders.AUTHORIZATION, "bearer %s".formatted(expectedToken))
                                .withStatus(200)
                                .withBody(responseBody))

        );

        final var actualCategory = target.categoryOfId(aulas.id()).get();

        Assertions.assertEquals(aulas.id(),actualCategory.id());
        Assertions.assertEquals(aulas.name(),actualCategory.name());
        Assertions.assertEquals(aulas.description(),actualCategory.description());
        Assertions.assertEquals(aulas.isActive(),actualCategory.isActive());
        Assertions.assertEquals(aulas.createdAt(),actualCategory.createdAt());
        Assertions.assertEquals(aulas.updatedAt(),actualCategory.updatedAt());
        Assertions.assertNull(actualCategory.deletedAt());

        WireMock.verify(1, getRequestedFor(urlPathEqualTo("/api/categories/%s".formatted(aulas.id()))));

    }

    @Test
    public void givenACategory_whenReceiveTwoCalls_shouldReturnCachedValue() {
        // given
        final var aulas = Fixture.Categories.aulas();



        final var responseBody = writeValueAsString(new CategoryDTO(
                aulas.id(),
                aulas.name(),
                aulas.description(),
                aulas.isActive(),
                aulas.createdAt(),
                aulas.updatedAt(),
                aulas.deletedAt()
        ));

        final var expectedToken = "access-123";
        Mockito.doReturn(expectedToken).when(clientCredentialsManager).retrieve();

        WireMock.stubFor(
                WireMock.get(WireMock.urlPathEqualTo("/api/categories/%s".formatted(aulas.id())))
                        .willReturn(aResponse()
                                .withHeader(HttpHeaders.AUTHORIZATION, "bearer %s".formatted(expectedToken))
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withStatus(200)
                                .withBody(responseBody))

        );

        target.categoryOfId(aulas.id()).get();
        target.categoryOfId(aulas.id()).get();
        final var actualCategory = target.categoryOfId(aulas.id()).get();

        Assertions.assertEquals(aulas.id(),actualCategory.id());
        Assertions.assertEquals(aulas.name(),actualCategory.name());
        Assertions.assertEquals(aulas.description(),actualCategory.description());
        Assertions.assertEquals(aulas.isActive(),actualCategory.isActive());
        Assertions.assertEquals(aulas.createdAt(),actualCategory.createdAt());
        Assertions.assertEquals(aulas.updatedAt(),actualCategory.createdAt());
        Assertions.assertEquals(aulas.createdAt(),actualCategory.createdAt());

        final var actualCachedValue = cache("admin-categories").get(aulas.id());

        Assertions.assertEquals(actualCategory, actualCachedValue.get());

        WireMock.verify(1, getRequestedFor(urlPathEqualTo("/api/categories/%s".formatted(aulas.id()))));

    }

    @Test
    public void givenACategory_whenReceive5xxFromServer_shouldBeReturnError() {
        // given
        final var expectedId = "789";

        final var expectedErrorMessage = "Error observed from categories [resourceId: %s] [status: 500]".formatted(expectedId);

        final var responseBody = writeValueAsString(Map.of("message", "Internal Server Error") );

        final var expectedToken = "access-123";
        Mockito.doReturn(expectedToken).when(clientCredentialsManager).retrieve();

        WireMock.stubFor(
                WireMock.get(WireMock.urlPathEqualTo("/api/categories/%s".formatted(expectedId)))
                        .willReturn(aResponse()
                                .withHeader(HttpHeaders.AUTHORIZATION, "bearer %s".formatted(expectedToken))
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withStatus(500)
                                .withBody(responseBody))

        );

        final var actualException = Assertions.assertThrows(InternalErrorException.class, () -> target.categoryOfId(expectedId));

        Assertions.assertEquals(expectedErrorMessage,actualException.getMessage());

        WireMock.verify(2, getRequestedFor(urlPathEqualTo("/api/categories/%s".formatted(expectedId))));

    }

    @Test
    public void givenACategory_whenReceive404FromServer_shouldBeReturnEmpty() {
        // given
        final var expectedId = "889";


        final var responseBody = writeValueAsString(Map.of("message", "Not Found") );

        final var expectedToken = "access-123";
        Mockito.doReturn(expectedToken).when(clientCredentialsManager).retrieve();

        WireMock.stubFor(
                WireMock.get(WireMock.urlPathEqualTo("/api/categories/%s".formatted(expectedId)))
                        .willReturn(aResponse()
                                .withHeader(HttpHeaders.AUTHORIZATION, "bearer %s".formatted(expectedToken))
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withStatus(404)
                                .withBody(responseBody))

        );

        final var actualCategory = target.categoryOfId(expectedId);

        Assertions.assertTrue(actualCategory.isEmpty());

        WireMock.verify(1, getRequestedFor(urlPathEqualTo("/api/categories/%s".formatted(expectedId))));
    }




    @Test
    public void givenACategory_whenReceiveTimeoutFromServer_shouldBeReturnError() {
        final var aulas = Fixture.Categories.aulas();
        // given
        final var expectedId = aulas.id();

        final var expectedErrorMessage = "Timeout observed from categories [resourceId: %s]".formatted(expectedId);

        final var responseBody = writeValueAsString(new CategoryDTO(
                        aulas.id(),
                        aulas.name(),
                        aulas.description(),
                        aulas.isActive(),
                        aulas.createdAt(),
                        aulas.updatedAt(),
                        aulas.deletedAt()
                )

        );

        final var expectedToken = "access-123";
        Mockito.doReturn(expectedToken).when(clientCredentialsManager).retrieve();

        WireMock.stubFor(
                WireMock.get(WireMock.urlPathEqualTo("/api/categories/%s".formatted(expectedId)))
                        .willReturn(aResponse()
                                .withHeader(HttpHeaders.AUTHORIZATION, "bearer %s".formatted(expectedToken))
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withStatus(200)
                                .withFixedDelay(2200)
                                .withBody(responseBody))

        );

        final var actualException = Assertions.assertThrows(InternalErrorException.class, () -> target.categoryOfId(expectedId));

        Assertions.assertEquals(expectedErrorMessage,actualException.getMessage());

        WireMock.verify(2, getRequestedFor(urlPathEqualTo("/api/categories/%s".formatted(aulas.id()))));
    }

    @Test
    public void givenACategory_whenBulkheadIsFull_shouldBeReturnError() {

        final var expectedErrorMessage ="Bulkhead 'categories' is full and does not permit further calls";

        acquireBulkheadPermission(CATEGORY);

        final var actualException = Assertions.assertThrows(BulkheadFullException.class, () -> target.categoryOfId("123"));

        Assertions.assertEquals(expectedErrorMessage,actualException.getMessage());

        releaseBulkheadPermission(CATEGORY);

    }
    @Test
    public void givenServerError_whenIsMoreThanThreshold_shouldOpenCircuitBreak(){
        // given
        final var expectedId = "012";

        final var expectedErrorMessage = "Error observed from categories [resourceId: %s] [status: 500]".formatted(expectedId);

        final var responseBody = writeValueAsString(Map.of("message", "Internal Server Error") );

        final var expectedToken = "access-123";
        Mockito.doReturn(expectedToken).when(clientCredentialsManager).retrieve();

        WireMock.stubFor(
                WireMock.get(WireMock.urlPathEqualTo("/api/categories/%s".formatted(expectedId)))
                        .willReturn(aResponse()
                                .withHeader(HttpHeaders.AUTHORIZATION, "bearer %s".formatted(expectedToken))
                                .withStatus(500)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(responseBody))

        );

        Assertions.assertThrows(InternalErrorException.class, () -> this.target.categoryOfId(expectedId));
        final var actualEx = Assertions.assertThrows(CallNotPermittedException.class, () -> this.target.categoryOfId(expectedId));


        checkCircuitBreakerState(CATEGORY, CircuitBreaker.State.OPEN);

        WireMock.verify(3, WireMock.getRequestedFor(WireMock.urlPathEqualTo("/api/categories/%s".formatted(expectedId))));
    }

    @Test
    public void givenCall_whenCBIsOpen_shouldReturnERROR(){
        // given
        transitionToOpenState(CATEGORY);
        final var expectedId = "123";

        final var expectedErrorMessage = "CircuitBreaker 'categories' is OPEN and does not permit further calls";

        final var actualEx = Assertions.assertThrows(CallNotPermittedException.class, () -> this.target.categoryOfId(expectedId));


        checkCircuitBreakerState(CATEGORY, CircuitBreaker.State.OPEN);

        Assertions.assertEquals(expectedErrorMessage, actualEx.getMessage());

        WireMock.verify(0, WireMock.getRequestedFor(WireMock.urlPathEqualTo("/api/categories/%s".formatted(expectedId))));
    }
}
