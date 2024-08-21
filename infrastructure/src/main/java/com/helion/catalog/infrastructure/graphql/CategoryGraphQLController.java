package com.helion.catalog.infrastructure.graphql;

import com.helion.catalog.application.category.list.ListCategoryUseCase;
import com.helion.catalog.application.category.save.SaveCategoryUseCase;
import com.helion.catalog.domain.category.CategorySearchQuery;
import com.helion.catalog.infrastructure.category.GqlCategoryPresenter;
import com.helion.catalog.infrastructure.category.models.GqlCategoryInput;
import com.helion.catalog.infrastructure.category.models.GqlCategory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Objects;

@Controller
public class CategoryGraphQLController {

    private final ListCategoryUseCase listCategoryUseCase;
    private final SaveCategoryUseCase saveCategoryUseCase;

    public CategoryGraphQLController(
            final ListCategoryUseCase listCategoryUseCase,
            final SaveCategoryUseCase saveCategoryUseCase) {
        this.listCategoryUseCase = Objects.requireNonNull(listCategoryUseCase);
        this.saveCategoryUseCase = Objects.requireNonNull(saveCategoryUseCase);
    }
    @QueryMapping
    public List<GqlCategory> categories(
            @Argument final String search,
            @Argument final int page,
            @Argument final int perPage,
            @Argument final String sort,
            @Argument final String direction
    ){
      final var aQuery = new CategorySearchQuery(page, perPage, search, sort, direction);
      return this.listCategoryUseCase.execute(aQuery)
              .map(GqlCategoryPresenter::present)
              .data();
    }

    @MutationMapping
    public GqlCategory saveCategory(@Argument GqlCategoryInput input){
        return GqlCategoryPresenter.present(this.saveCategoryUseCase.execute(input.toCategory()));
    }
}
