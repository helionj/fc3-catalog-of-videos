package com.helion.admin.catalog.infrastructure;

import com.helion.admin.catalog.infrastructure.configuration.WebServerConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.AbstractEnvironment;

public class MainTest {

    @Test
    public void testMain(){
        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "test-integration");
        Assertions.assertNotNull(new Main());
        Main.main(new String[]{});

    }
}
