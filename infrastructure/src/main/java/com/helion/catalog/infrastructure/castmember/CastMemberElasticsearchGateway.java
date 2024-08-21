package com.helion.catalog.infrastructure.castmember;

import com.helion.catalog.domain.castmember.CastMember;
import com.helion.catalog.domain.castmember.CastMemberGateway;
import com.helion.catalog.domain.castmember.CastMemberSearchQuery;
import com.helion.catalog.domain.pagination.Pagination;
import com.helion.catalog.infrastructure.castmember.persistence.CastMemberDocument;
import com.helion.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import io.github.resilience4j.core.StringUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchOperations;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.StreamSupport;

import static org.springframework.data.elasticsearch.core.query.Criteria.where;

@Component
@Profile("!development")
public class CastMemberElasticsearchGateway implements CastMemberGateway {

    private final CastMemberRepository castMemberRepository;
    private final SearchOperations searchOperations;

    public CastMemberElasticsearchGateway(final CastMemberRepository castMemberRepository, SearchOperations searchOperations) {
        this.castMemberRepository = Objects.requireNonNull(castMemberRepository);
        this.searchOperations = Objects.requireNonNull(searchOperations);
    }

    @Override
    public CastMember save(CastMember aMember) {
        this.castMemberRepository.save(CastMemberDocument.from(aMember));
        return aMember;
    }

    @Override
    public void deleteById(String anId) {
        this.castMemberRepository.deleteById(anId);
    }

    @Override
    public Optional<CastMember> findById(String anId) {

        return this.castMemberRepository.findById(anId).map(CastMemberDocument::toCastMember);
    }

    @Override
    public List<CastMember> findAllById(Set<String> ids) {
        if(ids == null || ids.isEmpty()) {
            return List.of();
        }
        return StreamSupport.stream(this.castMemberRepository.findAllById(ids).spliterator(), false)
                .map(CastMemberDocument::toCastMember)
                .toList();
    }

    @Override
    public Pagination<CastMember> findAll(CastMemberSearchQuery aQuery) {

        final var terms = aQuery.terms();
        final var currentPage = aQuery.page();
        final var perPage = aQuery.perPage();

        final var sort = Sort.by(Sort.Direction.fromString(aQuery.direction()), buildSort(aQuery.sort()));

        final var pageRequest = PageRequest.of(currentPage, perPage, sort);

        final Query query = StringUtils.isNotEmpty(terms)
                ? new CriteriaQuery(where("name").contains(terms), pageRequest)
                : Query.findAll().setPageable(pageRequest);

        final var res = this.searchOperations.search(query, CastMemberDocument.class);

        final var total = res.getTotalHits();
        final var categories = res.stream()
                .map(SearchHit::getContent)
                .map(CastMemberDocument::toCastMember)
                .toList();
        return new Pagination<>(currentPage, perPage, total, categories);
    }

    private String buildSort(String sort){

        if("name".equals(sort)) {
            return sort.concat(".keyword");
        } else {
            return sort;
        }
     }


}


