package com.helion.catalog.infrastructure.video;

import com.helion.catalog.infrastructure.authentication.GetClientCredentials;
import com.helion.catalog.infrastructure.configuration.annotations.Videos;
import com.helion.catalog.infrastructure.utils.HttpClient;
import com.helion.catalog.infrastructure.video.models.VideoDTO;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Objects;
import java.util.Optional;

@Component
@CacheConfig(cacheNames = "admin-videos")
public class VideoRestClient implements HttpClient, VideoClient {

    public final static String NAMESPACE = "videos";

    private final RestClient restClient;


    private final GetClientCredentials getClientCredentials;

    public VideoRestClient(@Videos final RestClient restClient, final GetClientCredentials getClientCredentials) {
        this.restClient = Objects.requireNonNull(restClient);
        this.getClientCredentials = Objects.requireNonNull(getClientCredentials);
    }

    @Override
    public String nameSpace() {
        return NAMESPACE;
    }

    @Override
    @Cacheable(key="#videoId")
    @Bulkhead(name= NAMESPACE)
    @CircuitBreaker(name = NAMESPACE)
    @Retry(name = NAMESPACE)
    public Optional<VideoDTO> videoOfId(String videoId) {
        final var token = this.getClientCredentials.retrieve();
        var aVideo = doGet(videoId, () ->
                this.restClient.get()
                        .uri("/{id}", videoId)
                        .header(HttpHeaders.AUTHORIZATION, "bearer "+token)
                        .retrieve()
                        .onStatus(isNotFound, notFoundHandler(videoId))
                        .onStatus(is5xx, a5xxHandler(videoId))
                        .body(VideoDTO.class)
        );
        return aVideo;
    }



}
