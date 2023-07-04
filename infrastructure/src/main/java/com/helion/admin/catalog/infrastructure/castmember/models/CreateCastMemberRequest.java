package com.helion.admin.catalog.infrastructure.castmember.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.helion.admin.catalog.domain.castmember.CastMemberType;

public record CreateCastMemberRequest(
        @JsonProperty("name") String name,
        @JsonProperty("type") CastMemberType type) {}
