package com.helion.catalog.infrastructure.kafka;

import com.fasterxml.jackson.core.type.TypeReference;
import com.helion.catalog.application.castmember.delete.DeleteCastMemberUseCase;
import com.helion.catalog.application.castmember.save.SaveCastMemberUseCase;
import com.helion.catalog.infrastructure.castmember.models.CastMemberEvent;
import com.helion.catalog.infrastructure.configuration.json.Json;
import com.helion.catalog.infrastructure.kafka.models.connect.MessageValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CastMemberListener {

    private static final Logger LOG = LoggerFactory.getLogger(CastMemberListener.class);
    public static final TypeReference<MessageValue<CastMemberEvent>> CAST_MEMBER_MESSAGE = new TypeReference<>() {
    };
    private final SaveCastMemberUseCase saveCastMemberUseCase;
    private final DeleteCastMemberUseCase deleteCastMemberUseCase;



    public CastMemberListener(final SaveCastMemberUseCase saveCastMemberUseCase,
                              final DeleteCastMemberUseCase deleteCastMemberUseCase
                            ) {
        this.saveCastMemberUseCase = Objects.requireNonNull(saveCastMemberUseCase);
        this.deleteCastMemberUseCase = Objects.requireNonNull(deleteCastMemberUseCase);
    }

    @KafkaListener(
            concurrency = "${kafka.consumers.cast-members.concurrency}",
            containerFactory = "kafkaListenerFactory",
            id = "${kafka.consumers.cast-members.id}",
            topics = "${kafka.consumers.cast-members.topics}",
            groupId = "${kafka.consumers.cast-members.group-id}",
            properties = {
                    "auto.offset.reset=${kafka.consumers.cast-members.auto-offset-reset}"
            }
    )
    @RetryableTopic(
            attempts = "4",
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE
    )
    public void onMessage(@Payload final String payload, final ConsumerRecordMetadata metadata){
        if (payload == null) {
            LOG.info("Message received from Kafka [topic:{}] [partition:{}] [offset:{}]: EMPTY", metadata.topic(), metadata.partition(), metadata.offset());
            return;
        }

        LOG.info("Message received from Kafka [topic:{}], [partition:{}]", metadata.topic(), metadata.partition());
        final var messagePayload =  Json.readValue(payload,CAST_MEMBER_MESSAGE).payload();
        final var op = messagePayload.operation();

        if (op.isDelete()){
            this.deleteCastMemberUseCase.execute(messagePayload.before().id());
        } else {
            this.saveCastMemberUseCase.execute(messagePayload.after().toCastMember());

        }
    }

    @DltHandler
    public void onDLTMessage(@Payload final String payload, final ConsumerRecordMetadata metadata){
        LOG.warn("Message received from Kafka DLT [topic:{}], [partition:{}]", metadata.topic(), metadata.partition());

    }
}
