package com.helion.admin.catalog.infrastructure.amqp;

import com.helion.admin.catalog.AmqpTest;
import com.helion.admin.catalog.application.video.media.update.UpdateMediaStatusCommand;
import com.helion.admin.catalog.application.video.media.update.UpdateMediaStatusUseCase;
import com.helion.admin.catalog.domain.utils.IdUtils;
import com.helion.admin.catalog.domain.video.MediaStatus;
import com.helion.admin.catalog.infrastructure.configuration.annotations.VideoEncodedQueue;
import com.helion.admin.catalog.infrastructure.configuration.json.Json;
import com.helion.admin.catalog.infrastructure.configuration.properties.amqp.QueueProperties;
import com.helion.admin.catalog.infrastructure.video.models.VideoEncoderCompleted;
import com.helion.admin.catalog.infrastructure.video.models.VideoEncoderError;
import com.helion.admin.catalog.infrastructure.video.models.VideoMessage;
import com.helion.admin.catalog.infrastructure.video.models.VideoMetadata;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.amqp.rabbit.test.TestRabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@AmqpTest
public class VideoEncoderListenerTest {

    @Autowired
    private TestRabbitTemplate rabbitTemplate;
    @Autowired
    private RabbitListenerTestHarness harness;
    @MockBean
    private UpdateMediaStatusUseCase updateMediaStatusUseCase;

    @Autowired
    @VideoEncodedQueue
    private QueueProperties queueProperties;

    @Test
    public void givenErrorResult_whenCallsListener_shouldProcess() throws InterruptedException {
        final var expectedError = new VideoEncoderError(
                new VideoMessage("123", "abc"),
                "Video not found"
        );

        final var expectedMessage = Json.writeValueAsString(expectedError);

        this.rabbitTemplate.convertAndSend(queueProperties.getQueue(),expectedMessage);

        final var invocationData =
                harness.getNextInvocationDataFor(VideoEncoderListener.LISTENER_ID, 1, TimeUnit.SECONDS);

        Assertions.assertNotNull(invocationData);
        Assertions.assertNotNull(invocationData.getArguments());

        final var actualMessage = invocationData.getArguments()[0];


        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void givenCompletedResult_whenCallsListener_shouldCallUseCase() throws InterruptedException {
        final var expectedId = IdUtils.uuid();
        final var expectedOutputBucket = "codeEducationTest";
        final var expectedStatus = MediaStatus.COMPLETED;
        final var expectedEncoderVideoFolder = "anyfolder";
        final var expectedResourceId = IdUtils.uuid();
        final var expectedFilePath = "any.mp4";
        final var expectedMetadata = new VideoMetadata(expectedEncoderVideoFolder,expectedResourceId, expectedFilePath);

        final var aResult = new VideoEncoderCompleted(
                expectedId,
                expectedOutputBucket,
                expectedMetadata
        );

        final var expectedMessage = Json.writeValueAsString(aResult);

        Mockito.doNothing().when(updateMediaStatusUseCase).execute(any());


        this.rabbitTemplate.convertAndSend(queueProperties.getQueue(),expectedMessage);

        final var invocationData =
                harness.getNextInvocationDataFor(VideoEncoderListener.LISTENER_ID, 1, TimeUnit.SECONDS);

        Assertions.assertNotNull(invocationData);
        Assertions.assertNotNull(invocationData.getArguments());

        final var actualMessage = invocationData.getArguments()[0];


        Assertions.assertEquals(expectedMessage, actualMessage);

        final var cmdCaptor = ArgumentCaptor.forClass(UpdateMediaStatusCommand.class);
        verify(updateMediaStatusUseCase).execute(cmdCaptor.capture());

        final var actualCommand = cmdCaptor.getValue();

        Assertions.assertEquals(expectedId, actualCommand.videoId());
        Assertions.assertEquals(expectedStatus, actualCommand.status());
        Assertions.assertEquals(expectedResourceId, actualCommand.resourceId());
        Assertions.assertEquals(expectedEncoderVideoFolder, actualCommand.folder());
        Assertions.assertEquals(expectedFilePath, actualCommand.filename());

    }




}
