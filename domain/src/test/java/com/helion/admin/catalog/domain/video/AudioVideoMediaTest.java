package com.helion.admin.catalog.domain.video;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AudioVideoMediaTest {

    @Test
    public void givenValidParams_whenCallsNewImage_shouldReturnInstance(){
        final var expectedChecksum = "123";
        final var expectedName = "image.png";
        final var expectedRawLocation = "/images/banners";
        final var expectedEncodedLocation = "/images/encoded";
        final var expectedStatus = MediaStatus.COMPLETED;

        final var actualImage =
                AudioVideoMedia.with(expectedChecksum,expectedName,expectedRawLocation,expectedEncodedLocation,expectedStatus);

        Assertions.assertNotNull(actualImage);
        Assertions.assertEquals(expectedChecksum, actualImage.checksum());
        Assertions.assertEquals(expectedName, actualImage.name());
        Assertions.assertEquals(expectedRawLocation, actualImage.rawLocation());
        Assertions.assertEquals(expectedEncodedLocation, actualImage.encodedLocation());
        Assertions.assertEquals(expectedStatus, actualImage.status());
    }

    @Test
    public void givenTwoVideosWithSameCheckSumAndLocation_whenCallsEquals_ShouldReturnsTrue(){
        final var expectedChecksum = "123";
        final var expectedRawLocation = "/images/banners";
        final var expectedEncodedLocation = "/images/encoded";
        final var expectedStatus = MediaStatus.COMPLETED;


        final var img1 = AudioVideoMedia.with(expectedChecksum, "Random", expectedRawLocation, expectedEncodedLocation, expectedStatus);
        final var img2 = AudioVideoMedia.with(expectedChecksum, "Simle", expectedRawLocation, expectedEncodedLocation, expectedStatus);

        Assertions.assertEquals(img1,img2);
        Assertions.assertNotSame(img1,img2);
    }

    @Test
    public void givenInvalidParam_whenCallWith_shouldReturnsError(){

        Assertions.assertThrows(NullPointerException.class,
                () -> AudioVideoMedia.with(null, "Random", "/images", "/images/encoded", MediaStatus.COMPLETED));

        Assertions.assertThrows(NullPointerException.class,
                () -> AudioVideoMedia.with("123", null, "/images", "/images/encoded", MediaStatus.COMPLETED));

        Assertions.assertThrows(NullPointerException.class,
                () -> AudioVideoMedia.with("123", "Random", null, "/images/encoded", MediaStatus.COMPLETED));

        Assertions.assertThrows(NullPointerException.class,
                () -> AudioVideoMedia.with("123", "Random", "/images", null, MediaStatus.COMPLETED));

        Assertions.assertThrows(NullPointerException.class,
                () -> AudioVideoMedia.with("123", "Random", "/images", "/images/encoded", null));
    }
}
