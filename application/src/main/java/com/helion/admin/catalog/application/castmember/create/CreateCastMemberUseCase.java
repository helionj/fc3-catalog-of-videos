package com.helion.admin.catalog.application.castmember.create;

import com.helion.admin.catalog.application.UseCase;

public  sealed abstract class CreateCastMemberUseCase
        extends UseCase<CreateCastMemberCommand, CreateCastMemberOutput>
        permits DefaultCreateCastMemberUseCase{}

