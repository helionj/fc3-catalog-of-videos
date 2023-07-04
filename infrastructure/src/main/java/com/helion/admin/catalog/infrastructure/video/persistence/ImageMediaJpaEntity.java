package com.helion.admin.catalog.infrastructure.video.persistence;

import com.helion.admin.catalog.domain.video.ImageMedia;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name="ImageMedia")
@Table(name="videos_image_media")
public class ImageMediaJpaEntity {

    @Id
    private String id;

    @Column(name="checksum", nullable = false)
    private String checksum;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="file_path", nullable = false)
    private String filePath;

    public ImageMediaJpaEntity(){};

    private ImageMediaJpaEntity(String id, String checksum, String name, String filePath) {
        this.id = id;
        this.checksum = checksum;
        this.name = name;
        this.filePath = filePath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public static ImageMediaJpaEntity from(ImageMedia media){
        return new ImageMediaJpaEntity(
                media.id(),
                media.checksum(),
                media.name(),
                media.location()
        );
    }

    public ImageMedia toDomain(){
        return ImageMedia.with(
                getId(),
                getChecksum(),
                getName(),
                getFilePath()
        );
    }
}
