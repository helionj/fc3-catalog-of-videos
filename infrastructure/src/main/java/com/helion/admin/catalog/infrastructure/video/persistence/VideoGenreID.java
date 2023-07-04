package com.helion.admin.catalog.infrastructure.video.persistence;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class VideoGenreID implements Serializable {

    @Column(name = "video_id", nullable = false)
    private String videoId;

    @Column(name = "genre_id", nullable = false)
    private String genreId;

    public VideoGenreID(){}

    private VideoGenreID(final String videoId, final String genreId) {
        this.videoId = videoId;
        this.genreId = genreId;
    }

    public final static VideoGenreID from(String videoId, String genreId){
        return new VideoGenreID(videoId, genreId);
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getGenreId() {
        return genreId;
    }

    public void setGenreId(String genreId) {
        this.genreId = genreId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoGenreID that = (VideoGenreID) o;
        return Objects.equals(getVideoId(), that.getVideoId()) && Objects.equals(getGenreId(), that.getGenreId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getVideoId(), getGenreId());
    }
}
