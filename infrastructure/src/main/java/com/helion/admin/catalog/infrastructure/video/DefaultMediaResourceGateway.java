package com.helion.admin.catalog.infrastructure.video;

import com.helion.admin.catalog.domain.resource.Resource;
import com.helion.admin.catalog.domain.video.*;
import com.helion.admin.catalog.infrastructure.configuration.properties.storage.StorageProperties;
import com.helion.admin.catalog.infrastructure.services.StorageService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DefaultMediaResourceGateway implements MediaResourceGateway {

    private final String filenamePattern;
    private final String locationPattern;
    private final StorageService storageService;

    public DefaultMediaResourceGateway(
            final StorageProperties props,
            final StorageService storageService) {
        this.filenamePattern = props.getFilenamePattern();
        this.locationPattern = props.getLocationPattern();
        this.storageService = storageService;
    }

    @Override
    public AudioVideoMedia storeAudioVideo(VideoID anId, VideoResource videoResource) {
        final var filePath = filePath(anId, videoResource.getType());
        final var aResource = videoResource.getResource();
        store(filePath, aResource);
        return AudioVideoMedia.with(aResource.checksum(), aResource.name(), filePath);
    }



    @Override
    public ImageMedia storeImage(VideoID anId, VideoResource videoResource) {
        final var filePath = filePath(anId, videoResource.getType());
        final var aResource = videoResource.getResource();
        store(filePath, aResource);
        return ImageMedia.with(aResource.checksum(), aResource.name(), filePath);
    }

    @Override
    public void clearResources(VideoID anId) {
        final var ids = this.storageService.list(folder(anId));
        this.storageService.deleteAll(ids);
    }

    @Override
    public Optional<Resource> getResource(VideoID anId, VideoMediaType type) {
        return this.storageService.get(filePath(anId, type));
    }

    private String fileName(final VideoMediaType aType){
        return filenamePattern.replace("{type}", aType.name());
    }

    private String folder(final VideoID anId){
        return locationPattern.replace("{videoId}", anId.getValue());
    }

    private String filePath(final VideoID anId, final VideoMediaType aType){
        return folder(anId).concat("/").concat(fileName(aType));
    }

    private void store(final String filePath, final Resource aResource) {
        this.storageService.store(filePath, aResource);
    }
}
