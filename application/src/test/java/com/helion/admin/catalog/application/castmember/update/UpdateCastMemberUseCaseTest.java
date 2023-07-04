package com.helion.admin.catalog.application.castmember.update;

import com.helion.admin.catalog.application.UseCaseTest;
import com.helion.admin.catalog.domain.Fixture;
import com.helion.admin.catalog.domain.castmember.CastMember;
import com.helion.admin.catalog.domain.castmember.CastMemberGateway;
import com.helion.admin.catalog.domain.castmember.CastMemberType;
import com.helion.admin.catalog.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class UpdateCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUpdateCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;


    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGateway);
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateCastMember_ShouldReturnGenreID(){

        final var aMember = CastMember.newMember("Um Ator", CastMemberType.ACTOR);
        final var expectedName = Fixture.name();
        final var expectedType = CastMemberType.DIRECTOR;
        final var expectedID = aMember.getId();

        final var aCommand = UpdateCastMemberCommand.with(expectedID.getValue(), expectedName, expectedType);

        when(castMemberGateway.findById(any())).thenReturn(Optional.of(CastMember.with(aMember)));
        when(castMemberGateway.update(any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(castMemberGateway, times(1)).update(Mockito.argThat(updatedMember ->
                Objects.equals(expectedName, updatedMember.getName())

                        && Objects.equals(expectedType, updatedMember.getType())
                        && Objects.nonNull(updatedMember.getCreatedAt())
                        && Objects.nonNull(updatedMember.getUpdatedAt())
                        && Objects.nonNull(updatedMember.getId())
                        && Objects.equals(aMember.getCreatedAt(), updatedMember.getCreatedAt())
                        && aMember.getUpdatedAt().isBefore(updatedMember.getUpdatedAt())

        ));
    }

    @Test
    public void givenAinValidName_whenCallsUpdateCastMember_ShouldReturnNotificationException(){

        final var aMember = CastMember.newMember("Um Ator", CastMemberType.ACTOR);
        final String expectedName = null;
        final var expectedType = Fixture.CastMembers.type();
        final var expectedID = aMember.getId();

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateCastMemberCommand.with(expectedID.getValue(),expectedName, expectedType);

        when(castMemberGateway.findById(any())).thenReturn(Optional.of(CastMember.with(aMember)));

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());

        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(castMemberGateway, times(1)).findById(eq(expectedID));
    }
    @Test
    public void givenAnEmptyName_whenCallsUpdateCastMember_ShouldReturnNotificationException(){

        final var aMember = CastMember.newMember("Um Ator", CastMemberType.ACTOR);
        final var expectedName = " ";
        final var expectedType = Fixture.CastMembers.type();
        final var expectedID = aMember.getId();

        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateCastMemberCommand.with(expectedID.getValue(),expectedName, expectedType);

        when(castMemberGateway.findById(any())).thenReturn(Optional.of(CastMember.with(aMember)));

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());

        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(castMemberGateway, times(1)).findById(eq(expectedID));
    }

    @Test
    public void givenAinValidType_whenCallsUpdateCastMember_ShouldReturnNotificationException(){

        final var aMember = CastMember.newMember("Um Ator", CastMemberType.ACTOR);
        final String expectedName = Fixture.name();
        final CastMemberType expectedType = null;
        final var expectedID = aMember.getId();

        final var expectedErrorMessage = "'type' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateCastMemberCommand.with(expectedID.getValue(),expectedName, expectedType);

        when(castMemberGateway.findById(any())).thenReturn(Optional.of(CastMember.with(aMember)));

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());

        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(castMemberGateway, times(1)).findById(eq(expectedID));
    }
}
