package com.helion.catalog.infrastructure.category;

import com.helion.catalog.domain.category.Category;
import com.helion.catalog.domain.category.CategoryGateway;
import com.helion.catalog.domain.category.CategorySearchQuery;
import com.helion.catalog.domain.pagination.Pagination;
import com.helion.catalog.infrastructure.category.persistence.CategoryDocument;
import com.helion.catalog.infrastructure.category.persistence.CategoryRepository;
import org.apache.commons.lang3.StringUtils;
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
@Component
@Profile("!development")
public class CategoryElasticsearchGateway implements CategoryGateway {


    private final CategoryRepository categoryRepository;
    private final SearchOperations searchOperations;

    public CategoryElasticsearchGateway(
            final CategoryRepository categoryRepository,
            final SearchOperations searchOperations) {
        this.categoryRepository = Objects.requireNonNull(categoryRepository);
        this.searchOperations = Objects.requireNonNull(searchOperations);
    }

    @Override
    public Category save(Category aCategory) {
        try {
            this.categoryRepository.save(CategoryDocument.from(aCategory));
            return aCategory;
        } catch(Exception ex){
            System.out.println("ERROR: "+ex.getMessage());
        }
       return null;
    }

    @Override
    public void deleteById(String anId) {
       this.categoryRepository.deleteById(anId);
    }

    @Override
    public Optional<Category> findById(String anId) {
        return this.categoryRepository.findById(anId).map(CategoryDocument::toCategory);
    }

    @Override
    public Pagination<Category> findAll(CategorySearchQuery aQuery) {
        final var terms = aQuery.terms();
        final var currentPage = aQuery.page();
        final var perPage = aQuery.perPage();

        final var sort = Sort.by(Sort.Direction.fromString(aQuery.direction()), buildSort(aQuery.sort()));

        final var page = PageRequest.of(currentPage, perPage, sort);

        final Query query;

        if(StringUtils.isNotEmpty(terms)) {

            final var criteria = Criteria.where("name").contains(terms)
                    .or(Criteria.where("description").contains(terms));
            query = new CriteriaQuery(criteria, page);

        } else {
            query = Query.findAll().setPageable(page);
        }

        final var res = this.searchOperations.search(query, CategoryDocument.class);

        final var total = res.getTotalHits();
        final var categories = res.stream()
                .map(SearchHit::getContent)
                .map(CategoryDocument::toCategory)
                .toList();
        return new Pagination<>(currentPage, perPage, total, categories);
    }

    @Override
    public List<Category> findAllById(Set<String> ids) {
        if(ids == null || ids.isEmpty()) {
            return List.of();
        }
        return StreamSupport.stream(this.categoryRepository.findAllById(ids).spliterator(), false)
                .map(CategoryDocument::toCategory)
                .toList();
    }

    private String buildSort(String sort){

        if("name".equals(sort)) {
            return sort.concat(".keyword");
        } else {
            return sort;
        }
    }
}
