package com.helion.catalog.infrastructure.authentication;

import com.helion.catalog.AbstractRestClientTest;
import com.helion.catalog.infrastructure.configuration.json.Json;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.testcontainers.shaded.com.google.common.net.HttpHeaders;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.helion.catalog.infrastructure.authentication.AuthenticationGateway.ClientCredentialsInput;
import static com.helion.catalog.infrastructure.authentication.KeycloakAuthenticationGateway.*;

class KeycloakAuthenticationGatewayTest extends AbstractRestClientTest {

    @Autowired
    private KeycloakAuthenticationGateway gateway;

    @Test
    public void givenValidParams_whenCallsLogin_shouldReturnValidCredentials(){

        final var expectedClientId =  "client-123";
        final var expectedClientSecret = "secret-123";
        final var expectedToken = "access";
        final var expectedRefreshToken = "refresh";

        stubFor(post(urlPathEqualTo("/realms/test/protocol/openid-connect/token"))
                .withHeader(HttpHeaders.ACCEPT, equalTo(MediaType.APPLICATION_JSON_VALUE))
                .withHeader(HttpHeaders.CONTENT_TYPE, equalTo(MediaType.APPLICATION_FORM_URLENCODED_VALUE+";charset=UTF-8"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(Json.writeValueAsString(new KeycloakAuthenticationResult(expectedToken, expectedRefreshToken)))
                )

            );

        final var actualOutput =
                this.gateway.login(new ClientCredentialsInput(expectedClientId,expectedClientSecret));

        Assertions.assertEquals(expectedToken, actualOutput.accessToken());
        Assertions.assertEquals(expectedRefreshToken, actualOutput.refreshToken());



    }

    @Test
    public void givenValidParams_whenCallsRefresh_shouldReturnValidCredentials(){

        final var expectedClientId =  "client-123";
        final var expectedClientSecret = "secret-123";
        final var expectedToken = "access";
        final var expectedRefreshToken = "refresh";

        stubFor(post(urlPathEqualTo("/realms/test/protocol/openid-connect/token"))
                .withHeader(HttpHeaders.ACCEPT, equalTo(MediaType.APPLICATION_JSON_VALUE))
                .withHeader(HttpHeaders.CONTENT_TYPE, equalTo(MediaType.APPLICATION_FORM_URLENCODED_VALUE+";charset=UTF-8"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(Json.writeValueAsString(new KeycloakAuthenticationResult(expectedToken, expectedRefreshToken)))
                )

        );

        final var actualOutput =
                this.gateway.refresh(new RefreshTokenInput(expectedClientId,expectedClientSecret, expectedRefreshToken));

        Assertions.assertEquals(expectedToken, actualOutput.accessToken());
        Assertions.assertEquals(expectedRefreshToken, actualOutput.refreshToken());



    }
}