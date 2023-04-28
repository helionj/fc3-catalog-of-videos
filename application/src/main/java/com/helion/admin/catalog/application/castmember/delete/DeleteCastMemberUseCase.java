package com.helion.admin.catalog.application.castmember.delete;

import com.helion.admin.catalog.application.UnitUseCase;

public sealed abstract class DeleteCastMemberUseCase
        extends UnitUseCase<String>
        permits DefaultDeleteCastMemberUseCase {
}
