package com.helion.catalog.application.castmember.list;

import com.helion.catalog.application.UseCase;
import com.helion.catalog.domain.castmember.CastMemberGateway;
import com.helion.catalog.domain.castmember.CastMemberSearchQuery;
import com.helion.catalog.domain.pagination.Pagination;

public class ListCastMemberUseCase extends UseCase<CastMemberSearchQuery, Pagination<ListCastMemberOutput>> {

    private final CastMemberGateway castMemberGateway;

    public ListCastMemberUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = castMemberGateway;
    }

    @Override
    public Pagination<ListCastMemberOutput> execute(CastMemberSearchQuery aQuery) {
        return this.castMemberGateway.findAll(aQuery).map(ListCastMemberOutput::from);
    }
}
