package com.helion.catalog.infrastructure.category;

import com.helion.catalog.domain.category.Category;

import java.util.Optional;
public interface CategoryClient {
    Optional<Category> categoryOfId(String anId);
}
