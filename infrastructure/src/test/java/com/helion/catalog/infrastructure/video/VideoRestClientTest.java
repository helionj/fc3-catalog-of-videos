package com.helion.catalog.infrastructure.video;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.helion.catalog.AbstractRestClientTest;
import com.helion.catalog.domain.Fixture;
import com.helion.catalog.domain.exceptions.InternalErrorException;
import com.helion.catalog.domain.utils.IdUtils;
import com.helion.catalog.infrastructure.authentication.ClientCredentialsManager;
import com.helion.catalog.infrastructure.video.models.ImageResourceDTO;
import com.helion.catalog.infrastructure.video.models.VideoDTO;
import com.helion.catalog.infrastructure.video.models.VideoResourceDTO;
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

public class VideoRestClientTest extends AbstractRestClientTest {

    @Autowired
    private VideoRestClient target;

    @SpyBean
    private ClientCredentialsManager clientCredentialsManager;

    @Test
    public void givenAVideo_whenReceive200FromServer_shouldBeOk() {
        // given
        final var golang = Fixture.Videos.golang();



        final var responseBody = writeValueAsString(new VideoDTO(
                golang.id(),
                golang.title(),
                golang.description(),
                golang.published(),
                golang.launchedAt().getValue(),
                golang.rating().getName(),
                golang.duration(),
                golang.opened(),
                videoResource(golang.trailer()),
                imageResource(golang.banner()),
                imageResource(golang.thumb()),
                imageResource(golang.thumbHalf()),
                videoResource(golang.video()),
                golang.castMembers(),
                golang.categories(),
                golang.genres(),
                golang.createdAt().toString(),
                golang.updatedAt().toString()
        ));

        final var expectedToken = "access-123";
        Mockito.doReturn(expectedToken).when(clientCredentialsManager).retrieve();

        WireMock.stubFor(
                WireMock.get(WireMock.urlPathEqualTo("/api/videos/%s".formatted(golang.id())))
                        .withHeader(HttpHeaders.AUTHORIZATION, equalTo("bearer %s".formatted(expectedToken)))
                        .willReturn(aResponse()
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withStatus(200)
                                .withBody(responseBody))

        );

        final var actualVideo = target.videoOfId(golang.id()).get();

        Assertions.assertEquals(golang.id(),actualVideo.id());
        Assertions.assertEquals(golang.description(),actualVideo.description());
        Assertions.assertEquals(golang.published(),actualVideo.published());
        Assertions.assertEquals(golang.launchedAt().getValue(),actualVideo.yearLaunched());
        Assertions.assertEquals(golang.rating().getName(),actualVideo.rating());
        Assertions.assertEquals(golang.duration(),actualVideo.duration());
        Assertions.assertEquals(golang.opened(),actualVideo.opened());
        Assertions.assertEquals(golang.trailer(),actualVideo.trailer().encodedLocation());
        Assertions.assertEquals(golang.banner(),actualVideo.banner().location());
        Assertions.assertEquals(golang.thumb(),actualVideo.thumbnail().location());
        Assertions.assertEquals(golang.thumbHalf(),actualVideo.thumbnailHalf().location());
        Assertions.assertEquals(golang.video(),actualVideo.video().encodedLocation());
        Assertions.assertEquals(golang.castMembers(),actualVideo.castMembersId());
        Assertions.assertEquals(golang.categories(),actualVideo.categoriesId());
        Assertions.assertEquals(golang.genres(),actualVideo.genresId());
        Assertions.assertEquals(golang.createdAt().toString(),actualVideo.createdAt());
        Assertions.assertEquals(golang.updatedAt().toString(),actualVideo.updatedAt());

        WireMock.verify(1, getRequestedFor(urlPathEqualTo("/api/videos/%s".formatted(golang.id()))));

    }

