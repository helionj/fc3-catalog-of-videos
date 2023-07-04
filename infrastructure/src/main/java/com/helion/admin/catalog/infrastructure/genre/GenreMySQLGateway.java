package com.helion.admin.catalog.infrastructure.genre;

import com.helion.admin.catalog.domain.category.CategoryID;
import com.helion.admin.catalog.domain.category.pagination.Pagination;
import com.helion.admin.catalog.domain.category.pagination.SearchQuery;
import com.helion.admin.catalog.domain.genre.Genre;
import com.helion.admin.catalog.domain.genre.GenreGateway;
import com.helion.admin.catalog.domain.genre.GenreID;
import com.helion.admin.catalog.infrastructure.genre.persistence.GenreJpaEntity;
import com.helion.admin.catalog.infrastructure.genre.persistence.GenreRepository;
import com.helion.admin.catalog.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Component
public class GenreMySQLGateway implements GenreGateway {


    private final GenreRepository genreRepository;

    public GenreMySQLGateway(GenreRepository genreRepository) {
        this.genreRepository = Objects.requireNonNull(genreRepository);
    }

    @Override
    public Genre create(final Genre aGenre) {
        return save(aGenre);
    }


    @Override
    public void deleteById(GenreID anId) {
        final var aGenreId = anId.getValue();
        if (this.genreRepository.existsById(aGenreId)) {
            this.genreRepository.deleteById(aGenreId);
        }
    }

    @Override
    public Optional<Genre> findById(GenreID anId) {
        return this.genreRepository.findById(anId.getValue())
                .map(GenreJpaEntity::toAggregate);
    }

    @Override
    public Genre update(Genre aGenre) {
        return save(aGenre);
    }

    @Override
    public Pagination<Genre> findAll(SearchQuery aQuery) {
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final var where = Optional.ofNullable(aQuery.terms())
                .filter(str -> !str.isBlank())
                .map(this::assembleSpecification)
                .orElse(null);

        final var pageResults = this.genreRepository.findAll(Specification.where(where), page);

        return new Pagination<>(
                pageResults.getNumber(),
                pageResults.getSize(),
                pageResults.getTotalElements(),
                pageResults.map(GenreJpaEntity::toAggregate).toList()
        );
    }

    @Override
    public List<GenreID> existsByIds(Iterable<GenreID> genreIDS) {


        final var ids = StreamSupport.stream(genreIDS.spliterator(), false)
                .map(GenreID::getValue)
                .toList();
        return this.genreRepository.existsByIds(ids).stream()
                .map(GenreID::from)
                .toList();
    }

    private Specification<GenreJpaEntity> assembleSpecification(final String terms) {
        return SpecificationUtils.like("name", terms);
    }

    private Genre save(final Genre aGenre) {
        return this.genreRepository.save(GenreJpaEntity.from(aGenre)).toAggregate();
    }
}
