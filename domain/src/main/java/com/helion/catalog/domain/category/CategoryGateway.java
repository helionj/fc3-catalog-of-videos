package com.helion.catalog.domain.category;

import com.helion.catalog.domain.pagination.Pagination;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CategoryGateway {

    Category save(Category aCategory);

    void deleteById(String anId);

    Optional<Category> findById(String anId);


    Pagination<Category> findAll(CategorySearchQuery aQuery);

    List<Category> findAllById(Set<String> ids);
}