package com.helion.admin.catalog.application.genre.create;

import com.helion.admin.catalog.domain.category.CategoryGateway;
import com.helion.admin.catalog.domain.category.CategoryID;
import com.helion.admin.catalog.domain.exceptions.NotificationException;
import com.helion.admin.catalog.domain.genre.Genre;
import com.helion.admin.catalog.domain.genre.GenreGateway;
import com.helion.admin.catalog.domain.validation.Error;
import com.helion.admin.catalog.domain.validation.ValidationHandler;
import com.helion.admin.catalog.domain.validation.handler.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DefaultCreateGenreUseCase extends CreateGenreUseCase{

    private GenreGateway genreGateway;
    private CategoryGateway categoryGateway;

    public DefaultCreateGenreUseCase(
            final GenreGateway genreGateway,
            final CategoryGateway categoryGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public CreateGenreOutput execute(final CreateGenreCommand aCommand) {
        final var aName = aCommand.name();
        final var isActive = aCommand.isActive();
        final var categories = toCategoryID(aCommand.categories());

        final var notification = Notification.create();
        notification.append(validateCategories(categories));
        final var aGenre = notification.validate(() -> Genre.newGenre(aName, isActive));

        if (notification.hasErrors()){
            throw new NotificationException("Could not create Aggregate Genre", notification);
        }
        aGenre.addCategories(categories);
        return  CreateGenreOutput.from(this.genreGateway.create(aGenre));
    }

    private ValidationHandler validateCategories(final List<CategoryID> ids) {
        final var notification = Notification.create();
        if(ids == null || ids.isEmpty()){
            return notification;
        }
        final var retrievedIds = categoryGateway.existsByIds(ids);

        if(ids.size() != retrievedIds.size()){
            final var missingIds = new ArrayList<>(ids);
            missingIds.removeAll(retrievedIds);

            final var missingIdsMessage = missingIds.stream()
                    .map(CategoryID::getValue)
                    .collect(Collectors.joining(", "));

            notification.append(new Error("Some categories could not be found: %s".formatted(missingIdsMessage)));
        }

        return notification;
    }

    private List<CategoryID> toCategoryID(final List<String> categories){
        return categories.stream()
                .map(CategoryID::from)
                .toList();
    }
}
