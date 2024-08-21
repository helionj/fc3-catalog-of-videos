package com.helion.catalog.domain.genre;

import com.helion.catalog.domain.validation.Error;
import com.helion.catalog.domain.validation.ValidationHandler;
import com.helion.catalog.domain.validation.handler.ThrowsValidationHandler;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public class Genre {

    private String id;
    private String name;
    private Set<String> categories;
    private boolean isActive;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;


    public Genre(
            final String id,
            final String name,
            final Set<String> categories,
            final boolean isActive,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt
            ) {
        this.id = id;
        this.name = name;
        this.categories = categories != null ? categories : new HashSet<>();
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        validate(new ThrowsValidationHandler());
    }

    public static Genre with(
            final String id,
            final String name,
            final Set<String> categories,
            final boolean isActive,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt

    ){
        return new Genre(id, name, categories, isActive, createdAt, updatedAt, deletedAt);

    }

    public static Genre with(Genre aGenre){
        return new Genre(aGenre.id(), aGenre.name(), aGenre.categories(), aGenre.isActive(), aGenre.createdAt(), aGenre.updatedAt(), aGenre.deletedAt());
    }

    public void validate(ValidationHandler handler) {
        if (id == null || id.isBlank()) {
            handler.append(new Error("'id' should not be empty"));
        }

        if (name == null || name.isBlank()) {
            handler.append(new Error("'name' should not be empty"));
        }
    }

    public String id() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> categories() {
        return categories;
    }

    public void setCategories(Set<String> categories) {
        this.categories = categories;
    }

    public boolean active() {
        return isActive;
    }
    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant deletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }
}
