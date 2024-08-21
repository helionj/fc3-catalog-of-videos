package com.helion.catalog.infrastructure.kafka;

import com.helion.catalog.AbstractEmbeddedKafkaTest;
import com.helion.catalog.application.genre.delete.DeleteGenreUseCase;
import com.helion.catalog.application.genre.save.SaveGenreUseCase;
import com.helion.catalog.domain.Fixture;
import com.helion.catalog.infrastructure.configuration.json.Json;
import com.helion.catalog.infrastructure.genre.GenreClient;
import com.helion.catalog.infrastructure.genre.models.GenreDTO;
import com.helion.catalog.infrastructure.genre.models.GenreEvent;
import com.helion.catalog.infrastructure.kafka.models.connect.MessageValue;
import com.helion.catalog.infrastructure.kafka.models.connect.Operation;
import com.helion.catalog.infrastructure.kafka.models.connect.ValuePayload;
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

public class GenreListenerTest extends AbstractEmbeddedKafkaTest {

    @MockBean
    private DeleteGenreUseCase deleteGenreUseCase;

    @MockBean
    private SaveGenreUseCase saveGenreUseCase;

    @MockBean
    private GenreClient genreGateway;

    @SpyBean
    private GenreListener genreListener;

    @Value("${kafka.consumers.genres.topics}")
    private String genreTopic;

    @Captor
    private ArgumentCaptor<ConsumerRecordMetadata> metadata;

    @Test
    public void testGenresTopics() throws Exception {
        // given
        final var expectedMainTopic = "adm_videos_mysql.adm_videos.genres";
        final var expectedRetry0Topic = "adm_videos_mysql.adm_videos.genres-retry-0";
        final var expectedRetry1Topic = "adm_videos_mysql.adm_videos.genres-retry-1";
        final var expectedRetry2Topic = "adm_videos_mysql.adm_videos.genres-retry-2";
        final var expectedDLTTopic = "adm_videos_mysql.adm_videos.genres-dlt";

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
        final var expectedMainTopic = "adm_videos_mysql.adm_videos.genres";
        final var expectedRetry0Topic = "adm_videos_mysql.adm_videos.genres-retry-0";
        final var expectedRetry1Topic = "adm_videos_mysql.adm_videos.genres-retry-1";
        final var expectedRetry2Topic = "adm_videos_mysql.adm_videos.genres-retry-2";
        final var expectedDLTTopic = "adm_videos_mysql.adm_videos.genres-dlt";

        final var tech = Fixture.Genres.tech();
        final var techEvent = new GenreEvent(tech.id());

        final var message =
                Json.writeValueAsString(new MessageValue<>(new ValuePayload<>(techEvent, techEvent, aSource(), Operation.DELETE)));

        final var latch = new CountDownLatch(5);

        doAnswer(t -> {
            latch.countDown();

            throw new RuntimeException("BOOM!");

        }).when(deleteGenreUseCase).execute(any());

        doAnswer(t ->{
            latch.countDown();
            return null;
        }).when(genreListener).onDltMessage(any(), any());

        producer().send(new ProducerRecord<>(genreTopic, message)).get(10, TimeUnit.SECONDS);


        Assertions.assertTrue(latch.await(1, TimeUnit.MINUTES));

        verify(genreListener, times(expectedMaxAttempts)).onMessage(eq(message), metadata.capture());

        final var allMetas =  metadata.getAllValues();

        Assertions.assertEquals(expectedMainTopic, allMetas.get(0).topic());
        Assertions.assertEquals(expectedRetry0Topic, allMetas.get(1).topic());
        Assertions.assertEquals(expectedRetry1Topic, allMetas.get(2).topic());
        Assertions.assertEquals(expectedRetry2Topic, allMetas.get(3).topic());

        verify(genreListener, times(expectedMaxDLTAttempts)).onDltMessage(eq(message), metadata.capture());

        Assertions.assertEquals(expectedDLTTopic, metadata.getValue().topic());
    }

    @Test
    public void givenUpdateOperationWhenProcessGoesOKShouldEndTheOperation() throws Exception {
        // given
        final var tech = Fixture.Genres.tech();
        final var techEvent = new GenreEvent(tech.id());

        final var message =
                Json.writeValueAsString(new MessageValue<>(new ValuePayload<>(techEvent, techEvent, aSource(), Operation.UPDATE)));

        final var latch = new CountDownLatch(1);

        doAnswer(t -> {
            latch.countDown();
            return new SaveGenreUseCase.Output(tech.id());
        }).when(saveGenreUseCase).execute(any());

        doReturn(Optional.of(GenreDTO.from(tech))).when(genreGateway).genreOfId(any());

        // when
        producer().send(new ProducerRecord<>(genreTopic, message)).get(10, TimeUnit.SECONDS);

        Assertions.assertTrue(latch.await(1, TimeUnit.MINUTES));

        // then
        verify(genreGateway, times(1)).genreOfId(eq(tech.id()));

        verify(saveGenreUseCase, times(1)).execute(refEq(
                new SaveGenreUseCase.Input(
                        tech.id(),
                        tech.name(),
                        tech.categories(),
                        tech.active(),
                        tech.createdAt(),
                        tech.updatedAt(),
                        tech.deletedAt()
                )
        ));
    }

    @Test
    public void givenCreateOperationWhenProcessGoesOKShouldEndTheOperation() throws Exception {
        // given
        final var tech = Fixture.Genres.tech();
        final var techEvent = new GenreEvent(tech.id());

        final var message =
                Json.writeValueAsString(new MessageValue<>(new ValuePayload<>(techEvent, null, aSource(), Operation.CREATE)));

        final var latch = new CountDownLatch(1);

        doAnswer(t -> {
            latch.countDown();
            return new SaveGenreUseCase.Output(tech.id());
        }).when(saveGenreUseCase).execute(any());

        doReturn(Optional.of(GenreDTO.from(tech))).when(genreGateway).genreOfId(any());

        // when
        producer().send(new ProducerRecord<>(genreTopic, message)).get(10, TimeUnit.SECONDS);

        Assertions.assertTrue(latch.await(1, TimeUnit.MINUTES));

        // then
        verify(genreGateway, times(1)).genreOfId(eq(tech.id()));

        verify(saveGenreUseCase, times(1)).execute(refEq(
                new SaveGenreUseCase.Input(
                        tech.id(),
                        tech.name(),
                        tech.categories(),
                        tech.active(),
                        tech.createdAt(),
                        tech.updatedAt(),
                        tech.deletedAt()
                )
        ));
    }

    @Test
    public void givenDeleteOperationWhenProcessGoesOKShouldEndTheOperation() throws Exception {
        // given
        final var tech = Fixture.Genres.tech();
        final var techEvent = new GenreEvent(tech.id());

        final var message =
                Json.writeValueAsString(new MessageValue<>(new ValuePayload<>(null, techEvent, aSource(), Operation.DELETE)));

        final var latch = new CountDownLatch(1);

        doAnswer(t -> {
            latch.countDown();
            return null;
        }).when(deleteGenreUseCase).execute(any());

        // when
        producer().send(new ProducerRecord<>(genreTopic, message)).get(10, TimeUnit.SECONDS);

        Assertions.assertTrue(latch.await(1, TimeUnit.MINUTES));

        // then

        verify(deleteGenreUseCase, times(1)).execute(eq(new DeleteGenreUseCase.Input(tech.id())));
    }
}
