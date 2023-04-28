package com.helion.admin.catalog.application.castmember.create;

import com.helion.admin.catalog.application.Fixture;
import com.helion.admin.catalog.application.UseCaseTest;
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

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class CreateCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultCreateCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;
    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGateway);
    }

    @Test
    public void givenValidCommand_whenCallsCreateCastMember_shouldReturnCastMemberID(){
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();

        final var aCommand = CreateCastMemberCommand.with(expectedName, expectedType);

        when(castMemberGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(castMemberGateway, times(1)).create(Mockito.argThat(aMember ->
                Objects.equals(expectedName, aMember.getName())

                        && Objects.equals(expectedType, aMember.getType())
                        && Objects.nonNull(aMember.getCreatedAt())
                        && Objects.nonNull(aMember.getUpdatedAt())
                        && Objects.nonNull(aMember.getId())
        ));
    }

    @Test
    public void givenAnInvalidNullName_whenCallsCreateCastMember_shouldReturnDomainException(){
        final String expectedName = null;
        final var expectedType = Fixture.CastMember.type();
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount= 1;

        final var aCommand = CreateCastMemberCommand.with(expectedName, expectedType);

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(castMemberGateway, times(0)).create(any());
    }
    @Test
    public void givenAnInvalidEmptyName_whenCallsCreateCastMember_shouldReturnDomainException(){
        final String expectedName = " ";
        final var expectedType = Fixture.CastMember.type();
        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount= 1;

        final var aCommand = CreateCastMemberCommand.with(expectedName, expectedType);

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(castMemberGateway, times(0)).create(any());
    }

    @Test
    public void givenAnInvalidNullType_whenCallsCreateCastMember_shouldReturnDomainException(){
        final var expectedName = Fixture.name();
        final CastMemberType expectedType = null;
        final var expectedErrorMessage = "'type' should not be null";
        final var expectedErrorCount= 1;

        final var aCommand = CreateCastMemberCommand.with(expectedName, expectedType);

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(castMemberGateway, times(0)).create(any());
    }
}
