package com.helion.admin.catalog.application.category.create;

import com.helion.admin.catalog.domain.category.Category;
import com.helion.admin.catalog.domain.category.CategoryID;

public record CreateCategoryOutput(String id) {

    public static CreateCategoryOutput from(final String anId) {
        return new CreateCategoryOutput(anId);
    }
    public static CreateCategoryOutput from(final Category aCategory) {
        return new CreateCategoryOutput(aCategory.getId().getValue());
    }
}
