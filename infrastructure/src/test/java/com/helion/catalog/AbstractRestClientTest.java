package com.helion.catalog;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.helion.catalog.infrastructure.category.CategoryRestClient;
import com.helion.catalog.infrastructure.configuration.WebServerConfig;
import com.helion.catalog.infrastructure.genre.GenreRestClient;
import com.helion.catalog.infrastructure.video.VideoRestClient;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@ActiveProfiles("test-integration")
@AutoConfigureWireMock(port = 0)
@EnableAutoConfiguration(exclude = {
        ElasticsearchRepositoriesAutoConfiguration.class,
        KafkaAutoConfiguration.class
})
@SpringBootTest(classes ={WebServerConfig.class, IntegrationTestConfiguration.class})
public abstract class AbstractRestClientTest {

    protected final static String CATEGORY = CategoryRestClient.NAMESPACE;
    protected final static String GENRE = GenreRestClient.NAMESPACE;

    protected final static String VIDEO = VideoRestClient.NAMESPACE;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BulkheadRegistry bulkheadRegistry;

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @Autowired
    private CacheManager cacheManager;


    @BeforeEach
    void before(){
        WireMock.reset();
        WireMock.resetAllRequests();
        List.of(CATEGORY, GENRE, VIDEO).forEach(this::resetFaultTolerance);
        resetAllCaches();
    }

    private void resetAllCaches(){
        cacheManager.getCacheNames().forEach(name -> cacheManager.getCache(name).clear());
    }

    protected Cache cache(final String name){
        return cacheManager.getCache(name);
    }
    protected void acquireBulkheadPermission(final String name) {
        bulkheadRegistry.bulkhead(name).acquirePermission();
    }

    protected void releaseBulkheadPermission(final String name) {
        bulkheadRegistry.bulkhead(name).releasePermission();
    }
    private void resetFaultTolerance(final String name) {

        circuitBreakerRegistry.circuitBreaker(name).reset();
    }

    protected void checkCircuitBreakerState(final String name, final CircuitBreaker.State expectedState ){
        final var cb = circuitBreakerRegistry.circuitBreaker(name);
        Assertions.assertEquals(expectedState, cb.getState());
    }

    protected void transitionToOpenState(final String name){
        circuitBreakerRegistry.circuitBreaker(name).transitionToOpenState();
    }

    protected void transitionToClosedState(final String name){
        circuitBreakerRegistry.circuitBreaker(name).transitionToClosedState();
    }

    protected String writeValueAsString(final Object object){
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
