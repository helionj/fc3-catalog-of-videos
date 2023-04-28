package com.helion.admin.catalog.infrastructure.castmember;

import com.helion.admin.catalog.domain.castmember.CastMember;
import com.helion.admin.catalog.domain.castmember.CastMemberGateway;
import com.helion.admin.catalog.domain.castmember.CastMemberID;
import com.helion.admin.catalog.domain.category.pagination.Pagination;
import com.helion.admin.catalog.domain.category.pagination.SearchQuery;
import com.helion.admin.catalog.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.helion.admin.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import com.helion.admin.catalog.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class CastMemberMySQLGateway implements CastMemberGateway {

    private final CastMemberRepository repository;

    public CastMemberMySQLGateway(CastMemberRepository castMemberRepository) {
        this.repository = Objects.requireNonNull(castMemberRepository);
    }

    @Override
    public CastMember create(CastMember aMember) {
        return save(aMember);
    }

    @Override
    public void deleteById(CastMemberID anId) {
        final var anIdValue = anId.getValue();
        if (this.repository.existsById(anIdValue)) {
            this.repository.deleteById(anIdValue);
        }
    }

    @Override
    public Optional<CastMember> findById(CastMemberID anId) {
        return this.repository.findById(anId.getValue())
                .map(CastMemberJpaEntity::toAggregate);
    }

    @Override
    public CastMember update(CastMember aMember) {
        return save(aMember);
    }

    @Override
    public Pagination<CastMember> findAll(SearchQuery aQuery) {
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final var where = Optional.ofNullable(aQuery.terms())
                .filter(str -> !str.isBlank())
                .map(this::assembleSpecification)
                .orElse(null);

        final var pageResults = this.repository.findAll(Specification.where(where), page);

        return new Pagination<>(
                pageResults.getNumber(),
                pageResults.getSize(),
                pageResults.getTotalElements(),
                pageResults.map(CastMemberJpaEntity::toAggregate).toList()
        );
    }

    private Specification<CastMemberJpaEntity> assembleSpecification(final String terms) {
        return SpecificationUtils.like("name", terms);
    }

    private CastMember save(final CastMember aMember) {
        return this.repository.save(CastMemberJpaEntity.from(aMember)).toAggregate();
    }
}
