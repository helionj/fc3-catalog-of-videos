package com.helion.admin.catalog.infrastructure.video.models;

import com.helion.admin.catalog.domain.utils.IdUtils;
import com.helion.admin.catalog.infrastructure.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

@JacksonTest
public class VideoEncoderResultTest {

    @Autowired
    private JacksonTester<VideoEncoderResult> json;

    @Test
    public void testUnMarshallSuccessResult() throws IOException {

        final var expectedId = IdUtils.uuid();
        final var expectedOutputBucket = "codeEducationTest";
        final var expectedStatus = "COMPLETED";
        final var expectedEncoderVideoFolder = "anyfolder";
        final var expectedResourceId = IdUtils.uuid();
        final var expectedFilePath = "any.mp4";
        final var expectedMetadata = new VideoMetadata(expectedEncoderVideoFolder,expectedResourceId, expectedFilePath);

        final var json = """
                {
                    "id": "%s",
                    "output_bucket_path": "%s",
                    "status": "%s",
                    "video": {
                        "encoded_video_folder": "%s",
                        "resource_id": "%s",
                        "file_path": "%s"
                    }
                }
                """.formatted(
                        expectedId,
                        expectedOutputBucket,
                        expectedStatus,
                        expectedEncoderVideoFolder,
                        expectedResourceId,
                        expectedFilePath);
        final var actualResult = this.json.parse(json);

        Assertions.assertThat(actualResult).isInstanceOf(VideoEncoderCompleted.class)
                .hasFieldOrPropertyWithValue("id", expectedId)
                .hasFieldOrPropertyWithValue("outputBucket", expectedOutputBucket)
                .hasFieldOrPropertyWithValue("status", expectedStatus)
                .hasFieldOrPropertyWithValue("video", expectedMetadata);
    }

    @Test
    public void testUnMarshallErrorResult() throws IOException {

        final var expectedMessage = "Resource not found";
        final var expectedStatus = "ERROR";
        final var expectedResourceId = IdUtils.uuid();
        final var expectedFilePath = "any.mp4";
        final var expectedVideoMessage = new VideoMessage(expectedResourceId, expectedFilePath);

        final var json = """
                {
                   
                    "status": "%s",
                    "error": "%s",
                    "message": {
                        "resource_id": "%s",
                        "file_path": "%s"
                    }
                }
                """.formatted(expectedStatus, expectedMessage, expectedResourceId, expectedFilePath);
        final var actualResult = this.json.parse(json);

        Assertions.assertThat(actualResult).isInstanceOf(VideoEncoderError.class)
                .hasFieldOrPropertyWithValue("status", expectedStatus)
                .hasFieldOrPropertyWithValue("error", expectedMessage)
                .hasFieldOrPropertyWithValue("message", expectedVideoMessage);
    }

    @Test
    public void testMarshallSuccessResult() throws IOException {

        final var expectedId = IdUtils.uuid();
        final var expectedOutputBucket = "codeEducationTest";
        final var expectedStatus = "COMPLETED";
        final var expectedEncoderVideoFolder = "anyfolder";
        final var expectedResourceId = IdUtils.uuid();
        final var expectedFilePath = "any.mp4";
        final var expectedMetadata = new VideoMetadata(expectedEncoderVideoFolder,expectedResourceId, expectedFilePath);


        final var aResult = new VideoEncoderCompleted(expectedId,expectedOutputBucket, expectedMetadata);
        final var actualResult = this.json.write(aResult);

        Assertions.assertThat(actualResult)
                .hasJsonPathValue("$.id", expectedId)
                .hasJsonPathValue("$.output_bucket_path", expectedOutputBucket)
                .hasJsonPathValue("$.status", expectedStatus)
                .hasJsonPathValue("$.video.encoded_video_folder", expectedEncoderVideoFolder)
                .hasJsonPathValue("$.video.resource_id", expectedResourceId)
                .hasJsonPathValue("$.video.file_path", expectedFilePath);
    }

    @Test
    public void testMarshallErrorResult() throws IOException {

        final var expectedMessage = "Resource not found";
        final var expectedStatus = "ERROR";
        final var expectedResourceId = IdUtils.uuid();
        final var expectedFilePath = "any.mp4";
        final var expectedVideoMessage = new VideoMessage(expectedResourceId, expectedFilePath);

        final var aResult = new VideoEncoderError(expectedVideoMessage, expectedMessage);

        final var actualResult = this.json.write(aResult);


        Assertions.assertThat(actualResult)
                .hasJsonPathValue("$.status", expectedStatus)
                .hasJsonPathValue("$.error", expectedMessage)
                .hasJsonPathValue("$.message.resource_id", expectedResourceId)
                .hasJsonPathValue("$.message.file_path", expectedResourceId);
    }
}
