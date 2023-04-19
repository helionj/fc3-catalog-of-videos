package com.helion.admin.catalog.domain.castmember;

import com.helion.admin.catalog.domain.AgregateRoot;
import com.helion.admin.catalog.domain.exceptions.NotificationException;
import com.helion.admin.catalog.domain.utils.InstantUtils;
import com.helion.admin.catalog.domain.validation.ValidationHandler;
import com.helion.admin.catalog.domain.validation.handler.Notification;

import java.time.Instant;

public class CastMember extends AgregateRoot<CastMemberID> {

    private String name;
    private CastMemberType type;
    private Instant createdAt;
    private Instant updatedAt;

    protected CastMember(
            final CastMemberID anID,
            final String aName,
            final CastMemberType aType,
            final Instant aCreationDate,
            final Instant aUpdatedDate) {
            super(anID);
            this.name = aName;
            this.type = aType;
            this.createdAt = aCreationDate;
            this.updatedAt = aUpdatedDate;
            selfValidate();
    }

    public String getName() {
        return name;
    }

    public CastMemberType getType() {
        return type;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public CastMember update(String expectedName, CastMemberType expectedType) {
        this.name = expectedName;
        this.type = expectedType;
        selfValidate();
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public static CastMember newMember(final String aName, final CastMemberType aType){
        final var id = CastMemberID.unique();
        final var now = InstantUtils.now();
        return new CastMember(id, aName, aType, now, now);
    }

    public static CastMember with(
            final CastMemberID anID,
            final String aName,
            final CastMemberType aType,
            final Instant aCreationDate,
            final Instant aUpdatedDate
    ){
        return new CastMember(anID, aName, aType, aCreationDate, aUpdatedDate);
    }
    public static CastMember with(
            final CastMember aMember

    ){
        return new CastMember(
                aMember.id,
                aMember.name,
                aMember.type,
                aMember.createdAt,
                aMember.updatedAt);
    }

    private void selfValidate() {
        final var notification = Notification.create();
        validate(notification);

        if(notification.hasErrors()){
            throw new NotificationException("Failed to create aggregate MemberCast", notification);
        }
    }

    @Override
    public void validate(ValidationHandler handler) {
        new CastMemberValidator(this, handler).validate();
    }


}
