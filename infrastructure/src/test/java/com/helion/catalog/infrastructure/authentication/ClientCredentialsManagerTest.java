package com.helion.catalog.infrastructure.authentication;

import com.helion.catalog.domain.exceptions.InternalErrorException;
import com.helion.catalog.infrastructure.configuration.properties.KeycloakProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static com.helion.catalog.infrastructure.authentication.AuthenticationGateway.*;
import static com.helion.catalog.infrastructure.authentication.AuthenticationGateway.AuthenticationResult;
import static com.helion.catalog.infrastructure.authentication.AuthenticationGateway.ClientCredentialsInput;
import static com.helion.catalog.infrastructure.authentication.ClientCredentialsManager.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class ClientCredentialsManagerTest {

    @Mock
    private KeycloakProperties keycloakProperties;

    @Mock
    private AuthenticationGateway authenticationGateway;

    @InjectMocks
    private ClientCredentialsManager manager;

    @Test
    public void givenValidAuthenticationResult_whenCallsRefresh_shouldCreateCredentials(){

        final var expectedAccessToken = "access";
        final var expectedRefreshToken = "refresh";
        final var expectedClientId = "client-123";
        final var expectedClientSecret ="secret-123";

        doReturn(expectedClientId).when(keycloakProperties).clientId();
        doReturn(expectedClientSecret).when(keycloakProperties).clientSecret();

        doReturn(new AuthenticationResult(expectedAccessToken, expectedRefreshToken))
                .when(authenticationGateway).login(new ClientCredentialsInput(expectedClientId, expectedClientSecret));

        this.manager.refresh();

        final var actualToken = this.manager.retrieve();

        Assertions.assertEquals(expectedAccessToken,actualToken);
    }

    @Test
    public void givenPreviusAuthentication_whenCallsRefresh_shouldUpdateCredentials(){

        final var expectedAccessToken = "access";
        final var expectedRefreshToken = "refresh";
        final var expectedClientId = "client-123";
        final var expectedClientSecret ="secret-123";

        ReflectionTestUtils.setField(this.manager, "credentials", new ClientCredentials(expectedClientId, "acc", "ref"));

        doReturn(expectedClientId).when(keycloakProperties).clientId();
        doReturn(expectedClientSecret).when(keycloakProperties).clientSecret();

        doReturn(new AuthenticationResult(expectedAccessToken, expectedRefreshToken))
                .when(authenticationGateway).refresh(new RefreshTokenInput(expectedClientId, expectedClientSecret, "ref"));

        this.manager.refresh();

        final var actualCredentials = (ClientCredentials)ReflectionTestUtils.getField(this.manager, "credentials");
        Assertions.assertEquals(expectedAccessToken,actualCredentials.accessToken());
        Assertions.assertEquals(expectedRefreshToken,actualCredentials.refreshToken());
    }

    @Test
    public void givenErrorFromRefreshToken_whenCallsRefresh_shouldFallbackToLogin(){

        final var expectedAccessToken = "access";
        final var expectedRefreshToken = "refresh";
        final var expectedClientId = "client-123";
        final var expectedClientSecret ="secret-123";

        ReflectionTestUtils.setField(this.manager, "credentials", new ClientCredentials(expectedClientId, "acc", "ref"));

        doReturn(expectedClientId).when(keycloakProperties).clientId();
        doReturn(expectedClientSecret).when(keycloakProperties).clientSecret();

        doThrow(InternalErrorException.with("BLA!"))
                .when(authenticationGateway).refresh(new RefreshTokenInput(expectedClientId, expectedClientSecret, "ref"));

        doReturn(new AuthenticationResult(expectedAccessToken, expectedRefreshToken))
                .when(authenticationGateway).login(new ClientCredentialsInput(expectedClientId, expectedClientSecret));

        this.manager.refresh();

        final var actualCredentials = (ClientCredentials)ReflectionTestUtils.getField(this.manager, "credentials");
        Assertions.assertEquals(expectedAccessToken,actualCredentials.accessToken());
        Assertions.assertEquals(expectedRefreshToken,actualCredentials.refreshToken());
    }
}