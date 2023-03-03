package com.helion.admin.catalog.application;

import com.helion.admin.catalog.domain.category.Category;

public abstract class UseCase<IN, OUT> {
    public abstract OUT execute(IN anIn);
}