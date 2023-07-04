package com.helion.admin.catalog.infrastructure.video.persistence;

import com.helion.admin.catalog.domain.genre.GenreID;

import javax.persistence.*;
import java.util.Objects;

@Entity(name= "VideoGenre")
@Table(name="videos_genres")
public class VideoGenreJpaEntity {

    @EmbeddedId
    private VideoGenreID id;

    @ManyToOne(fetch= FetchType.LAZY)
    @MapsId("videoId")
    private VideoJpaEntity video;

    public VideoGenreJpaEntity(){}

    private VideoGenreJpaEntity(VideoGenreID id, VideoJpaEntity video) {
        this.id = id;
        this.video = video;
    }

    public final static VideoGenreJpaEntity from(final VideoJpaEntity video, final GenreID id){
        return new VideoGenreJpaEntity(
                VideoGenreID.from(video.getId(), id.getValue()),
                video
        );
    }

    public VideoGenreID getId() {
        return id;
    }

    public void setId(VideoGenreID id) {
        this.id = id;
    }

    public VideoJpaEntity getVideo() {
        return video;
    }

    public void setVideo(VideoJpaEntity video) {
        this.video = video;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoGenreJpaEntity that = (VideoGenreJpaEntity) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getVideo(), that.getVideo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getVideo());
    }
}
