package com.helion.catalog.infrastructure.configuration.usecases;

import com.helion.catalog.application.genre.delete.DeleteGenreUseCase;
import com.helion.catalog.application.genre.get.GetAllGenresByIdUseCase;
import com.helion.catalog.application.genre.list.ListGenreUseCase;
import com.helion.catalog.application.genre.save.SaveGenreUseCase;
import com.helion.catalog.domain.genre.GenreGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration(proxyBeanMethods = false)
public class GenreUseCasesConfig {

    private final GenreGateway genreGateway;

    public GenreUseCasesConfig(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Bean
    DeleteGenreUseCase deleteGenreUseCase(){
        return new DeleteGenreUseCase(genreGateway);
    }

    @Bean
    ListGenreUseCase listGenreUseCase(){
        return new ListGenreUseCase(genreGateway);
    }

    @Bean
    SaveGenreUseCase saveGenreUseCase(){
        return new SaveGenreUseCase(genreGateway);
    }

    @Bean
    GetAllGenresByIdUseCase getAllGenresByIdUseCase() { return new GetAllGenresByIdUseCase(genreGateway); }
}
