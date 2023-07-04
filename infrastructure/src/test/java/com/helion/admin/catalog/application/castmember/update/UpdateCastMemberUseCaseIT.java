package com.helion.admin.catalog.application.castmember.update;

import com.helion.admin.catalog.IntegrationTest;
import com.helion.admin.catalog.domain.Fixture;
import com.helion.admin.catalog.domain.castmember.CastMember;
import com.helion.admin.catalog.domain.castmember.CastMemberGateway;
import com.helion.admin.catalog.domain.castmember.CastMemberType;
import com.helion.admin.catalog.domain.exceptions.NotificationException;
import com.helion.admin.catalog.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.helion.admin.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@IntegrationTest
public class UpdateCastMemberUseCaseIT{

    @Autowired
    private UpdateCastMemberUseCase useCase;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @SpyBean
    private CastMemberGateway castMemberGateway;



    @Test
    public void givenAValidCommand_whenCallsUpdateCastMember_ShouldReturnGenreID(){

        final var aMember = CastMember.newMember("Um Ator", CastMemberType.ACTOR);
        final var expectedName = Fixture.name();
        final var expectedType = CastMemberType.DIRECTOR;
        final var expectedID = aMember.getId();

        final var actualUpdatedAt = aMember.getUpdatedAt();
        this.castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        final var aCommand = UpdateCastMemberCommand.with(expectedID.getValue(), expectedName, expectedType);



        final var actualOutput = useCase.execute(aCommand);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final var updatedMember = this.castMemberRepository.findById(expectedID.getValue()).get();

        Assertions.assertEquals(expectedName, updatedMember.getName());
        Assertions.assertEquals(expectedType, updatedMember.getType());
        Assertions.assertNotNull(updatedMember.getCreatedAt());
        Assertions.assertNotNull(updatedMember.getUpdatedAt());
        Assertions.assertTrue(updatedMember.getCreatedAt().isBefore(updatedMember.getUpdatedAt()));
        Assertions.assertTrue(actualUpdatedAt.isBefore(updatedMember.getUpdatedAt()));

        verify(castMemberGateway).findById(any());
        verify(castMemberGateway).update(any());

    }

    @Test
    public void givenAinValidName_whenCallsUpdateCastMember_ShouldReturnNotificationException(){

        final var aMember = CastMember.newMember("Um Ator", CastMemberType.ACTOR);
        final String expectedName = null;
        final var expectedType = Fixture.CastMembers.type();
        final var expectedID = aMember.getId();

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        this.castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        final var aCommand = UpdateCastMemberCommand.with(expectedID.getValue(),expectedName, expectedType);


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

        this.castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        final var aCommand = UpdateCastMemberCommand.with(expectedID.getValue(),expectedName, expectedType);


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

        this.castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        final var aCommand = UpdateCastMemberCommand.with(expectedID.getValue(),expectedName, expectedType);

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());

        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(castMemberGateway, times(1)).findById(eq(expectedID));
    }
}
