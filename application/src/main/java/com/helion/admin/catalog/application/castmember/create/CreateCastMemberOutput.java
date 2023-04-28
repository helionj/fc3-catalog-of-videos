package com.helion.admin.catalog.application.castmember.create;

import com.helion.admin.catalog.domain.castmember.CastMember;

public record CreateCastMemberOutput(String id) {

    public static CreateCastMemberOutput from(final String anId){
        return new CreateCastMemberOutput(anId);
    }

    public static CreateCastMemberOutput from(final CastMember aMember){
        return new CreateCastMemberOutput(aMember.getId().getValue());
    }
}
