package com.helion.catalog.application.castmember.get;

import com.helion.catalog.application.UseCaseTest;
import com.helion.catalog.domain.Fixture;
import com.helion.catalog.domain.castmember.CastMember;
import com.helion.catalog.domain.castmember.CastMemberGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GetAllCastMembersByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private GetAllCastMembersByIdUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Test
    public void givenAValidIds_whenCallsGetAllById_shouldReturnIt() {
        final var castMembers = List.of(
                Fixture.CastMembers.actor(), Fixture.CastMembers.director()
        );

        final var expectedItems = castMembers.stream().map(GetAllCastMembersByIdUseCase.Output::new).toList();

        final var expectedIds = castMembers.stream().map(CastMember::id).collect(Collectors.toSet());



        when(this.castMemberGateway.findAllById(any())).thenReturn(castMembers);

        final var actualOutput = this.useCase.execute(new GetAllCastMembersByIdUseCase.Input(expectedIds));

        Assertions.assertTrue(expectedItems.size() == actualOutput.size() && expectedItems.containsAll(actualOutput));

        verify(castMemberGateway, times(1)).findAllById(any());

    }

    @Test
    public void givenANullIds_whenCallsGetAllById_shouldReturEmpty() {


        final var expectedItems = List.of();

        final Set<String> expectedIds = null;


        final var actualOutput = this.useCase.execute(new GetAllCastMembersByIdUseCase.Input(expectedIds));

        Assertions.assertTrue(actualOutput.isEmpty());

        verify(castMemberGateway, times(0)).findAllById(any());
    }

}