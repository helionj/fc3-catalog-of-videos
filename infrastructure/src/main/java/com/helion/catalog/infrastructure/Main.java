package com.helion.catalog.infrastructure;

import com.helion.catalog.infrastructure.configuration.WebServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.AbstractEnvironment;


@SpringBootApplication
public class Main {

    @Value("${rest-client.base-url}")
    public static String uri;
    public static void main(String[] args) {
        System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "sandbox");
        System.out.println(uri);
        SpringApplication.run(WebServerConfig.class, args);
    }



}