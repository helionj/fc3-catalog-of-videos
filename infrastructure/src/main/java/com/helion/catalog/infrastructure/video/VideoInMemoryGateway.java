package com.helion.catalog.infrastructure.video;

import com.helion.catalog.domain.pagination.Pagination;
import com.helion.catalog.domain.video.Video;
import com.helion.catalog.domain.video.VideoGateway;
import com.helion.catalog.domain.video.VideoSearchQuery;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Profile("development")
public class VideoInMemoryGateway implements VideoGateway {

    Map<String, Video> db;

    public VideoInMemoryGateway() {
        this.db = new ConcurrentHashMap<>();
    }

    @Override
    public Video save(Video aVideo) {
        this.db.put(aVideo.id(), aVideo);
        return aVideo;
    }

    @Override
    public void deleteById(String genreId) {
        if(db.containsKey(genreId)){
            this.db.remove(genreId);
        }

    }

    @Override
    public Optional<Video> findById(String videoId) {
        return Optional.ofNullable(this.db.get(videoId));
    }

    @Override
    public Pagination<Video> findAll(VideoSearchQuery aQuery) {
        return new Pagination<>(
                aQuery.page(),
                aQuery.perPage(),
                this.db.values().size(),
                this.db.values().stream().toList()

        );
    }

    @Override
    public List<Video> findAllById(Set<String> ids) {
        if (ids ==null || ids.isEmpty()){
            return List.of();
        }
        return this.db.values().stream().toList();
    }
}
