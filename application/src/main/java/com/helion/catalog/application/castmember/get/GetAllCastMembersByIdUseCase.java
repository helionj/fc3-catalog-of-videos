package com.helion.catalog.application.castmember.get;

import com.helion.catalog.application.UseCase;
import com.helion.catalog.domain.castmember.CastMember;
import com.helion.catalog.domain.castmember.CastMemberGateway;
import com.helion.catalog.domain.castmember.CastMemberType;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class GetAllCastMembersByIdUseCase extends UseCase<GetAllCastMembersByIdUseCase.Input, List<GetAllCastMembersByIdUseCase.Output>> {

    private final CastMemberGateway castMemberGateway;

    public GetAllCastMembersByIdUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public List<Output> execute(Input in) {
        if(in.ids().isEmpty()){
            return List.of();
        }
        return this.castMemberGateway.findAllById(in.ids())
                .stream()
                .map(Output::new)
                .toList();
    }

    public record Input(Set<String> ids){
        @Override
        public Set<String> ids() {
            return ids != null ? ids : Collections.emptySet();
        }
    }

    public record Output(
            String id,
            String name,
            CastMemberType type,
            Instant createdAt,
            Instant updatedAt

    ){
        public Output(CastMember aMember){
            this(
                    aMember.id(), aMember.name(), aMember.type(), aMember.createdAt(), aMember.updatedAt()
            );
        }
    }
}
