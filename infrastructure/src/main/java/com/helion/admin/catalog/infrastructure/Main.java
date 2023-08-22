package com.helion.admin.catalog.infrastructure;

import com.helion.admin.catalog.infrastructure.configuration.WebServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.AbstractEnvironment;


@SpringBootApplication
public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        LOG.info("[step:to-be-init] Inicializando o Spring");
        System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "production");
        SpringApplication.run(WebServerConfig.class, args);
        LOG.info("[step:initialized] Spring inicializado");
    }



}