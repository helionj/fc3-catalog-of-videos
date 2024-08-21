package com.helion.catalog.infrastructure.genre;

import com.helion.catalog.infrastructure.genre.models.GenreDTO;

import java.util.Optional;

public interface GenreClient {

    Optional<GenreDTO> genreOfId(String genreId);

}
