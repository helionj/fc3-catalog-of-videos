package com.helion.catalog.infrastructure.configuration.usecases;

import com.helion.catalog.application.category.delete.DeleteCategoryUseCase;
import com.helion.catalog.application.category.get.GetAllCategoriesByIdUseCase;
import com.helion.catalog.application.category.list.ListCategoryUseCase;
import com.helion.catalog.application.category.save.SaveCategoryUseCase;
import com.helion.catalog.domain.category.CategoryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class CategoryUseCasesConfig {


    private final CategoryGateway categoryGateway;

    public CategoryUseCasesConfig(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Bean
    DeleteCategoryUseCase deleteCategoryUseCase(){
        return new DeleteCategoryUseCase(categoryGateway);
    }

    @Bean
    ListCategoryUseCase listCategoryUseCase(){
        return new ListCategoryUseCase(categoryGateway);
    }

    @Bean
    SaveCategoryUseCase saveCategoryUseCase(){
        return new SaveCategoryUseCase(categoryGateway);
    }

    @Bean
    GetAllCategoriesByIdUseCase getAllCategoriesByIdUseCase() { return new GetAllCategoriesByIdUseCase(categoryGateway); }
}
