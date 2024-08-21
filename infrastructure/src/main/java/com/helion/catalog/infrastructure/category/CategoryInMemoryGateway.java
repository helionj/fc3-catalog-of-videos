package com.helion.catalog.infrastructure.category;

import com.helion.catalog.domain.category.Category;
import com.helion.catalog.domain.category.CategoryGateway;
import com.helion.catalog.domain.category.CategorySearchQuery;
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
public class CategoryInMemoryGateway implements CategoryGateway {

    Map<String, Category> db;

    public CategoryInMemoryGateway() {
        this.db = new ConcurrentHashMap<>();
    }

    @Override
    public Category save(Category aCategory) {
        this.db.put(aCategory.id(), aCategory);
        return aCategory;
    }

    @Override
    public void deleteById(String categoryId) {
        if(db.containsKey(categoryId)){
            this.db.remove(categoryId);
        }

    }

    @Override
    public Optional<Category> findById(String categoryId) {
        return Optional.ofNullable(this.db.get(categoryId));
    }

    @Override
    public Pagination<Category> findAll(CategorySearchQuery aQuery) {
        return new Pagination<>(
                aQuery.page(),
                aQuery.perPage(),
                this.db.values().size(),
                this.db.values().stream().toList()

        );
    }

    @Override
    public List<Category> findAllById(Set<String> ids) {

        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return ids.stream()
                .map(this.db::get)
                .toList();

    }


}
