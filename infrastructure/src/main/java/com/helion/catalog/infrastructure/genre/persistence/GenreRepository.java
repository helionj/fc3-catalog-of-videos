package com.helion.catalog.infrastructure.genre.persistence;

import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

@Profile("!development")
public interface GenreRepository extends ElasticsearchRepository<GenreDocument, String> {
}
