package com.helion.admin.catalog.application.castmember.delete;

import com.helion.admin.catalog.domain.castmember.CastMemberGateway;
import com.helion.admin.catalog.domain.castmember.CastMemberID;

import java.util.Objects;

public final class DefaultDeleteCastMemberUseCase extends DeleteCastMemberUseCase{

    private final CastMemberGateway castMemberGateway;

    public DefaultDeleteCastMemberUseCase(CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public void execute(String anId) {
        this.castMemberGateway.deleteById(CastMemberID.from(anId));
    }
}
