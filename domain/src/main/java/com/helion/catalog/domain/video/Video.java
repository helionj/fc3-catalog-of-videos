package com.helion.catalog.domain.video;

import com.helion.catalog.domain.validation.Error;
import com.helion.catalog.domain.validation.ValidationHandler;
import com.helion.catalog.domain.validation.handler.ThrowsValidationHandler;

import java.time.Instant;
import java.time.Year;
import java.util.Set;

public class Video {
    public  String id;
    private String title;
    private String description;
    private Year launchedAt;
    private double duration;
    private Rating rating;
    private boolean opened;
    private boolean published;

    private Instant createdAt;
    private Instant updatedAt;

    private String banner;
    private String thumbNail;
    private String thumbNailHalf;
    private String trailer;
    private String video;

    private Set<String> categories;
    private Set<String> genres;
    private Set<String> castMembers;

    private Video(
            final String id,
            final String title,
            final String description,
            final Integer launchedAt,
            final double duration,
            final String rating,
            final boolean opened,
            final boolean published,
            final String createdAt,
            final String updatedAt,
            final String banner,
            final String thumb,
            final String thumbHalf,
            final String trailer,
            final String video,
            final Set<String> categories,
            final Set<String> genres,
            final Set<String> castMembers) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.launchedAt = launchedAt != null ? Year.of(launchedAt) : null;
        this.duration = duration;
        this.rating = Rating.of(rating).orElse(null);;
        this.opened = opened;
        this.published = published;
        this.createdAt = createdAt != null ? Instant.parse(createdAt) : null;;
        this.updatedAt = updatedAt != null ? Instant.parse(updatedAt) : null;;
        this.banner = banner;
        this.thumbNail = thumb;
        this.thumbNailHalf = thumbHalf;
        this.trailer = trailer;
        this.video = video;
        this.categories = categories != null ? categories : Set.of();
        this.genres = genres != null ? genres : Set.of();
        this.castMembers = castMembers != null ? castMembers : Set.of();

        if(this.video == null || this.video.isBlank()) {
            this.published = false;
        }

        if(this.banner == null || this.banner.isBlank()) {
            this.published = false;
        }

        if(this.trailer == null || this.trailer.isBlank()) {
            this.published = false;
        }
        if(this.thumbNail == null || this.thumbNail.isBlank()) {
            this.published = false;
        }

        if(this.thumbNailHalf == null || this.thumbNailHalf.isBlank()) {
            this.published = false;
        }

        validate(new ThrowsValidationHandler());
    }

    public String id() {
        return id;
    }

    public String title() {
        return title;
    }

    public String description() {
        return description;
    }

    public Year launchedAt() {
        return launchedAt;
    }

    public double duration() {
        return duration;
    }

    public Rating rating() {
        return rating;
    }

    public boolean opened() {
        return opened;
    }

    public boolean published() {
        return published;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    public String banner() {
        return banner;
    }

    public String thumb() {
        return thumbNail;
    }

    public String thumbHalf() {
        return thumbNailHalf;
    }

    public String trailer() {
        return trailer;
    }

    public String video() {
        return video;
    }

    public Set<String> categories() {
        return categories;
    }

    public Set<String> genres() {
        return genres;
    }

    public Set<String> castMembers() {
        return castMembers;
    }

    public static Video with(
            final String id,
            final String title,
            final String description,
            final Integer launchedAt,
            final double duration,
            final String rating,
            final boolean opened,
            final boolean published,
            final String createdAt,
            final String updatedAt,
            final String banner,
            final String thumb,
            final String thumbHalf,
            final String trailer,
            final String video,
            final Set<String> categories,
            final Set<String> genres,
            final Set<String> castMembers
    ){
        return new Video(
                id,
                title,
                description,
                launchedAt,
                duration,
                rating,
                opened,
                published,
                createdAt,
                updatedAt,
                banner,
                thumb,
                thumbHalf,
                trailer,
                video,
                categories,
                genres,
                castMembers);
    }

    public static Video with(Video aVideo){
        return new Video(
                aVideo.id(),
                aVideo.title,
                aVideo.description,
                aVideo.launchedAt().getValue(),
                aVideo.duration,
                aVideo.rating.getName(),
                aVideo.opened,
                aVideo.published,
                aVideo.createdAt.toString(),
                aVideo.updatedAt.toString(),
                aVideo.banner,
                aVideo.thumbNail,
                aVideo.thumbNailHalf,
                aVideo.trailer,
                aVideo.video,
                aVideo.categories,
                aVideo.genres,
                aVideo.castMembers
        );
    }

    public void validate(final ValidationHandler handler){
        if (id == null || id.isBlank()) {
            handler.append( new Error("'id' should not be empty"));
        }

        if (title == null || title.isBlank()) {
            handler.append(new Error("'title' should not be empty"));
        }

        if (launchedAt == null) {
            handler.append(new Error("'launchedAt' should not be empty"));
        }

        if (rating == null) {
            handler.append(new Error("'rating' should not be empty"));
        }

        if (createdAt == null) {
            handler.append(new Error("'createdAt' should not be empty"));
        }

        if (updatedAt == null) {
            handler.append(new Error("'updatedAt' should not be empty"));
        }
    }
}