    @Test
    public void givenAVideo_whenReceiveTwoCalls_shouldReturnCachedValue() {
        // given
        final var golang = Fixture.Videos.golang();



        final var responseBody = writeValueAsString(new VideoDTO(
                golang.id(),
                golang.title(),
                golang.description(),
                golang.published(),
                golang.launchedAt().getValue(),
                golang.rating().getName(),
                golang.duration(),
                golang.opened(),
                videoResource(golang.trailer()),
                imageResource(golang.banner()),
                imageResource(golang.thumb()),
                imageResource(golang.thumbHalf()),
                videoResource(golang.video()),
                golang.castMembers(),
                golang.categories(),
                golang.genres(),
                golang.createdAt().toString(),
                golang.updatedAt().toString()
        ));

        final var expectedToken = "access-123";
        Mockito.doReturn(expectedToken).when(clientCredentialsManager).retrieve();

        WireMock.stubFor(
                WireMock.get(WireMock.urlPathEqualTo("/api/videos/%s".formatted(golang.id())))
                        .withHeader(HttpHeaders.AUTHORIZATION, equalTo("bearer %s".formatted(expectedToken)))
                        .willReturn(aResponse()
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withStatus(200)
                                .withBody(responseBody))

        );

        target.videoOfId(golang.id()).get();
        target.videoOfId(golang.id()).get();
        final var actualVideo = target.videoOfId(golang.id()).get();

        Assertions.assertEquals(golang.id(),actualVideo.id());
        Assertions.assertEquals(golang.description(),actualVideo.description());
        Assertions.assertEquals(golang.published(),actualVideo.published());
        Assertions.assertEquals(golang.launchedAt().getValue(),actualVideo.yearLaunched());
        Assertions.assertEquals(golang.rating().getName(),actualVideo.rating());
        Assertions.assertEquals(golang.duration(),actualVideo.duration());
        Assertions.assertEquals(golang.opened(),actualVideo.opened());
        Assertions.assertEquals(golang.trailer(),actualVideo.trailer().encodedLocation());
        Assertions.assertEquals(golang.banner(),actualVideo.banner().location());
        Assertions.assertEquals(golang.thumb(),actualVideo.thumbnail().location());
        Assertions.assertEquals(golang.thumbHalf(),actualVideo.thumbnailHalf().location());
        Assertions.assertEquals(golang.video(),actualVideo.video().encodedLocation());
        Assertions.assertEquals(golang.castMembers(),actualVideo.castMembersId());
        Assertions.assertEquals(golang.categories(),actualVideo.categoriesId());
        Assertions.assertEquals(golang.genres(),actualVideo.genresId());
        Assertions.assertEquals(golang.createdAt().toString(),actualVideo.createdAt());
        Assertions.assertEquals(golang.updatedAt().toString(),actualVideo.updatedAt());

        final var actualCachedValue = cache("admin-videos").get(golang.id());

        Assertions.assertEquals(actualVideo, actualCachedValue.get());

        WireMock.verify(1, getRequestedFor(urlPathEqualTo("/api/videos/%s".formatted(golang.id()))));

    }
    @Test
    public void givenAVideo_whenReceive5xxFromServer_shouldBeReturnError() {
        // given
        final var expectedId = "789";

        final var expectedErrorMessage = "Error observed from videos [resourceId: %s] [status: 500]".formatted(expectedId);

        final var responseBody = writeValueAsString(Map.of("message", "Internal Server Error") );

        final var expectedToken = "access-123";
        Mockito.doReturn(expectedToken).when(clientCredentialsManager).retrieve();

        WireMock.stubFor(
                WireMock.get(WireMock.urlPathEqualTo("/api/videos/%s".formatted(expectedId)))
                        .withHeader(HttpHeaders.AUTHORIZATION, equalTo("bearer %s".formatted(expectedToken)))
                        .willReturn(aResponse()
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withStatus(500)
                                .withBody(responseBody))

        );

        final var actualException = Assertions.assertThrows(InternalErrorException.class, () -> target.videoOfId(expectedId));

        Assertions.assertEquals(expectedErrorMessage,actualException.getMessage());

        WireMock.verify(2, getRequestedFor(urlPathEqualTo("/api/videos/%s".formatted(expectedId))));

    }

    @Test
    public void givenAVideo_whenReceive404FromServer_shouldBeReturnEmpty() {
        // given
        final var expectedId = "9999";


        final var responseBody = writeValueAsString(Map.of("message", "Not Found") );

        final var expectedToken = "access-123";
        Mockito.doReturn(expectedToken).when(clientCredentialsManager).retrieve();

        WireMock.stubFor(
                WireMock.get(WireMock.urlPathEqualTo("/api/videos/%s".formatted(expectedId)))
                        .withHeader(HttpHeaders.AUTHORIZATION, equalTo("bearer %s".formatted(expectedToken)))
                        .willReturn(aResponse()
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withStatus(404)
                                .withBody(responseBody))

        );

        final var actualVideo = target.videoOfId(expectedId);

        Assertions.assertTrue(actualVideo.isEmpty());

        WireMock.verify(1, getRequestedFor(urlPathEqualTo("/api/videos/%s".formatted(expectedId))));
    }




