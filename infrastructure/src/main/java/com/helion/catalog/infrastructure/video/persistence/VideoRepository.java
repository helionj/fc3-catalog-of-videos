package com.helion.catalog.infrastructure.video.persistence;

import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

@Profile("!development")
public interface VideoRepository extends ElasticsearchRepository<VideoDocument, String> {
}
