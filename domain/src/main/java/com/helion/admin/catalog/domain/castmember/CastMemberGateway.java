package com.helion.admin.catalog.domain.castmember;

import com.helion.admin.catalog.domain.category.CategoryID;
import com.helion.admin.catalog.domain.category.pagination.Pagination;
import com.helion.admin.catalog.domain.category.pagination.SearchQuery;

import java.util.List;
import java.util.Optional;

public interface CastMemberGateway {

    CastMember create(CastMember aMember);

    void deleteById(CastMemberID anId);

    Optional<CastMember> findById(CastMemberID anId);

    CastMember update(CastMember aMember);

    Pagination<CastMember> findAll(SearchQuery aQuery);

    List<CastMemberID> existsByIds(final Iterable<CastMemberID> ids);
}
