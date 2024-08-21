package com.helion.catalog.domain.genre;

import com.helion.catalog.domain.pagination.Pagination;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreGateway {

    Genre save(Genre aGenre);

    void deleteById(String anId);

    Optional<Genre> findById(String anId);


    Pagination<Genre> findAll(GenreSearchQuery aQuery);

    List<Genre> findAllById(Set<String> ids);
}
