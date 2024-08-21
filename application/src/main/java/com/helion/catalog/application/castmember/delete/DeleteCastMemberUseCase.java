package com.helion.catalog.application.castmember.delete;

import com.helion.catalog.application.UnitUseCase;
import com.helion.catalog.domain.castmember.CastMemberGateway;

import java.util.Objects;

public class DeleteCastMemberUseCase extends UnitUseCase<String> {

    private final CastMemberGateway castMemberGateway;

    public DeleteCastMemberUseCase(CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public void execute(String anId) {
        if(anId == null) {
            return;
        }
        this.castMemberGateway.deleteById(anId);
    }
}
