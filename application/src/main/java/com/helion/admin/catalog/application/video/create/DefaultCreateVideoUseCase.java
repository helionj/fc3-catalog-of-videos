package com.helion.admin.catalog.application.video.create;

import com.helion.admin.catalog.domain.Identifier;
import com.helion.admin.catalog.domain.castmember.CastMemberGateway;
import com.helion.admin.catalog.domain.castmember.CastMemberID;
import com.helion.admin.catalog.domain.category.CategoryGateway;
import com.helion.admin.catalog.domain.category.CategoryID;
import com.helion.admin.catalog.domain.exceptions.DomainException;
import com.helion.admin.catalog.domain.exceptions.InternalErrorException;
import com.helion.admin.catalog.domain.exceptions.NotificationException;
import com.helion.admin.catalog.domain.genre.GenreGateway;
import com.helion.admin.catalog.domain.genre.GenreID;
import com.helion.admin.catalog.domain.validation.Error;
import com.helion.admin.catalog.domain.validation.ValidationHandler;
import com.helion.admin.catalog.domain.validation.handler.Notification;
import com.helion.admin.catalog.domain.video.*;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DefaultCreateVideoUseCase extends CreateVideoUseCase{

    final private CategoryGateway categoryGateway;
    final private GenreGateway genreGateway;
    final private CastMemberGateway castMemberGateway;
    final private VideoGateway videoGateway;

    final private MediaResourceGateway mediaResourceGateway;

    public DefaultCreateVideoUseCase(
            final CategoryGateway categoryGateway,
            final GenreGateway genreGateway,
            final CastMemberGateway castMemberGateway,
            final VideoGateway videoGateway,
            final MediaResourceGateway mediaResourceGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
        this.videoGateway = Objects.requireNonNull(videoGateway);
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
    }

    @Override
    public CreateVideoOutput execute(final CreateVideoCommand aCommand) {
        final var aRating = Rating.of(aCommand.rating()).orElse(null);
        final var aLaunchYear = aCommand.launchedAt() != null ? Year.of(aCommand.launchedAt()) : null;
        final var categories = toIdentifier(aCommand.categories(), CategoryID::from);
        final var genres = toIdentifier(aCommand.genres(), GenreID::from);
        final var members = toIdentifier(aCommand.castMembers(), CastMemberID::from);

        final var notification = Notification.create();
        notification.append(validateCategories(categories));
        notification.append(validateGenres(genres));
        notification.append(validateMembers(members));

        final var aVideo= Video.newVideo(
                aCommand.title(),
                aCommand.description(),
                aLaunchYear,
                aCommand.duration(),
                aCommand.opened(),
                aCommand.published(),
                aRating,
                categories,
                genres,
                members
        );

        aVideo.validate(notification);

        if(notification.hasErrors()) {
            throw new NotificationException("Could not create Aggregate Video", notification);
        }
        return CreateVideoOutput.from(create(aCommand, aVideo));
    }

    private Video create(CreateVideoCommand aCommand, Video aVideo) {

        final var anId = aVideo.getId();
        try{
            final var aVideoMedia = aCommand.getVideo()
                    .map(it -> this.mediaResourceGateway.storeAudioVideo(anId, VideoResource.with(it, VideoMediaType.VIDEO)))
                    .orElse(null);

            final var aTrailerMedia = aCommand.getTrailer()
                    .map(it -> this.mediaResourceGateway.storeAudioVideo(anId,VideoResource.with(it, VideoMediaType.TRAILER) ))
                    .orElse(null);

            final var aBannerMedia = aCommand.getBanner()
                    .map(it -> this.mediaResourceGateway.storeImage(anId, VideoResource.with(it, VideoMediaType.BANNER)))
                    .orElse(null);

            final var aThumbnailMedia = aCommand.getThumbnail()
                    .map(it -> this.mediaResourceGateway.storeImage(anId, VideoResource.with(it, VideoMediaType.THUMBNAIL)))
                    .orElse(null);

            final var aThumbHalfMedia = aCommand.getThumbnailHalf()
                    .map(it -> this.mediaResourceGateway.storeImage(anId, VideoResource.with(it, VideoMediaType.THUMBNAIL_HALF)))
                    .orElse(null);


            return this.videoGateway.create(
                    aVideo
                            .setVideo(aVideoMedia)
                            .setTrailer(aTrailerMedia)
                            .setBanner(aBannerMedia)
                            .setThumbnail(aThumbnailMedia)
                            .setThumbnailHalf(aThumbHalfMedia)
            );
        } catch (final Throwable t){
            this.mediaResourceGateway.clearResources(anId);
            throw InternalErrorException.with("An error on create video was observed [videoId: %s]".formatted(anId.getValue()),t);
        }

    }

    private Supplier<DomainException> invalidRating(final String rating){
        return () -> DomainException.with(new Error("Rating not found %s".formatted(rating)));
    }

    private <T> Set<T> toIdentifier(final Set<String> ids, Function<String, T> mapper){
        return ids.stream().map(mapper).collect(Collectors.toSet());
    }

    private ValidationHandler validateCategories(final Set<CategoryID> ids){
        return validateAggregate("categories", ids, categoryGateway::existsByIds);
    }
    private ValidationHandler validateGenres(final Set<GenreID> ids){
        return validateAggregate("genres", ids, genreGateway::existsByIds);
    }

    private ValidationHandler validateMembers(final Set<CastMemberID> ids){
        return validateAggregate("cast members", ids, castMemberGateway::existsByIds);
    }

    private <T extends Identifier> ValidationHandler validateAggregate(
            final String aggregate,
            final Set<T> ids,
            final Function<Iterable<T>, List<T>> existsByIds
    ){
        final var notification = Notification.create();
        if(ids == null || ids.isEmpty()){
            return notification;
        }
        final var retrievedIds = existsByIds.apply(ids);

        if(ids.size() != retrievedIds.size()){
            final var missingIds = new ArrayList<>(ids);
            missingIds.removeAll(retrievedIds);

            final var missingIdsMessage = missingIds.stream()
                    .map(Identifier::getValue)
                    .collect(Collectors.joining(", "));

            notification.append(new Error("Some %s could not be found: %s".formatted(aggregate, missingIdsMessage)));
        }

        return notification;
    }


}
