package com.helion.admin.catalog.application.castmember.create;

import com.helion.admin.catalog.domain.castmember.CastMember;
import com.helion.admin.catalog.domain.castmember.CastMemberGateway;
import com.helion.admin.catalog.domain.exceptions.NotificationException;
import com.helion.admin.catalog.domain.validation.handler.Notification;

public final class DefaultCreateCastMemberUseCase extends CreateCastMemberUseCase{

    private final CastMemberGateway castMemberGateway;

    public DefaultCreateCastMemberUseCase(CastMemberGateway castMemberGateway) {
        this.castMemberGateway = castMemberGateway;
    }

    @Override
    public CreateCastMemberOutput execute(CreateCastMemberCommand aCommand) {
        final var name = aCommand.name();
        final var type = aCommand.type();
        final var notification = Notification.create();
        final var aMember = notification.validate(() -> CastMember.newMember(name, type));

        if (notification.hasErrors()){
            throw new NotificationException("Could not create Aggregate CastMember", notification);
        }
        return CreateCastMemberOutput.from(this.castMemberGateway.create(aMember));
    }
}
