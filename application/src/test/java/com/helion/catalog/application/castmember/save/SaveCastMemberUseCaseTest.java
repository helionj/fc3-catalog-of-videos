package com.helion.catalog.application.castmember.save;

import com.helion.catalog.application.UseCaseTest;
import com.helion.catalog.domain.Fixture;
import com.helion.catalog.domain.castmember.CastMember;
import com.helion.catalog.domain.castmember.CastMemberGateway;
import com.helion.catalog.domain.castmember.CastMemberType;
import com.helion.catalog.domain.exceptions.DomainException;
import com.helion.catalog.domain.utils.InstantUtils;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class SaveCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private SaveCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Test
    public void givenAValidCastMember_whenCallsSave_shouldPersistIt() {

        final var aCastMember = Fixture.CastMembers.actor();

        Mockito.when(castMemberGateway.save(any())).thenAnswer(returnsFirstArg());

        this.useCase.execute(aCastMember);

        verify(castMemberGateway, times(1)).save(aCastMember);
    }

    @Test
    public void givenANullCastMember_whenCallsSave_shouldReturnsError() {

        final var expectedErrorCount = 1;
        final var expectedMessage = "'aCastMember' cannot be null";
        final CastMember aCastMember = null;

        final var actualError = assertThrows(DomainException.class, () -> this.useCase.execute(aCastMember));

        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedMessage, actualError.getErrors().get(0).message());
        verify(castMemberGateway, times(0)).save(aCastMember);
    }

    @Test
    public void givenInValidName_whenCallsSave_shouldReturnError() {
        final var expectedErrorCount = 1;
        final var expectedMessage = "'name' should not be empty";
        final var aCastMember = CastMember.with(
                UUID.randomUUID().toString().replace("-", ""),
                " ",
                CastMemberType.ACTOR,
                InstantUtils.now(),
                InstantUtils.now()
        );

        final var actualError = assertThrows(DomainException.class, () -> this.useCase.execute(aCastMember));

        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedMessage, actualError.getErrors().get(0).message());
        verify(castMemberGateway, times(0)).save(aCastMember);
    }

    @Test
    public void givenInValidId_whenCallsSave_shouldReturnError() {
        final var expectedErrorCount = 1;
        final var expectedMessage = "'id' should not be empty";
        final var aCastMember = CastMember.with(
                " ",
                "Paulo Actor",
                CastMemberType.ACTOR,
                InstantUtils.now(),
                InstantUtils.now()
        );

        final var actualError = assertThrows(DomainException.class, () -> this.useCase.execute(aCastMember));

        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedMessage, actualError.getErrors().get(0).message());
        verify(castMemberGateway, times(0)).save(aCastMember);
    }

    @Test
    public void givenInValidCastMemberType_whenCallsSave_shouldReturnError() {
        final var expectedErrorCount = 1;
        final var expectedMessage = "'type' should not be null";
        final var aCastMember = CastMember.with(
                UUID.randomUUID().toString().replace("-", ""),
                "Paulo Actor",
                null,
                InstantUtils.now(),
                InstantUtils.now()
                );

        final var actualError = assertThrows(DomainException.class, () -> this.useCase.execute(aCastMember));

        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedMessage, actualError.getErrors().get(0).message());
        verify(castMemberGateway, times(0)).save(aCastMember);
    }

}
