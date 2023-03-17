package com.helion.admin.catalog.e2e.category;

import com.helion.admin.catalog.E2ETest;
import com.helion.admin.catalog.domain.category.CategoryID;
import com.helion.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@E2ETest
@Testcontainers
public class CategoryE2ETest {


    @Autowired
    private CategoryRepository categoryRepository;

    @Container
    private static final MySQLContainer MYSQL_CONTAINER =
            new MySQLContainer("mysql:latest")
                    .withPassword("123456")
                    .withUsername("root")
                    .withDatabaseName("adm_videos");

    @DynamicPropertySource
    public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
        final var mappedPort = MYSQL_CONTAINER.getMappedPort(3306);
        System.out.printf("Container is running on port: %s\n", mappedPort);
        registry.add("mysql.port", () -> mappedPort);
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToCreateANewCategoryWithValidValues(){
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        Assertions.assertEquals(0, categoryRepository.count());
    }


}
