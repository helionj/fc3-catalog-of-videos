package com.helion.admin.catalog.infrastructure.configuration.usecase;

import com.helion.admin.catalog.application.genre.create.CreateGenreUseCase;
import com.helion.admin.catalog.application.genre.create.DefaultCreateGenreUseCase;
import com.helion.admin.catalog.application.genre.delete.DefaultDeleteGenreUseCase;
import com.helion.admin.catalog.application.genre.delete.DeleteGenreUseCase;
import com.helion.admin.catalog.application.genre.retrieve.get.DefaultGetGenreByIdUseCase;
import com.helion.admin.catalog.application.genre.retrieve.get.GetGenreByIdUseCase;
import com.helion.admin.catalog.application.genre.retrieve.list.DefaultListGenreUseCase;
import com.helion.admin.catalog.application.genre.retrieve.list.ListGenreUseCase;
import com.helion.admin.catalog.application.genre.update.DefaultUpdateGenreUseCase;
import com.helion.admin.catalog.application.genre.update.UpdateGenreUseCase;
import com.helion.admin.catalog.domain.category.CategoryGateway;
import com.helion.admin.catalog.domain.genre.GenreGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class GenreUseCaseConfig {

    private final CategoryGateway categoryGateway;
    private final GenreGateway genreGateway;

    public GenreUseCaseConfig(final CategoryGateway categoryGateway,
                              final GenreGateway genreGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Bean
    CreateGenreUseCase createGenreUseCase(){
        return new DefaultCreateGenreUseCase(genreGateway, categoryGateway);
    }

    @Bean
    DeleteGenreUseCase deleteGenreUseCase(){
        return new DefaultDeleteGenreUseCase(genreGateway);
    }

    @Bean
    GetGenreByIdUseCase getGenreByIdUseCase(){
        return  new DefaultGetGenreByIdUseCase(genreGateway);
    }

    @Bean
    ListGenreUseCase listGenreUseCase(){
        return new DefaultListGenreUseCase(genreGateway);
    }

    @Bean
    UpdateGenreUseCase updateGenreUseCase(){
        return new DefaultUpdateGenreUseCase(categoryGateway, genreGateway);
    }
}
