package com.helion.admin.catalog.application.castmember.retrieve.list;

import com.helion.admin.catalog.domain.castmember.CastMemberGateway;
import com.helion.admin.catalog.domain.category.pagination.Pagination;
import com.helion.admin.catalog.domain.category.pagination.SearchQuery;

import java.util.Objects;

public final class DefaultListCastMemberUseCase extends ListCastMemberUseCase{

    private final CastMemberGateway castMemberGateway;

    public DefaultListCastMemberUseCase(CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public Pagination<CastMemberListOutput> execute(SearchQuery aQuery) {
        return this.castMemberGateway.findAll(aQuery).map(CastMemberListOutput::from);
    }
}
