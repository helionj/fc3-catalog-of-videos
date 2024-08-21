package com.helion.catalog.application.castmember.save;

import com.helion.catalog.application.UseCase;
import com.helion.catalog.domain.castmember.CastMember;
import com.helion.catalog.domain.castmember.CastMemberGateway;
import com.helion.catalog.domain.exceptions.NotificationException;
import com.helion.catalog.domain.validation.Error;
import com.helion.catalog.domain.validation.handler.Notification;

import java.util.Objects;

public class SaveCastMemberUseCase extends UseCase<CastMember, CastMember> {

    private final CastMemberGateway castMemberGateway;

    public SaveCastMemberUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public CastMember execute(final CastMember aCastMember) {
        if (aCastMember == null){
            throw NotificationException.with(new Error("'aCastMember' cannot be null"));
        }

        final var notification = Notification.create();
        aCastMember.validate(notification);

        if (notification.hasError()){
            throw NotificationException.with("Invalid CastMember", notification);
        }
        return this.castMemberGateway.save(aCastMember);
    }
}
