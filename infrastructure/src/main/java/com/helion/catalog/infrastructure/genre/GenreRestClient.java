package com.helion.catalog.infrastructure.genre;

import com.helion.catalog.infrastructure.authentication.GetClientCredentials;
import com.helion.catalog.infrastructure.configuration.annotations.Genres;
import com.helion.catalog.infrastructure.genre.models.GenreDTO;
import com.helion.catalog.infrastructure.kafka.CategoryListener;
import com.helion.catalog.infrastructure.utils.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Objects;
import java.util.Optional;

@Component
@CacheConfig(cacheNames = "admin-genres")
public class GenreRestClient implements HttpClient, GenreClient {

    public final static String NAMESPACE = "genres";

    private final RestClient restClient;


    private final GetClientCredentials getClientCredentials;

    private static final Logger LOG = LoggerFactory.getLogger(CategoryListener.class);

    public GenreRestClient(@Genres final RestClient restClient,
                           final GetClientCredentials getClientCredentials) {
        this.restClient = Objects.requireNonNull(restClient);
        this.getClientCredentials = Objects.requireNonNull(getClientCredentials);
    }

    @Override
    public String nameSpace() {
        return NAMESPACE;
    }

    @Override
    @Cacheable(key="#genreId")
    //@Bulkhead(name= NAMESPACE)
    //@CircuitBreaker(name = NAMESPACE)
    //@Retry(name = NAMESPACE)
    public Optional<GenreDTO> genreOfId(String genreId) {
        final var token = this.getClientCredentials.retrieve();
        var aGenre = doGet(genreId, () ->
                this.restClient.get()
                        .uri("/{id}", genreId)
                        .header(HttpHeaders.AUTHORIZATION, "bearer "+token)
                        .retrieve()
                        .onStatus(isNotFound, notFoundHandler(genreId))
                        .onStatus(is5xx, a5xxHandler(genreId))
                        .body(GenreDTO.class)
        );

        return aGenre;
    }

}
