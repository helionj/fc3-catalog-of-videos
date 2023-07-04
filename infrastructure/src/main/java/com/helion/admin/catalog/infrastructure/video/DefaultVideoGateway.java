package com.helion.admin.catalog.infrastructure.video;

import com.helion.admin.catalog.domain.Identifier;
import com.helion.admin.catalog.domain.category.pagination.Pagination;
import com.helion.admin.catalog.domain.video.*;
import com.helion.admin.catalog.infrastructure.utils.SqlUtils;
import com.helion.admin.catalog.infrastructure.video.persistence.VideoJpaEntity;
import com.helion.admin.catalog.infrastructure.video.persistence.VideoRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import static com.helion.admin.catalog.domain.utils.CollectionUtils.mapTo;
import static com.helion.admin.catalog.domain.utils.CollectionUtils.nullIfEmpty;

@Component
public class DefaultVideoGateway implements VideoGateway {

    private final VideoRepository videoRepository;

    public DefaultVideoGateway(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    @Override
    @Transactional
    public Video create(final Video aVideo) {
        return save(aVideo);
    }
    @Override
    public void deleteById(final VideoID anId) {
        final var aVideoId =anId.getValue();
        if (this.videoRepository.existsById(aVideoId)){
            this.videoRepository.deleteById(aVideoId);
        }
    }

    @Override
    @Transactional
    public Video update(Video aVideo) {
        return save(aVideo);
    }

    @Override
    @Transactional
    public Optional<Video> findById(VideoID anId) {
        return this.videoRepository.findById(anId.getValue()).map(VideoJpaEntity::toAggregate);
    }

    @Override
    public Pagination<VideoPreview> findAll(final VideoSearchQuery aQuery) {
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final var actualPage = this.videoRepository.findAll(
                SqlUtils.like(SqlUtils.upper(aQuery.terms())),
                nullIfEmpty(mapTo(aQuery.castMembers(), Identifier::getValue)),
                nullIfEmpty(mapTo(aQuery.categories(), Identifier::getValue)),
                nullIfEmpty(mapTo(aQuery.genres(), Identifier::getValue)),
                page
        );
        return new Pagination<>(
                actualPage.getNumber(),
                actualPage.getSize(),
                actualPage.getTotalElements(),
                actualPage.toList()
        );
    }

    private Video save(Video aVideo) {
        return this.videoRepository.save(VideoJpaEntity.from(aVideo)).toAggregate();
    }


}
