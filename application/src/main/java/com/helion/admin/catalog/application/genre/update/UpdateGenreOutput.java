package com.helion.admin.catalog.application.genre.update;

import com.helion.admin.catalog.domain.genre.Genre;

public record UpdateGenreOutput(String id) {

    public static UpdateGenreOutput from(final Genre aGenre){
        return new UpdateGenreOutput(aGenre.getId().getValue());
    }

    public static UpdateGenreOutput from(final String anId){
        return new UpdateGenreOutput(anId);
    }
}
