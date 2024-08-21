package com.helion.catalog.infrastructure.genre;

import com.helion.catalog.domain.genre.Genre;
import com.helion.catalog.domain.genre.GenreGateway;
import com.helion.catalog.domain.genre.GenreSearchQuery;
import com.helion.catalog.domain.pagination.Pagination;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Profile("development")
public class GenreInMemoryGateway implements GenreGateway {

    Map<String, Genre> db;

    public GenreInMemoryGateway() {
        this.db = new ConcurrentHashMap<>();
    }

    @Override
    public Genre save(Genre aGenre) {
        this.db.put(aGenre.id(), aGenre);
        return aGenre;
    }

    @Override
    public void deleteById(String genreId) {
        if(db.containsKey(genreId)){
            this.db.remove(genreId);
        }

    }

    @Override
    public Optional<Genre> findById(String genreId) {
        return Optional.ofNullable(this.db.get(genreId));
    }

    @Override
    public Pagination<Genre> findAll(GenreSearchQuery aQuery) {
        return new Pagination<>(
                aQuery.page(),
                aQuery.perPage(),
                this.db.values().size(),
                this.db.values().stream().toList()

        );
    }

    @Override
    public List<Genre> findAllById(Set<String> ids) {

        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return ids.stream()
                .map(this.db::get)
                .toList();
    }
}
