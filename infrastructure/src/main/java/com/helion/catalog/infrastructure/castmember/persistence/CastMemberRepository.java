package com.helion.catalog.infrastructure.castmember.persistence;

import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

@Profile("!development")
public interface CastMemberRepository extends ElasticsearchRepository<CastMemberDocument, String> {
}
