package com.helion.catalog.domain.castmember;

import com.helion.catalog.domain.validation.Error;
import com.helion.catalog.domain.validation.ValidationHandler;

import java.time.Instant;

public class CastMember {

    private final String id;
    private final String name;
    private final CastMemberType type;
    private final Instant createdAt;
    private final Instant updatedAt;


    private CastMember(
            final String anId,
            final String aName,
            final CastMemberType type,
            final Instant aCreatedAt,
            final Instant aUpdatedAt
            ) {
        this.id = anId;
        this.name = aName;
        this.type = type;
        this.createdAt = aCreatedAt;
        this.updatedAt = aUpdatedAt;
    }



    public static CastMember with(
            final String anId,
            final String name,
            final CastMemberType type,
            final Instant createdAt,
            final Instant updatedAt) {
        return new CastMember(
                anId,
                name,
                type,
                createdAt,
                updatedAt);
    }

    public static CastMember with(final CastMember aCastMember){
        return new CastMember(
                aCastMember.id(),
                aCastMember.name(),
                aCastMember.type(),
                aCastMember.createdAt(),
                aCastMember.updatedAt());
    }

    public CastMember validate(final ValidationHandler aHandler){
        if(id == null || id.isBlank()){
            aHandler.append(new Error("'id' should not be empty"));
        }
        if(name == null || name.isBlank()){
            aHandler.append(new Error("'name' should not be empty"));
        }
        if(type == null ){
            aHandler.append(new Error("'type' should not be null"));
        }
        return this;

    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public CastMemberType type() {
        return type;
    }
    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

}