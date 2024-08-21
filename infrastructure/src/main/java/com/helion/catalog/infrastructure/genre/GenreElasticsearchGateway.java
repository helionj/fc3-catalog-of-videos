package com.helion.catalog.infrastructure.genre;

import com.helion.catalog.domain.genre.Genre;
import com.helion.catalog.domain.genre.GenreGateway;
import com.helion.catalog.domain.genre.GenreSearchQuery;
import com.helion.catalog.domain.pagination.Pagination;
import com.helion.catalog.infrastructure.genre.persistence.GenreDocument;
import com.helion.catalog.infrastructure.genre.persistence.GenreRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchOperations;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.StreamSupport;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

@Component
@Profile("!development")
public class GenreElasticsearchGateway implements GenreGateway {

    public static final String NAME = "name";
    public static final String KEYWORD = ".keyword";
    private final GenreRepository genreRepository;
    private final SearchOperations searchOperations;

    public GenreElasticsearchGateway(
            final GenreRepository genreRepository,
            final SearchOperations searchOperations) {
        this.genreRepository = Objects.requireNonNull(genreRepository);
        this.searchOperations = Objects.requireNonNull(searchOperations);
    }

    @Override
    public Genre save(Genre aGenre) {
        this.genreRepository.save(GenreDocument.from(aGenre));
        return aGenre;
    }

    @Override
    public void deleteById(String anId) {
        this.genreRepository.deleteById(anId);
    }

    @Override
    public Optional<Genre> findById(String anId) {
        return this.genreRepository.findById(anId).map(GenreDocument::toGenre);
    }

    @Override
    public Pagination<Genre> findAll(GenreSearchQuery aQuery) {

        final var terms = aQuery.terms();
        final var currentPage = aQuery.page();
        final var perPage = aQuery.perPage();

        final var sort = Sort.by(Sort.Direction.fromString(aQuery.direction()), buildSort(aQuery.sort()));

        final var pageRequest = PageRequest.of(currentPage, perPage, sort);


        final Query query = isEmpty(terms) && isEmpty(aQuery.categories())
                ? Query.findAll().setPageable(pageRequest)
                : new CriteriaQuery(createCriteria(aQuery), pageRequest);

        final var res = this.searchOperations.search(query, GenreDocument.class);

        final var total = res.getTotalHits();
        final var genres = res.stream()
                .map(SearchHit::getContent)
                .map(GenreDocument::toGenre)
                .toList();
        return new Pagination<>(currentPage, perPage, total, genres);
    }

    @Override
    public List<Genre> findAllById(Set<String> ids) {
        if(ids == null || ids.isEmpty()) {
            return List.of();
        }
        return StreamSupport.stream(this.genreRepository.findAllById(ids).spliterator(), false)
                .map(GenreDocument::toGenre)
                .toList();
    }


    private static Criteria createCriteria(final GenreSearchQuery aQuery){
        Criteria criteria = null;

        if (isNotEmpty(aQuery.terms())){
            criteria = Criteria.where("name").contains(aQuery.terms());
        }
        if(!isEmpty(aQuery.categories())){
            final var categoriesWhere = Criteria.where("categories").in(aQuery.categories());
            criteria = criteria != null ? criteria.and(categoriesWhere): categoriesWhere;
        }
       return criteria;
    }

    private String buildSort(String sort){

        if(NAME.equals(sort)) {
            return sort.concat(KEYWORD);
        } else {
            return sort;
        }
    }
}
