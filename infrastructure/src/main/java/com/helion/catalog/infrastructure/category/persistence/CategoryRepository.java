package com.helion.catalog.infrastructure.category.persistence;

import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

@Profile("!development")
public interface CategoryRepository extends ElasticsearchRepository<CategoryDocument, String> {
}
