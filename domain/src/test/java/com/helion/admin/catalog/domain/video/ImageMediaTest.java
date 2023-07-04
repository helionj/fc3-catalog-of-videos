package com.helion.admin.catalog.domain.video;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ImageMediaTest {

    @Test
    public void givenValidParams_whenCallsNewImage_shouldReturnInstance(){
        final var expectedChecksum = "123";
        final var expectedName = "banner.png";
        final var expectedLocation = "/images/banners";

        final var actualImage =
                ImageMedia.with(expectedChecksum,expectedName,expectedLocation);

        Assertions.assertNotNull(actualImage);
        Assertions.assertEquals(expectedChecksum, actualImage.checksum());
        Assertions.assertEquals(expectedName, actualImage.name());
        Assertions.assertEquals(expectedLocation, actualImage.location());
    }

    @Test
    public void givenTwoImagesWithSameCheckSumAndLocation_whenCallsEquals_ShouldReturnsTrue(){
        final var expectedChecksum = "123";
        final var expectedLocation = "/images/banners";

        final var img1 = ImageMedia.with(expectedChecksum, "Random", expectedLocation);
        final var img2 = ImageMedia.with(expectedChecksum, "Simle", expectedLocation);

        Assertions.assertEquals(img1,img2);
        Assertions.assertNotSame(img1,img2);
    }

    @Test
    public void givenInvalidParam_whenCallWith_shouldReturnsError(){

        Assertions.assertThrows(NullPointerException.class,
                () -> ImageMedia.with(null, "Random", "/images"));

        Assertions.assertThrows(NullPointerException.class,
                () -> ImageMedia.with("123", null, "/images"));

        Assertions.assertThrows(NullPointerException.class,
                () -> ImageMedia.with("123", "Random", null));
    }
}
