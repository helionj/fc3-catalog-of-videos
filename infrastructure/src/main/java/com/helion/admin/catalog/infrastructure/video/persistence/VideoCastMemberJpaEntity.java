package com.helion.admin.catalog.infrastructure.video.persistence;

import com.helion.admin.catalog.domain.castmember.CastMemberID;

import javax.persistence.*;
import java.util.Objects;

@Entity(name= "VideoCastMember")
@Table(name="videos_cast_members")
public class VideoCastMemberJpaEntity {

    @EmbeddedId
    private VideoCastMemberID id;

    @ManyToOne(fetch= FetchType.LAZY)
    @MapsId("videoId")
    private VideoJpaEntity video;

    public VideoCastMemberJpaEntity(){};

    private VideoCastMemberJpaEntity(VideoCastMemberID id, VideoJpaEntity video) {
        this.id = id;
        this.video = video;
    }

    public final static VideoCastMemberJpaEntity from(final VideoJpaEntity video, CastMemberID id){
        return new VideoCastMemberJpaEntity(
                VideoCastMemberID.from(video.getId(), id.getValue()),
                video);
    }

    public VideoCastMemberID getId() {
        return id;
    }

    public void setId(VideoCastMemberID id) {
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
        VideoCastMemberJpaEntity that = (VideoCastMemberJpaEntity) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getVideo(), that.getVideo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getVideo());
    }
}
