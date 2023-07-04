package com.helion.admin.catalog.application.castmember.retrieve.get;

import com.helion.admin.catalog.application.UseCaseTest;
import com.helion.admin.catalog.domain.Fixture;
import com.helion.admin.catalog.domain.castmember.CastMember;
import com.helion.admin.catalog.domain.castmember.CastMemberGateway;
import com.helion.admin.catalog.domain.castmember.CastMemberID;
import com.helion.admin.catalog.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class GetCastMemberByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    DefaultGetCastMemberByIdUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGateway);
    }

    @Test
    public void givenAnValidId_whenCallsGetCastMemberById_shouldReturnsACastMember(){
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();
        final var aMember = CastMember.newMember(expectedName, expectedType);
        final var expectedID = aMember.getId();

        when(castMemberGateway.findById(eq(expectedID))).thenReturn(Optional.of(aMember));

        final var actualMember = useCase.execute(expectedID.getValue());

        Assertions.assertEquals(expectedID.getValue(), actualMember.id());
        Assertions.assertEquals(expectedName, actualMember.name());
        Assertions.assertEquals(aMember.getCreatedAt(), actualMember.createdAt());
        Assertions.assertEquals(aMember.getUpdatedAt(), actualMember.updatedAt());
        Assertions.assertEquals(CastMemberOutput.from(aMember), actualMember);
        Mockito.verify(castMemberGateway, times(1)).findById(expectedID);
    }

    @Test
    public void givenAiNValidId_whenCallsGetCastMemberById_shouldReturnsNotFound(){
        final var expectedId = CastMemberID.from("123");
        final var expectedErrorMessage = "CastMember with ID 123 was not found";
        when(castMemberGateway.findById(eq(expectedId))).thenReturn(Optional.empty());
        final var actualException = Assertions.assertThrows(NotFoundException.class,
                () -> useCase.execute(expectedId.getValue()));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenAnValidId_whenGatewayThrowsException_shouldReturnsException(){
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();
        final var aMember = CastMember.newMember(expectedName, expectedType);
        final var expectedID = aMember.getId();
        final var expectedErrorMessage ="Gateway Error";

        when(castMemberGateway.findById(eq(expectedID))).thenThrow(new IllegalStateException("Gateway Error"));

        final var actualException = Assertions.assertThrows(IllegalStateException.class, () ->  useCase.execute(expectedID.getValue()));
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
        Mockito.verify(castMemberGateway, times(1)).findById(expectedID);
    }


}
