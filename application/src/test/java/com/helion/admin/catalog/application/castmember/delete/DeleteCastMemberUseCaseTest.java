package com.helion.admin.catalog.application.castmember.delete;

import com.helion.admin.catalog.application.UseCaseTest;
import com.helion.admin.catalog.domain.castmember.CastMember;
import com.helion.admin.catalog.domain.castmember.CastMemberGateway;
import com.helion.admin.catalog.domain.castmember.CastMemberID;
import com.helion.admin.catalog.domain.castmember.CastMemberType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class DeleteCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultDeleteCastMemberUseCase useCase;
    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGateway);
    }

    @Test
    public void givenAValidCastMemberID_whenCallsDeleteCastMember_shouldBeDeleted(){
        final var aMember = CastMember.newMember("Vin Diesel", CastMemberType.DIRECTOR);
        final var expectedId = aMember.getId();
        doNothing().when(castMemberGateway).deleteById(eq(expectedId));
        Assertions.assertDoesNotThrow(() ->  useCase.execute(expectedId.getValue()));

        Mockito.verify(castMemberGateway, times(1)).deleteById(expectedId);
    }

    @Test
    public void givenAinValidCastMemberID_whenCallsDeleteCastMember_shouldBeOk(){
        final var expectedId = CastMemberID.from("invalidID");
        doNothing().when(castMemberGateway).deleteById(eq(expectedId));
        Assertions.assertDoesNotThrow(() ->  useCase.execute(expectedId.getValue()));

        Mockito.verify(castMemberGateway, times(1)).deleteById(expectedId);
    }

    @Test
    public void givenAValidCastMemberID_whenCallsDeleteCastMemberAndGatewayThrowsUnexpectedError_shouldReceiveAnException(){
        final var aMember = CastMember.newMember("Vin Diesel", CastMemberType.DIRECTOR);
        final var expectedId = aMember.getId();

        doThrow(new IllegalStateException("Gateway Error"))
                .when(castMemberGateway).deleteById(any());

        Assertions.assertThrows(IllegalStateException.class, () ->  useCase.execute(expectedId.getValue()));

        Mockito.verify(castMemberGateway, times(1)).deleteById(expectedId);

    }
}
