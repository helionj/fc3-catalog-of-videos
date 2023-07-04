package com.helion.admin.catalog.application.castmember.retrieve.get;

import com.helion.admin.catalog.IntegrationTest;
import com.helion.admin.catalog.domain.Fixture;
import com.helion.admin.catalog.domain.castmember.CastMember;
import com.helion.admin.catalog.domain.castmember.CastMemberGateway;
import com.helion.admin.catalog.domain.castmember.CastMemberID;
import com.helion.admin.catalog.domain.exceptions.NotFoundException;
import com.helion.admin.catalog.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.helion.admin.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@IntegrationTest
public class GetCastMemberByIdUseCaseIT{

    @Autowired
    private GetCastMemberByIdUseCase useCase;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @SpyBean
    private CastMemberGateway castMemberGateway;


    @Test
    public void givenAnValidId_whenCallsGetCastMemberById_shouldReturnsACastMember(){
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();
        final var aMember = CastMember.newMember(expectedName, expectedType);
        final var expectedID = aMember.getId();

        this.castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        final var actualMember = useCase.execute(expectedID.getValue());


        Assertions.assertEquals(expectedID.getValue(), actualMember.id());
        Assertions.assertEquals(expectedName, actualMember.name());
        Assertions.assertEquals(aMember.getCreatedAt(), actualMember.createdAt());
        Assertions.assertEquals(aMember.getUpdatedAt(), actualMember.updatedAt());
        Assertions.assertEquals(CastMemberOutput.from(aMember), actualMember);
        Mockito.verify(castMemberGateway, times(1)).findById(any());
    }

    @Test
    public void givenAiNValidId_whenCallsGetCastMemberById_shouldReturnsNotFound(){
        final var expectedId = CastMemberID.from("123");
        final var expectedErrorMessage = "CastMember with ID 123 was not found";
        final var actualException = Assertions.assertThrows(NotFoundException.class,
                () -> useCase.execute(expectedId.getValue()));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }




}
