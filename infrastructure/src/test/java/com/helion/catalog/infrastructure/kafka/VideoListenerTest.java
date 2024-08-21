package com.helion.catalog.infrastructure.kafka;

import com.helion.catalog.AbstractEmbeddedKafkaTest;
import com.helion.catalog.application.genre.save.SaveGenreUseCase;
import com.helion.catalog.application.video.delete.DeleteVideoUseCase;
import com.helion.catalog.application.video.save.SaveVideoUseCase;
import com.helion.catalog.domain.Fixture;
import com.helion.catalog.domain.utils.IdUtils;
import com.helion.catalog.domain.video.Video;
import com.helion.catalog.infrastructure.configuration.json.Json;
import com.helion.catalog.infrastructure.kafka.models.connect.MessageValue;
import com.helion.catalog.infrastructure.kafka.models.connect.Operation;
import com.helion.catalog.infrastructure.kafka.models.connect.ValuePayload;
import com.helion.catalog.infrastructure.video.VideoClient;
import com.helion.catalog.infrastructure.video.models.ImageResourceDTO;
import com.helion.catalog.infrastructure.video.models.VideoDTO;
import com.helion.catalog.infrastructure.video.models.VideoEvent;
import com.helion.catalog.infrastructure.video.models.VideoResourceDTO;
import org.apache.kafka.clients.admin.TopicListing;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class VideoListenerTest extends AbstractEmbeddedKafkaTest {

    @MockBean
    private DeleteVideoUseCase deleteVideoUseCase;

    @MockBean
    private SaveVideoUseCase saveVideoUseCase;

    @MockBean
    private VideoClient videoClient;

    @SpyBean
    private VideoListener videoListener;

    @Value("${kafka.consumers.videos.topics}")
    private String videoTopic;

    @Captor
    private ArgumentCaptor<ConsumerRecordMetadata> metadata;

    @Test
    public void testVideosTopics() throws Exception {
        // given
        final var expectedMainTopic = "adm_videos_mysql.adm_videos.videos";
        final var expectedRetry0Topic = "adm_videos_mysql.adm_videos.videos-retry-0";
        final var expectedRetry1Topic = "adm_videos_mysql.adm_videos.videos-retry-1";
        final var expectedRetry2Topic = "adm_videos_mysql.adm_videos.videos-retry-2";
        final var expectedDLTTopic = "adm_videos_mysql.adm_videos.videos-dlt";

        // when
        final var actualTopics = admin().listTopics().listings().get(10, TimeUnit.SECONDS).stream()
                .map(TopicListing::name)
                .collect(Collectors.toSet());

        // then
        Assertions.assertTrue(actualTopics.contains(expectedMainTopic));
        Assertions.assertTrue(actualTopics.contains(expectedRetry0Topic));
        Assertions.assertTrue(actualTopics.contains(expectedRetry1Topic));
        Assertions.assertTrue(actualTopics.contains(expectedRetry2Topic));
        Assertions.assertTrue(actualTopics.contains(expectedDLTTopic));
    }
    @Test
    public void givenInvalidResponsesFromHandlerShouldRetryItUntilGoesToDlt() throws InterruptedException, ExecutionException, TimeoutException {
        final var expectedMaxAttempts = 4;
        final var expectedMaxDLTAttempts = 1;
        final var expectedMainTopic = "adm_videos_mysql.adm_videos.videos";
        final var expectedRetry0Topic = "adm_videos_mysql.adm_videos.videos-retry-0";
        final var expectedRetry1Topic = "adm_videos_mysql.adm_videos.videos-retry-1";
        final var expectedRetry2Topic = "adm_videos_mysql.adm_videos.videos-retry-2";
        final var expectedDLTTopic = "adm_videos_mysql.adm_videos.videos-dlt";

        final var java21 = Fixture.Videos.java21();
        final var videoEvent = new VideoEvent(java21.id());

        final var message =
                Json.writeValueAsString(new MessageValue<>(new ValuePayload<>(videoEvent, videoEvent, aSource(), Operation.DELETE)));

        final var latch = new CountDownLatch(5);

        doAnswer(t -> {
            latch.countDown();

            throw new RuntimeException("BOOM!");

        }).when(deleteVideoUseCase).execute(any());

        doAnswer(t ->{
            latch.countDown();
            return null;
        }).when(videoListener).onDltMessage(any(), any());

        producer().send(new ProducerRecord<>(videoTopic, message)).get(10, TimeUnit.SECONDS);


        Assertions.assertTrue(latch.await(1, TimeUnit.MINUTES));

        verify(videoListener, times(expectedMaxAttempts)).onMessage(eq(message), metadata.capture());

        final var allMetas =  metadata.getAllValues();

        Assertions.assertEquals(expectedMainTopic, allMetas.get(0).topic());
        Assertions.assertEquals(expectedRetry0Topic, allMetas.get(1).topic());
        Assertions.assertEquals(expectedRetry1Topic, allMetas.get(2).topic());
        Assertions.assertEquals(expectedRetry2Topic, allMetas.get(3).topic());

        //verify(videoListener, times(expectedMaxDLTAttempts)).onDltMessage(eq(message), metadata.capture());

        //Assertions.assertEquals(expectedDLTTopic, metadata.getValue().topic());
    }

    @Test
    public void givenValidVideoWhenUpdateOperationProcessGoesOKThenShouldEndTheOperation() throws Exception {
        // given
        final var java21 = Fixture.Videos.java21();
        final var videoEvent = new VideoEvent(java21.id());

        final var message =
                Json.writeValueAsString(new MessageValue<>(new ValuePayload<>(videoEvent, videoEvent, aSource(), Operation.UPDATE)));

        final var latch = new CountDownLatch(1);

        doAnswer(t -> {
            latch.countDown();
            return new SaveGenreUseCase.Output(java21.id());
        }).when(saveVideoUseCase).execute(any());

        doReturn(Optional.of(videoDTO(java21))).when(videoClient).videoOfId(any());

        // when
        producer().send(new ProducerRecord<>(videoTopic, message)).get(10, TimeUnit.SECONDS);

        Assertions.assertTrue(latch.await(1, TimeUnit.MINUTES));

        // then
        verify(videoClient, times(1)).videoOfId(eq(java21.id()));

        verify(saveVideoUseCase, times(1)).execute(refEq(
                new SaveVideoUseCase.Input(
                        java21.id(),
                        java21.title(),
                        java21.description(),
                        java21.launchedAt().getValue(),
                        java21.duration(),
                        java21.rating().getName(),
                        java21.opened(),
                        java21.published(),
                        java21.createdAt().toString(),
                        java21.updatedAt().toString(),
                        java21.banner(),
                        java21.thumb(),
                        java21.thumbHalf(),
                        java21.trailer(),
                        java21.video(),
                        java21.categories(),
                        java21.genres(),
                        java21.castMembers()

                )
        ));
    }

    @Test
    public void givenCreateOperationWhenProcessGoesOKShouldEndTheOperation() throws Exception {
        // given
        final var java21 = Fixture.Videos.java21();
        final var videoEvent = new VideoEvent(java21.id());

        final var message =
                Json.writeValueAsString(new MessageValue<>(new ValuePayload<>(videoEvent, null, aSource(), Operation.CREATE)));

        final var latch = new CountDownLatch(1);

        doAnswer(t -> {
            latch.countDown();
            return new SaveGenreUseCase.Output(java21.id());
        }).when(saveVideoUseCase).execute(any());

        doReturn(Optional.of(videoDTO(java21))).when(videoClient).videoOfId(any());

        // when
        producer().send(new ProducerRecord<>(videoTopic, message)).get(10, TimeUnit.SECONDS);

        Assertions.assertTrue(latch.await(1, TimeUnit.MINUTES));

        // then
        verify(videoClient, times(1)).videoOfId(eq(java21.id()));

        verify(saveVideoUseCase, times(1)).execute(refEq(
                new SaveVideoUseCase.Input(
                        java21.id(),
                        java21.title(),
                        java21.description(),
                        java21.launchedAt().getValue(),
                        java21.duration(),
                        java21.rating().getName(),
                        java21.opened(),
                        java21.published(),
                        java21.createdAt().toString(),
                        java21.updatedAt().toString(),
                        java21.banner(),
                        java21.thumb(),
                        java21.thumbHalf(),
                        java21.trailer(),
                        java21.video(),
                        java21.categories(),
                        java21.genres(),
                        java21.castMembers()

                )
        ));
    }

    @Test
    public void givenDeleteOperationWhenProcessGoesOKShouldEndTheOperation() throws Exception {
        // given
        final var java21 = Fixture.Videos.java21();
        final var videoEvent = new VideoEvent(java21.id());

        final var message =
                Json.writeValueAsString(new MessageValue<>(new ValuePayload<>(null, videoEvent, aSource(), Operation.DELETE)));

        final var latch = new CountDownLatch(1);

        doAnswer(t -> {
            latch.countDown();
            return null;
        }).when(deleteVideoUseCase).execute(any());

        // when
        producer().send(new ProducerRecord<>(videoTopic, message)).get(10, TimeUnit.SECONDS);

        Assertions.assertTrue(latch.await(1, TimeUnit.MINUTES));

        // then

        verify(deleteVideoUseCase, times(1)).execute(eq(new DeleteVideoUseCase.Input(java21.id())));
    }

    private static VideoDTO videoDTO(Video video) {
        return new VideoDTO(
                video.id(),
                video.title(),
                video.description(),
                video.published(),
                video.launchedAt().getValue(),
                video.rating().getName(),
                video.duration(),
                video.opened(),
                videoResource(video.trailer()),
                imageResource(video.banner()),
                imageResource(video.thumb()),
                imageResource(video.thumbHalf()),
                videoResource(video.video()),
                video.castMembers(),
                video.categories(),
                video.genres(),
                video.createdAt().toString(),
                video.updatedAt().toString()
        );
    }

    private static VideoResourceDTO videoResource(String data) {
        return new VideoResourceDTO(IdUtils.uniqueId(), IdUtils.uniqueId(), data, data, data, "processed");
    }

    private static ImageResourceDTO imageResource(String data) {
        return new ImageResourceDTO(IdUtils.uniqueId(), IdUtils.uniqueId(), data, data);
    }
}
