package com.helion.admin.catalog.application.castmember.retrieve.get;

import com.helion.admin.catalog.application.UseCase;

public sealed abstract class GetCastMemberByIdUseCase
        extends UseCase<String, CastMemberOutput>
        permits DefaultGetCastMemberByIdUseCase {
}
