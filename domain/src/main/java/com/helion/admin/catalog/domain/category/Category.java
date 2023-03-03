package com.helion.admin.catalog.domain.category;

import com.helion.admin.catalog.domain.AgregateRoot;
import com.helion.admin.catalog.domain.validation.ValidationHandler;

import java.time.Instant;
import java.util.Objects;

public class Category extends AgregateRoot<CategoryID> implements Cloneable{

    private String name;
    private String description;
    private boolean isActive;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Category(
            final CategoryID anId,
            final String aName,
            final String aDescription,
            final boolean isActive,
            final Instant aCreatedAt,
            final Instant aUpdatedAt,
            final Instant aDeletedAt) {
        super(anId);
        this.name = aName;
        this.description = aDescription;
        this.isActive = isActive;
        this.createdAt = Objects.requireNonNull(aCreatedAt, "'updatedAt' not be null");
        this.updatedAt = Objects.requireNonNull(aUpdatedAt,  "'createdAt' not be null");
        this.deletedAt = aDeletedAt;
    }

    public static Category newCategory(final String aName, final String aDescription, final boolean isActive){
        final var id = CategoryID.unique();
        final var now = Instant.now();
        final var deletedAt = isActive ? null : now;
        return new Category(id, aName, aDescription,isActive, now, now,deletedAt);
    }

    public static Category with(
            final CategoryID anId,
            final String name,
            final String description,
            final boolean active,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt) {
        return new Category(
                anId,
                name,
                description,
                active,
                createdAt,
                updatedAt,
                deletedAt
        );
    }

    public CategoryID getId() {
        return id;
    }

    @Override
    public void validate(ValidationHandler handler) {
        new CategoryValidator(this, handler).validate();
    }

    public Category deactivate() {
        if(this.deletedAt == null){
            this.deletedAt = Instant.now();
        }
        this.isActive = false;
        this.updatedAt = Instant.now();
        return this;
    }

    public Category activate() {

        this.deletedAt = null;
        this.isActive = true;
        this.updatedAt = Instant.now();
        return this;
    }
    public Category update(String aName, String aDescription, boolean isActive) {
        if(isActive){
            this.activate();
        }else{
            this.deactivate();
        }
        this.name = aName;
        this.description = aDescription;
        this.updatedAt= Instant.now();
        return this;
    }
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return isActive;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    @Override
    public Category clone() {
        try {
            return (Category) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}