package com.helion.admin.catalog.infrastructure.services.local;

import com.helion.admin.catalog.domain.Fixture;
import com.helion.admin.catalog.domain.utils.IdUtils;
import com.helion.admin.catalog.domain.video.VideoMediaType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;


class InMemoryStorageServiceTest {

    private InMemoryStorageService target = new InMemoryStorageService();

    @BeforeEach
    public void setUp(){
        target.reset();
    }
    @Test
    public void givenValidResource_whenCallStore_shouldStoreIt(){

        final var expectedName = IdUtils.uuid();
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.VIDEO);

        target.store(expectedName, expectedResource);

        Assertions.assertEquals(expectedResource, target.storage().get(expectedName));

    }

    @Test
    public void givenValidResource_whenCallGet_shouldRetrieveIt(){

        final var expectedName = IdUtils.uuid();
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.VIDEO);

        target.storage().put(expectedName, expectedResource);
        final var actualResource = target.get(expectedName).get();

        Assertions.assertEquals(expectedResource, actualResource);

    }

    @Test
    public void givenInValidResource_whenCallGet_shouldBeEmpty() {

        final var expectedName = IdUtils.uuid();

        final var actualResource = target.get(expectedName);

        Assertions.assertTrue(actualResource.isEmpty());
    }


    @Test
    public void givenValidPrefix_whenCallList_shouldRetrieveAll(){

        final var expectedNames = List.of(
                "video_"+IdUtils.uuid(),
                "video_"+IdUtils.uuid(),
                "video_"+IdUtils.uuid(),
                "image_"+IdUtils.uuid()
        );

        expectedNames.forEach(name -> target.storage().put(name, Fixture.Videos.resource(VideoMediaType.VIDEO)));

        final var actualResources = target.list("video");

        Assertions.assertTrue(expectedNames.containsAll(actualResources));
        Assertions.assertEquals(actualResources.size(),3 );

    }

    @Test
    public void givenValidNames_whenCallDelete_shouldDeleteAll(){

        final var videos = List.of(
                "video_"+IdUtils.uuid(),
                "video_"+IdUtils.uuid(),
                "video_"+IdUtils.uuid()
                );

        final var expectedNames = List.of(
                "image_"+IdUtils.uuid(),
                "image_"+IdUtils.uuid()
        );

        final var all = new ArrayList<String>(videos);

        all.addAll(expectedNames);


        all.forEach(name -> target.storage().put(name, Fixture.Videos.resource(VideoMediaType.VIDEO)));

        Assertions.assertEquals(5, target.storage().size());
        target.deleteAll(videos);

        Assertions.assertEquals(2, target.storage().size());
        Assertions.assertTrue(expectedNames.containsAll(target.storage().keySet()));

    }

}