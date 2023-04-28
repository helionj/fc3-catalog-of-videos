package com.helion.admin.catalog.application.castmember.update;

import com.helion.admin.catalog.domain.castmember.CastMember;
import com.helion.admin.catalog.domain.castmember.CastMemberGateway;
import com.helion.admin.catalog.domain.castmember.CastMemberID;
import com.helion.admin.catalog.domain.exceptions.DomainException;
import com.helion.admin.catalog.domain.exceptions.NotFoundException;
import com.helion.admin.catalog.domain.exceptions.NotificationException;
import com.helion.admin.catalog.domain.validation.handler.Notification;

import java.util.Objects;
import java.util.function.Supplier;

public final class DefaultUpdateCastMemberUseCase extends UpdateCastMemberUseCase{

    private final CastMemberGateway castMemberGateway;

    public DefaultUpdateCastMemberUseCase(CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public UpdateCastMemberOutput execute(UpdateCastMemberCommand aCommand) {

        final var anId = CastMemberID.from(aCommand.id());
        final var aName = aCommand.name();
        final var aType = aCommand.type();


        final var aMember = this.castMemberGateway.findById(anId).orElseThrow(notFound(anId));
        final var notification = Notification.create();

        notification.validate(() -> aMember.update(aName, aType));

        if(notification.hasErrors()){
            throw new NotificationException("Could not update Aggregate CastMember %s".formatted(aCommand.id()), notification);
        }
        final var result = this.castMemberGateway.update(aMember);
        return UpdateCastMemberOutput.from(result);

    }

    private Supplier<DomainException> notFound(final CastMemberID anId) {
        return () -> NotFoundException.with(CastMember.class, anId);
    }
}
