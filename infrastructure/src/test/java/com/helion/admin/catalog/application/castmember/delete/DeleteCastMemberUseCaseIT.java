package com.helion.admin.catalog.application.castmember.delete;

import com.helion.admin.catalog.IntegrationTest;
import com.helion.admin.catalog.domain.castmember.CastMember;
import com.helion.admin.catalog.domain.castmember.CastMemberGateway;
import com.helion.admin.catalog.domain.castmember.CastMemberID;
import com.helion.admin.catalog.domain.castmember.CastMemberType;
import com.helion.admin.catalog.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.helion.admin.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.mockito.Mockito.times;

@IntegrationTest
public class DeleteCastMemberUseCaseIT{

    @Autowired
    private DeleteCastMemberUseCase useCase;
    @Autowired
    private CastMemberRepository castMemberRepository;
    @SpyBean
    private CastMemberGateway castMemberGateway;



    @Test
    public void givenAValidCastMemberID_whenCallsDeleteCastMember_shouldBeDeleted(){
        final var aMember = CastMember.newMember("Vin Diesel", CastMemberType.DIRECTOR);
        final var expectedId = aMember.getId();

        Assertions.assertEquals(0, this.castMemberRepository.count());
        this.castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        Assertions.assertEquals(1, this.castMemberRepository.count());
        Assertions.assertDoesNotThrow(() ->  useCase.execute(expectedId.getValue()));
        Assertions.assertEquals(0, this.castMemberRepository.count());
        Mockito.verify(castMemberGateway, times(1)).deleteById(expectedId);
    }

    @Test
    public void givenAinValidCastMemberID_whenCallsDeleteCastMember_shouldBeOk(){
        final var expectedId = CastMemberID.from("invalidID");
        Assertions.assertDoesNotThrow(() ->  useCase.execute(expectedId.getValue()));

        Mockito.verify(castMemberGateway, times(1)).deleteById(expectedId);
    }


}
