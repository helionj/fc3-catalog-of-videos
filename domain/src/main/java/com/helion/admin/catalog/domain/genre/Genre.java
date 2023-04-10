package com.helion.admin.catalog.domain.genre;

import com.helion.admin.catalog.domain.AgregateRoot;
import com.helion.admin.catalog.domain.category.Category;
import com.helion.admin.catalog.domain.category.CategoryID;
import com.helion.admin.catalog.domain.exceptions.NotificationException;
import com.helion.admin.catalog.domain.utils.InstantUtils;
import com.helion.admin.catalog.domain.validation.ValidationHandler;
import com.helion.admin.catalog.domain.validation.handler.Notification;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Genre extends AgregateRoot<GenreID> implements Cloneable {

    private String name;
    private boolean isActive;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private List<CategoryID> categories;


    private Genre(
            final GenreID anId,
            final String aName,
            final boolean isActive,
            final List<CategoryID> categories,
            final Instant aCreatedAt,
            final Instant aUpdatedAt,
            final Instant aDeletedAt) {
        super(anId);
        this.name = aName;
        this.isActive = isActive;
        this.categories = categories;
        this.createdAt = Objects.requireNonNull(aCreatedAt, "'createdAt' not be null");
        this.updatedAt = Objects.requireNonNull(aUpdatedAt, "'updatedAt' not be null");
        this.deletedAt = aDeletedAt;

        selfValidate();
    }



    public static Genre with(
            final GenreID anId,
            final String aName,
            final boolean isActive,
            final List<CategoryID> categories,
            final Instant aCreatedAt,
            final Instant aUpdatedAt,
            final Instant aDeletedAt) {
        return new Genre(anId, aName, isActive, categories, aCreatedAt, aUpdatedAt, aDeletedAt);
    }

    public static Genre with(final Genre aGenre) {
        return new Genre(
                aGenre.id,
                aGenre.name,
                aGenre.isActive,
                new ArrayList<>(aGenre.categories),
                aGenre.createdAt,
                aGenre.updatedAt,
                aGenre.deletedAt
        );
    }

    public static Genre newGenre(String aName, boolean isActive){
        final var id = GenreID.unique();
        final var now = InstantUtils.now();
        final var deletedAt = isActive ? null : now;
        return new Genre(id, aName, isActive, new ArrayList<>(), now, now, deletedAt);
    }

    public Genre update(final String aName, final boolean isActive, final List<CategoryID> categories){

        if(isActive){
            this.activate();
        }else{
            this.deactivate();
        }
        this.name = aName;

        this.categories = new ArrayList<>(categories != null ? categories : Collections.EMPTY_LIST);

        selfValidate();
        return this;
    }

    private void selfValidate() {
        final var notification = Notification.create();
        validate(notification);

        if(notification.hasErrors()){
            throw new NotificationException("Failed to create aggregate Genre", notification);
        }
    }

    @Override
    public void validate(ValidationHandler handler) {
        new GenreValidator(this, handler).validate();
    }

    public String getName() {
        return name;
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

    public List<CategoryID> getCategories() {
        return Collections.unmodifiableList(this.categories);
    }

    public Genre deactivate(){
        if(this.deletedAt == null){
            this.deletedAt = InstantUtils.now();
        }
        this.isActive = false;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Genre activate() {

        this.deletedAt = null;
        this.isActive = true;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    @Override
    public String toString() {
        return "Genre{" +
                "name='" + name + '\'' +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", deletedAt=" + deletedAt +
                ", categories=" + categories +
                '}';
    }

    public Genre addCategory(final CategoryID aCategoryID){
        if(aCategoryID != null){
            this.categories.add(aCategoryID);
            this.updatedAt= InstantUtils.now();
        }

        return this;
    }
    public Genre addCategories(List<CategoryID> categories) {
        if(categories == null || categories.isEmpty()){
            return this;
        }
        this.categories.addAll(categories);
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Genre removeCategory(final CategoryID aCategoryID){
        if(aCategoryID != null){
            this.categories.remove(aCategoryID);
            this.updatedAt= InstantUtils.now();
        }
        return this;
    }

    @Override
    public Genre clone() {
        try {
            return (Genre) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
