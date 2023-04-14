package com.helion.admin.catalog.infrastructure.genre.presenters;

import com.helion.admin.catalog.application.genre.retrieve.get.GenreOutput;
import com.helion.admin.catalog.application.genre.retrieve.list.GenreListOutput;
import com.helion.admin.catalog.infrastructure.genre.models.GenreListResponse;
import com.helion.admin.catalog.infrastructure.genre.models.GenreResponse;

public class GenreApiPresenter {
    public static GenreResponse present(GenreOutput output){
        return new GenreResponse(
                output.id().getValue(),
                output.name(),
                output.categories(),
                output.isActive(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }

    public static GenreListResponse present(GenreListOutput output){
        return new GenreListResponse(
                output.id(),
                output.name(),
                output.isActive(),
                output.createdAt(),
                output.deletedAt()
        );
    }
}
