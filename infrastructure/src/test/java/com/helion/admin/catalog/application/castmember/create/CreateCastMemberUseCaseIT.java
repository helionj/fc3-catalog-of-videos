package com.helion.admin.catalog.application.castmember.create;

import com.helion.admin.catalog.IntegrationTest;
import com.helion.admin.catalog.domain.Fixture;
import com.helion.admin.catalog.domain.castmember.CastMemberGateway;
import com.helion.admin.catalog.domain.castmember.CastMemberType;
import com.helion.admin.catalog.domain.exceptions.NotificationException;
import com.helion.admin.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@IntegrationTest
public class CreateCastMemberUseCaseIT {

    @Autowired
    private CreateCastMemberUseCase useCase;

    @SpyBean
    private CastMemberGateway castMemberGateway;

    @Autowired
    private CastMemberRepository castMemberRepository;


    @Test
    public void givenValidCommand_whenCallsCreateCastMember_shouldReturnCastMemberID(){
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        final var aCommand = CreateCastMemberCommand.with(expectedName, expectedType);

        final var actualOutput = useCase.execute(aCommand);


        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final var actualMember = castMemberRepository.findById(actualOutput.id()).get();

        Assertions.assertEquals(expectedName, actualMember.getName());
        Assertions.assertEquals(expectedType, actualMember.getType());
        Assertions.assertNotNull(actualMember.getCreatedAt());
        Assertions.assertNotNull(actualMember.getUpdatedAt());

        verify(castMemberGateway).create(any());

    }

    @Test
    public void givenAnInvalidNullName_whenCallsCreateCastMember_shouldReturnDomainException(){
        final String expectedName = null;
        final var expectedType = Fixture.CastMembers.type();
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount= 1;

        final var aCommand = CreateCastMemberCommand.with(expectedName, expectedType);

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(castMemberGateway, times(0)).create(any());
    }
    @Test
    public void givenAnInvalidEmptyName_whenCallsCreateCastMember_shouldReturnDomainException(){
        final String expectedName = " ";
        final var expectedType = Fixture.CastMembers.type();
        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount= 1;

        final var aCommand = CreateCastMemberCommand.with(expectedName, expectedType);

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(castMemberGateway, times(0)).create(any());
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

        verify(castMemberGateway, times(0)).create(any());
    }
}
