package com.helion.admin.catalog.infrastructure;

import com.helion.admin.catalog.application.UseCase;
import com.helion.admin.catalog.application.category.create.CreateCategoryUseCase;
import com.helion.admin.catalog.application.category.delete.DeleteCategoryUseCase;
import com.helion.admin.catalog.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.helion.admin.catalog.application.category.retrieve.list.ListCategoriesUseCase;
import com.helion.admin.catalog.application.category.update.UpdateCategoryUseCase;
import com.helion.admin.catalog.domain.category.Category;
import com.helion.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.helion.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import com.helion.admin.catalog.infrastructure.configuration.WebServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.AbstractEnvironment;

import java.util.List;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "development");
        SpringApplication.run(WebServerConfig.class, args);
    }

}