    @Test
    public void givenAVideo_whenReceiveTimeoutFromServer_shouldBeReturnError() {
        final var golang = Fixture.Videos.golang();
        // given
        final var expectedId = golang.id();

        final var expectedErrorMessage = "Timeout observed from videos [resourceId: %s]".formatted(expectedId);

        final var responseBody = writeValueAsString(new VideoDTO(
                        golang.id(),
                        golang.title(),
                        golang.description(),
                        golang.published(),
                        golang.launchedAt().getValue(),
                        golang.rating().getName(),
                        golang.duration(),
                        golang.opened(),
                        videoResource(golang.trailer()),
                        imageResource(golang.banner()),
                        imageResource(golang.thumb()),
                        imageResource(golang.thumbHalf()),
                        videoResource(golang.video()),
                        golang.castMembers(),
                        golang.categories(),
                        golang.genres(),
                        golang.createdAt().toString(),
                        golang.updatedAt().toString()
                )

        );

        final var expectedToken = "access-123";
        Mockito.doReturn(expectedToken).when(clientCredentialsManager).retrieve();

        WireMock.stubFor(
                WireMock.get(WireMock.urlPathEqualTo("/api/videos/%s".formatted(expectedId)))
                        .withHeader(HttpHeaders.AUTHORIZATION, equalTo("bearer %s".formatted(expectedToken)))
                        .willReturn(aResponse()
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withStatus(200)
                                .withFixedDelay(2200)
                                .withBody(responseBody))

        );

        final var actualException = Assertions.assertThrows(InternalErrorException.class, () -> target.videoOfId(expectedId));

        Assertions.assertEquals(expectedErrorMessage,actualException.getMessage());

        WireMock.verify(2, getRequestedFor(urlPathEqualTo("/api/videos/%s".formatted(golang.id()))));
    }

    @Test
    public void givenAVideo_whenBulkheadIsFull_shouldBeReturnError() {

        final var expectedErrorMessage ="Bulkhead 'videos' is full and does not permit further calls";

        acquireBulkheadPermission(VIDEO);

        final var actualException = Assertions.assertThrows(BulkheadFullException.class, () -> target.videoOfId("777"));

        Assertions.assertEquals(expectedErrorMessage,actualException.getMessage());

        releaseBulkheadPermission(VIDEO);

    }
    @Test
    public void givenServerError_whenIsMoreThanThreshold_shouldOpenCircuitBreak(){
        // given
        final var expectedId = "012";

        final var expectedErrorMessage = "Error observed from videos [resourceId: %s] [status: 500]".formatted(expectedId);

        final var responseBody = writeValueAsString(Map.of("message", "Internal Server Error") );

        final var expectedToken = "access-123";
        Mockito.doReturn(expectedToken).when(clientCredentialsManager).retrieve();

        WireMock.stubFor(
                WireMock.get(WireMock.urlPathEqualTo("/api/videos/%s".formatted(expectedId)))
                        .withHeader(HttpHeaders.AUTHORIZATION, equalTo("bearer %s".formatted(expectedToken)))
                        .willReturn(aResponse()
                                .withStatus(500)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(responseBody))

        );

        Assertions.assertThrows(InternalErrorException.class, () -> this.target.videoOfId(expectedId));
        final var actualEx = Assertions.assertThrows(CallNotPermittedException.class, () -> this.target.videoOfId(expectedId));


        checkCircuitBreakerState(VIDEO, CircuitBreaker.State.OPEN);

        WireMock.verify(3, WireMock.getRequestedFor(WireMock.urlPathEqualTo("/api/videos/%s".formatted(expectedId))));
    }

    @Test
    public void givenCall_whenCBIsOpen_shouldReturnERROR(){
        // given
        transitionToOpenState(VIDEO);
        final var expectedId = "123";

        final var expectedErrorMessage = "CircuitBreaker 'videos' is OPEN and does not permit further calls";

        final var actualEx = Assertions.assertThrows(CallNotPermittedException.class, () -> this.target.videoOfId(expectedId));


        checkCircuitBreakerState(VIDEO, CircuitBreaker.State.OPEN);

        Assertions.assertEquals(expectedErrorMessage, actualEx.getMessage());

        WireMock.verify(0, WireMock.getRequestedFor(WireMock.urlPathEqualTo("/api/videos/%s".formatted(expectedId))));
    }

    private static VideoResourceDTO videoResource(String data) {
        return new VideoResourceDTO(IdUtils.uniqueId(), IdUtils.uniqueId(), data, data, data, "processed");
    }

    private static ImageResourceDTO imageResource(String data) {
        return new ImageResourceDTO(IdUtils.uniqueId(), IdUtils.uniqueId(), data, data);
    }
}
