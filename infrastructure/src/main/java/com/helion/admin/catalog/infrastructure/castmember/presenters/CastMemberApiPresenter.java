package com.helion.admin.catalog.infrastructure.castmember.presenters;

import com.helion.admin.catalog.application.castmember.retrieve.get.CastMemberOutput;
import com.helion.admin.catalog.application.castmember.retrieve.list.CastMemberListOutput;
import com.helion.admin.catalog.infrastructure.castmember.models.CastMemberListResponse;
import com.helion.admin.catalog.infrastructure.castmember.models.CastMemberResponse;

public class CastMemberApiPresenter {

    public static CastMemberResponse present(CastMemberOutput output){
        return new CastMemberResponse(
                output.id(),
                output.name(),
                output.type(),
                output.createdAt(),
                output.updatedAt()
        );
    }

    public static CastMemberListResponse present(CastMemberListOutput output){
        return new CastMemberListResponse(
                output.id(),
                output.name(),
                output.type(),
                output.createdAt()
        );
    }
}
