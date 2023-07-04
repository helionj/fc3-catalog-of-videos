package com.helion.admin.catalog.domain.video;

import com.helion.admin.catalog.domain.ValueObject;
import com.helion.admin.catalog.domain.resource.Resource;

import java.util.Objects;

public class VideoResource extends ValueObject {

    private final Resource resource;
    private final VideoMediaType type;

    public VideoResource(final Resource resource, final VideoMediaType type) {
        this.resource = Objects.requireNonNull(resource);
        this.type = Objects.requireNonNull(type);
    }

    public final static VideoResource with(final Resource aResource, final VideoMediaType aType){
        return new VideoResource(aResource, aType);
    }

    public Resource getResource() {
        return resource;
    }

    public VideoMediaType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoResource that = (VideoResource) o;
        return getType() == that.getType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType());
    }
}
