package com.helion.catalog.infrastructure.kafka;

import com.fasterxml.jackson.core.type.TypeReference;
import com.helion.catalog.application.genre.delete.DeleteGenreUseCase;
import com.helion.catalog.application.genre.save.SaveGenreUseCase;
import com.helion.catalog.infrastructure.configuration.json.Json;
import com.helion.catalog.infrastructure.genre.GenreClient;
import com.helion.catalog.infrastructure.genre.models.GenreEvent;
import com.helion.catalog.infrastructure.kafka.models.connect.MessageValue;
import com.helion.catalog.infrastructure.kafka.models.connect.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class GenreListener {

    private static final Logger LOG = LoggerFactory.getLogger(GenreListener.class);
    private static final TypeReference<MessageValue<GenreEvent>> GENRE_MESSAGE_TYPE = new TypeReference<>() {};

    private final SaveGenreUseCase saveGenreUseCase;
    private final DeleteGenreUseCase deleteGenreUseCase;
    private final GenreClient genreGateway;

    public GenreListener(
            final SaveGenreUseCase saveGenreUseCase,
            final DeleteGenreUseCase deleteGenreUseCase,
            final GenreClient genreGateway) {
        this.saveGenreUseCase = Objects.requireNonNull(saveGenreUseCase);
        this.deleteGenreUseCase = Objects.requireNonNull(deleteGenreUseCase);
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @KafkaListener(
            concurrency = "${kafka.consumers.genres.concurrency}",
            containerFactory = "kafkaListenerFactory",
            id = "${kafka.consumers.genres.id}",
            topics = "${kafka.consumers.genres.topics}",
            groupId = "${kafka.consumers.genres.group-id}",
            properties = {
                    "auto.offset.reset=${kafka.consumers.genres.auto-offset-reset}"
            }
    )
    @RetryableTopic(
           backoff = @Backoff(delay=1000, multiplier = 2),
            attempts = "${kafka.consumers.genres.max-attempts}",
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE
    )
    public void onMessage(@Payload final String payload, final ConsumerRecordMetadata metadata){

        if (payload == null) {
            LOG.info("Message received from Kafka [topic:{}] [partition:{}] [offset:{}]: EMPTY", metadata.topic(), metadata.partition(), metadata.offset());
            return;
        }

        LOG.info("Message received from Kafka [topic:{}] [partition:{}] [offset:{}]: {}", metadata.topic(), metadata.partition(), metadata.offset(), payload);
        final var messagePayload =  Json.readValue(payload, GENRE_MESSAGE_TYPE).payload();
        final var op = messagePayload.operation();

        if (Operation.isDelete(op)){
            this.deleteGenreUseCase.execute(new DeleteGenreUseCase.Input(messagePayload.before().id()));
        } else {
            this.genreGateway.genreOfId(messagePayload.after().id())
                    .map(it -> new SaveGenreUseCase.Input(it.id(), it.name(), it.categoriesId(), it.isActive(), it.createdAt(), it.updatedAt(), it.deletedAt()))
                    .ifPresentOrElse(this.saveGenreUseCase::execute, () -> {
                        LOG.warn("Genre was not found {}", messagePayload.after().id());
                    });
        }
    }

    @DltHandler
    public void onDltMessage(@Payload final String payload, final ConsumerRecordMetadata metadata){
        LOG.warn("Message received from Kafka DLT [topic:{}], [partition:{}]", metadata.topic(), metadata.partition());

    }
}
