package com.helion.catalog.infrastructure.kafka;

import com.fasterxml.jackson.core.type.TypeReference;
import com.helion.catalog.application.category.delete.DeleteCategoryUseCase;
import com.helion.catalog.application.category.save.SaveCategoryUseCase;
import com.helion.catalog.infrastructure.category.CategoryClient;
import com.helion.catalog.infrastructure.category.models.CategoryEvent;
import com.helion.catalog.infrastructure.configuration.json.Json;
import com.helion.catalog.infrastructure.configuration.properties.RestClientProperties;
import com.helion.catalog.infrastructure.kafka.models.connect.MessageValue;
import com.helion.catalog.infrastructure.kafka.models.connect.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CategoryListener {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryListener.class);

    private final SaveCategoryUseCase saveCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;
    private final CategoryClient categoryGateway;

    @Autowired
    private RestClientProperties properties;

    public CategoryListener(
            final SaveCategoryUseCase saveCategoryUseCase,
            final DeleteCategoryUseCase deleteCategoryUseCase,
            final CategoryClient categoryGateway) {
        this.saveCategoryUseCase = Objects.requireNonNull(saveCategoryUseCase);
        this.deleteCategoryUseCase = Objects.requireNonNull(deleteCategoryUseCase);
        this.categoryGateway = Objects.requireNonNull(categoryGateway);

    }

    @KafkaListener(
            concurrency = "${kafka.consumers.categories.concurrency}",
            containerFactory = "kafkaListenerFactory",
            id = "${kafka.consumers.categories.id}",
            topics = "${kafka.consumers.categories.topics}",
            groupId = "${kafka.consumers.categories.group-id}",
            properties = {
                    "auto.offset.reset=${kafka.consumers.categories.auto-offset-reset}"
            }
    )
    @RetryableTopic(
           backoff = @Backoff(delay=1000, multiplier = 2),
            attempts = "4",
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE
    )
    public void onMessage(@Payload(required = false) final String payload, final ConsumerRecordMetadata metadata){

        LOG.info("onMessage - Base URL:"+ properties.baseUrl());

        if (payload == null) {
            LOG.info("Message received from Kafka [topic:{}] [partition:{}] [offset:{}]: EMPTY", metadata.topic(), metadata.partition(), metadata.offset());
            return;
        }
        LOG.info("Message received from Kafka [topic:{}], [partition:{}]", metadata.topic(), metadata.partition());
        final var messagePayload =  Json.readValue(payload, new TypeReference<MessageValue<CategoryEvent>>(){}).payload();
        final var op = messagePayload.operation();

        if (Operation.isDelete(op)){
            this.deleteCategoryUseCase.execute(messagePayload.before().id());
        } else {

            this.categoryGateway.categoryOfId(messagePayload.after().id())
                    .ifPresentOrElse(this.saveCategoryUseCase::execute, () -> {
                        LOG.warn("Category was not found {}", messagePayload.after().id());
                    });
        }
    }

    @DltHandler
    public void onDltMessage(@Payload(required = false) final String payload, final ConsumerRecordMetadata metadata){
        LOG.info("onDltMessage - Base URL:"+ properties.baseUrl());
        if (payload == null) {
            LOG.info("Message received from Kafka [topic:{}] [partition:{}] [offset:{}]: EMPTY", metadata.topic(), metadata.partition(), metadata.offset());
            return;
        }
        LOG.warn("Message received from Kafka DLT [topic:{}], [partition:{}]", metadata.topic(), metadata.partition());
        final var messagePayload =  Json.readValue(payload, new TypeReference<MessageValue<CategoryEvent>>(){}).payload();
        final var op = messagePayload.operation();

        if (Operation.isDelete(op)){
            this.deleteCategoryUseCase.execute(messagePayload.before().id());
        } else {
            this.categoryGateway.categoryOfId(messagePayload.after().id())
                    .ifPresentOrElse(this.saveCategoryUseCase::execute, () -> {
                        LOG.warn("Category was not found {}", messagePayload.after().id());
                    });
        }
    }
}
