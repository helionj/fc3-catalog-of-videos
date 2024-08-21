package com.helion.catalog.infrastructure.castmember;

import com.helion.catalog.application.castmember.get.GetAllCastMembersByIdUseCase;
import com.helion.catalog.application.castmember.list.ListCastMemberOutput;
import com.helion.catalog.domain.castmember.CastMember;
import com.helion.catalog.infrastructure.castmember.models.GqlCastMember;

public class GqlCastMemberPresenter {

    private GqlCastMemberPresenter(){}

    public static GqlCastMember present(final ListCastMemberOutput out){
        return new GqlCastMember(out.id(), out.name(), out.type().name(), out.createdAt().toString(), out.createdAt().toString());
    }

    public static GqlCastMember present(final GetAllCastMembersByIdUseCase.Output out){
        return new GqlCastMember(out.id(), out.name(), out.type().name(), out.createdAt().toString(), out.createdAt().toString());
    }

    public static GqlCastMember present(final CastMember out){
        return new GqlCastMember(out.id(), out.name(), out.type().name(), out.createdAt().toString(), out.createdAt().toString());
    }
}
