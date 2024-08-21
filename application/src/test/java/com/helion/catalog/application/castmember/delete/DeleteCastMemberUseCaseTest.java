package com.helion.catalog.application.castmember.delete;

import com.helion.catalog.application.UseCaseTest;
import com.helion.catalog.domain.Fixture;
import com.helion.catalog.domain.castmember.CastMemberGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class DeleteCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DeleteCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Test
    public void givenValidId_whenCallsDelete_shouldBeOk(){

        final var ator = Fixture.CastMembers.actor();
        final var expectedId = ator.id();

        doNothing().when(castMemberGateway).deleteById(anyString());

        Assertions.assertDoesNotThrow(() -> this.useCase.execute(expectedId));

        verify(this.castMemberGateway, times(1)).deleteById(eq(expectedId));
    }

    @Test
    public void givenInValidId_whenCallsDelete_shouldBeOk(){

        final String expectedId = null;

        //doNothing().when(CastMemberGateway).deleteById(anyString());

        Assertions.assertDoesNotThrow(() -> this.useCase.execute(expectedId));

        verify(this.castMemberGateway, times(0)).deleteById(eq(expectedId));
    }

}
