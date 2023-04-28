package com.helion.admin.catalog.application.castmember.update;

import com.helion.admin.catalog.domain.castmember.CastMember;

public record UpdateCastMemberOutput(String id) {

    public static UpdateCastMemberOutput from(final String anId){
        return new UpdateCastMemberOutput(anId);
    }

    public static UpdateCastMemberOutput from(final CastMember aMember){
        return new UpdateCastMemberOutput(aMember.getId().getValue());
    }
}
