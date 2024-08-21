package com.helion.catalog.domain.castmember;

import com.helion.catalog.domain.pagination.Pagination;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CastMemberGateway {

    CastMember save(CastMember aMember);

    void deleteById(String anId);

    Optional<CastMember> findById(String anId);

    List<CastMember> findAllById(Set<String> ids);

    Pagination<CastMember> findAll(CastMemberSearchQuery aQuery);

}
