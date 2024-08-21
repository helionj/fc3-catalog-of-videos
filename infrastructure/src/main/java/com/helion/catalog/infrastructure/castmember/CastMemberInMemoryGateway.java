package com.helion.catalog.infrastructure.castmember;

import com.helion.catalog.domain.castmember.CastMember;
import com.helion.catalog.domain.castmember.CastMemberGateway;
import com.helion.catalog.domain.castmember.CastMemberSearchQuery;
import com.helion.catalog.domain.pagination.Pagination;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Profile("development")
public class CastMemberInMemoryGateway implements CastMemberGateway {

    Map<String, CastMember> db;

    public CastMemberInMemoryGateway() {
        this.db = new ConcurrentHashMap<>();
    }

    @Override
    public CastMember save(CastMember aMember) {
        this.db.put(aMember.id(), aMember);
        return aMember;
    }

    @Override
    public void deleteById(String memberId) {
        if(db.containsKey(memberId)){
            this.db.remove(memberId);
        }

    }

    @Override
    public Optional<CastMember> findById(String memberId) {
        return Optional.ofNullable(this.db.get(memberId));
    }

    @Override
    public Pagination<CastMember> findAll(CastMemberSearchQuery aQuery) {
        return new Pagination<>(
                aQuery.page(),
                aQuery.perPage(),
                this.db.values().size(),
                this.db.values().stream().toList()

        );
    }

    @Override
    public List<CastMember> findAllById(Set<String> ids) {

        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return ids.stream()
                .map(this.db::get)
                .toList();

    }
}
