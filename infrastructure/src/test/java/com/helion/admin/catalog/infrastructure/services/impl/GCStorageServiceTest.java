package com.helion.admin.catalog.infrastructure.services.impl;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.helion.admin.catalog.domain.Fixture;
import com.helion.admin.catalog.domain.resource.Resource;
import com.helion.admin.catalog.domain.utils.IdUtils;
import com.helion.admin.catalog.domain.video.VideoMediaType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.Mockito.*;

class GCStorageServiceTest {

    private GCStorageService target;

    private Storage storage;

    private String bucket = "fc3-test";

    @BeforeEach
    public void setUp(){
        this.storage = mock(Storage.class);
        this.target = new GCStorageService(this.bucket, this.storage);
    }

    @Test
    public void givenValidResource_whenCallStore_shouldStoreIt(){

        final var expectedName = IdUtils.uuid();
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.VIDEO);

        final var blob = mockBlob(expectedName, expectedResource);

        doReturn(blob).when(storage).create(any(BlobInfo.class), any());

        this.target.store(expectedName, expectedResource);

        final var captor = ArgumentCaptor.forClass(BlobInfo.class);

        verify(storage, times(1)).create(captor.capture(), eq(expectedResource.content()));

        final var actualBlob = captor.getValue();
        Assertions.assertEquals(this.bucket, actualBlob.getBlobId().getBucket());
        Assertions.assertEquals(expectedName, actualBlob.getBlobId().getName());
        Assertions.assertEquals(expectedName, actualBlob.getName());
        Assertions.assertEquals(expectedResource.checksum(), actualBlob.getCrc32cToHexString());
        Assertions.assertEquals(expectedResource.contentType(), actualBlob.getContentType());
    }

    @Test
    public void givenValidResource_whenCallGet_shouldRetrieveIt(){

        final var expectedName = IdUtils.uuid();
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final var blob = mockBlob(expectedName, expectedResource);

        doReturn(blob).when(storage).get(anyString(), anyString());

        final var actualResource = this.target.get(expectedName).get();


        verify(storage, times(1)).get(eq(this.bucket), eq(expectedName));

        Assertions.assertEquals(expectedResource, actualResource);

    }

    @Test
    public void givenInValidResource_whenCallGet_shouldBeEmpty() {

        final var expectedName = IdUtils.uuid();
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final var blob = mockBlob(expectedName, expectedResource);

        doReturn(null).when(storage).get(anyString(), anyString());

        final var actualResource = this.target.get(expectedName);


        verify(storage, times(1)).get(eq(this.bucket), eq(expectedName));

        Assertions.assertTrue(actualResource.isEmpty());
    }


    @Test
    public void givenValidPrefix_whenCallList_shouldRetrieveAll(){
        final var expectedPrefix = "media_";
        final var expectedNameVideo = expectedPrefix+IdUtils.uuid();
        final var expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final var expectedNameBanner = expectedPrefix+IdUtils.uuid();
        final var expectedBanner = Fixture.Videos.resource(VideoMediaType.BANNER);
        final var blobVideo = mockBlob(expectedNameVideo, expectedVideo);

        final var blobBanner = mockBlob(expectedNameBanner, expectedBanner);

        final var expectedResources = List.of(expectedNameVideo, expectedNameBanner);

        final var page = Mockito.mock(Page.class);
        doReturn(List.of(blobVideo, blobBanner)).when(page).iterateAll();
        doReturn(page).when(storage).list(anyString(), any());

        final var actualResources = this.target.list(expectedPrefix);


        verify(storage, times(1)).list(eq(this.bucket), eq(Storage.BlobListOption.prefix(expectedPrefix)));

        Assertions.assertTrue(expectedResources.size() ==actualResources.size() && expectedResources.containsAll(actualResources));

    }

    @Test
    public void givenValidNames_whenCallDelete_shouldDeleteAll(){

        final var expectedPrefix = "media_";
        final var expectedNameVideo = expectedPrefix+IdUtils.uuid();
        final var expectedNameBanner = expectedPrefix+IdUtils.uuid();


        final var expectedResources = List.of(expectedNameVideo, expectedNameBanner);


        this.target.deleteAll(expectedResources);

        final var captor = ArgumentCaptor.forClass(List.class);
        verify(storage, times(1)).delete(captor.capture());
        final var actualResources = ((List<BlobId>) captor.getValue()).stream()
                        .map(BlobId::getName)
                        .toList();
        Assertions.assertTrue(expectedResources.size() ==actualResources.size() && expectedResources.containsAll(actualResources));



    }

    private Blob mockBlob(final String name, final Resource resource){
        final var blob = mock(Blob.class);

        when(blob.getBlobId()).thenReturn(BlobId.of(this.bucket, name));
        when(blob.getCrc32cToHexString()).thenReturn(resource.checksum());
        when(blob.getContent()).thenReturn(resource.content());
        when(blob.getContentType()).thenReturn(resource.contentType());
        when(blob.getName()).thenReturn(resource.name());
        return blob;
    }
}