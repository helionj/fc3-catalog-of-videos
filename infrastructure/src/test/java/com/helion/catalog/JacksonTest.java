package com.helion.catalog;

import com.helion.catalog.infrastructure.configuration.WebServerConfig;
import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test-integration")
@JsonTest
@Tag("integrationTest")
public @interface JacksonTest {

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    @ActiveProfiles("test-integration")
    @SpringBootTest(classes = WebServerConfig.class)
    @Tag("integrationTest")
    @interface AmqpTest {
    }
}
