package com.helion.catalog.infrastructure.kafka;

import com.fasterxml.jackson.core.type.TypeReference;
import com.helion.catalog.application.video.delete.DeleteVideoUseCase;
import com.helion.catalog.application.video.save.SaveVideoUseCase;
import com.helion.catalog.infrastructure.configuration.json.Json;
import com.helion.catalog.infrastructure.kafka.models.connect.MessageValue;
import com.helion.catalog.infrastructure.kafka.models.connect.Operation;
import com.helion.catalog.infrastructure.video.VideoClient;
import com.helion.catalog.infrastructure.video.models.ImageResourceDTO;
import com.helion.catalog.infrastructure.video.models.VideoDTO;
import com.helion.catalog.infrastructure.video.models.VideoEvent;
import com.helion.catalog.infrastructure.video.models.VideoResourceDTO;
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
public class VideoListener {

    private static final Logger LOG = LoggerFactory.getLogger(VideoListener.class);
    private static final TypeReference<MessageValue<VideoEvent>> VIDEO_MESSAGE_TYPE = new TypeReference<>() {};

    private final SaveVideoUseCase saveVideoUseCase;
    private final DeleteVideoUseCase deleteVideoUseCase;
    private final VideoClient videoClient;

    public VideoListener(
            final SaveVideoUseCase saveVideoUseCase,
            final DeleteVideoUseCase deleteVideoUseCase,
            final VideoClient videoClient) {
        this.saveVideoUseCase = Objects.requireNonNull(saveVideoUseCase);
        this.deleteVideoUseCase = Objects.requireNonNull(deleteVideoUseCase);
        this.videoClient = Objects.requireNonNull(videoClient);
    }

    @KafkaListener(
            concurrency = "${kafka.consumers.videos.concurrency}",
            containerFactory = "kafkaListenerFactory",
            id = "${kafka.consumers.videos.id}",
            topics = "${kafka.consumers.videos.topics}",
            groupId = "${kafka.consumers.videos.group-id}",
            properties = {
                    "auto.offset.reset=${kafka.consumers.videos.auto-offset-reset}"
            }
    )
    @RetryableTopic(
           backoff = @Backoff(delay=1000, multiplier = 2),
            attempts = "4",
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE
    )
    public void onMessage(@Payload final String payload, final ConsumerRecordMetadata metadata){

        if (payload == null) {
            LOG.info("Message received from Kafka [topic:{}] [partition:{}] [offset:{}]: EMPTY", metadata.topic(), metadata.partition(), metadata.offset());
            return;
        }
        LOG.info("Message received from Kafka [topic:{}], [partition:{}]", metadata.topic(), metadata.partition());
        final var messagePayload =  Json.readValue(payload, VIDEO_MESSAGE_TYPE).payload();
        final var op = messagePayload.operation();

        if (Operation.isDelete(op)){
            this.deleteVideoUseCase.execute(new DeleteVideoUseCase.Input(messagePayload.before().id()));
        } else {

            this.videoClient.videoOfId(messagePayload.after().id())
                    .map(it -> toUseCaseInput(it))
                    .ifPresentOrElse(this.saveVideoUseCase::execute, () -> {
                        LOG.warn("Video was not found {}", messagePayload.after().id());
                    });
        }
    }

    private SaveVideoUseCase.Input toUseCaseInput(VideoDTO it) {

        LOG.info("Message payload [published:{}]", it.published());
        return new SaveVideoUseCase.Input(
                it.id(),
                it.title(),
                it.description(),
                it.yearLaunched(),
                it.duration(),
                it.rating(),
                it.opened(),
                it.published(),
                it.createdAt(),
                it.updatedAt(),
                it.getBanner().map(ImageResourceDTO::location).orElse(""),
                it.getThumbnail().map(ImageResourceDTO::location).orElse(""),
                it.getThumbnailHalf().map(ImageResourceDTO::location).orElse(""),
                it.getTrailer().map(VideoResourceDTO::encodedLocation).orElse(""),
                it.getVideo().map(VideoResourceDTO::encodedLocation).orElse(""),
                it.categoriesId(),
                it.genresId(),
                it.castMembersId());
    }

    @DltHandler
    public void onDltMessage(@Payload final String payload, final ConsumerRecordMetadata metadata){
        LOG.warn("Message received from Kafka DLT [topic:{}], [partition:{}]", metadata.topic(), metadata.partition());

    }
}
