package com.helion.catalog.domain.video;

import com.helion.catalog.domain.UnitTest;
import com.helion.catalog.domain.exceptions.DomainException;
import com.helion.catalog.domain.utils.IdUtils;
import com.helion.catalog.domain.utils.InstantUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class VideoTest extends UnitTest {

    @Test
    public void givenValidParams_whenCallVideoWithShouldInstantiate(){
        final var expectedId = IdUtils.uniqueId();
        final var expectedTitle = "System Design Interview";
        final var expectedDescription = """
            Lets design the high-level architecture of youtube - similar to 
            how we'd tackle this in a system design interview.
            """;
        final var expectedLaunchedAt = 2022;
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = "L";
        final var expectedCreatedAt = InstantUtils.now().toString();
        final var expectedUpdatedAt = InstantUtils.now().toString();
        final var expectedCategories = Set.of(IdUtils.uniqueId());
        final var expectedGenres = Set.of(IdUtils.uniqueId());
        final var expectedMembers = Set.of(IdUtils.uniqueId());
        final var expectedVideo= "http://video";
        final var expectedTrailer = "http://trailer";
        final var expectedBanner = "http://banner";
        final var expectedThumb = "http://thumbnaill";
        final var expectedThumbHalf =  "http://thumbnaill-half";



        final var actualVideo = Video.with(
                expectedId,
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCreatedAt,
                expectedUpdatedAt,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf,
                expectedTrailer,
                expectedVideo,
                expectedCategories,
                expectedGenres,
                expectedMembers

        );

        Assertions.assertNotNull(actualVideo);
        Assertions.assertNotNull(actualVideo.id());
        Assertions.assertEquals(expectedTitle, actualVideo.title());
        Assertions.assertEquals(expectedDescription, actualVideo.description());
        Assertions.assertEquals(expectedLaunchedAt, actualVideo.launchedAt().getValue());
        Assertions.assertEquals(expectedDuration, actualVideo.duration());
        Assertions.assertEquals(expectedOpened, actualVideo.opened());
        Assertions.assertEquals(expectedPublished, actualVideo.published());
        Assertions.assertEquals(expectedRating, actualVideo.rating().getName());
        Assertions.assertEquals(expectedCreatedAt, actualVideo.createdAt().toString());
        Assertions.assertEquals(expectedUpdatedAt, actualVideo.updatedAt().toString());
        Assertions.assertEquals(expectedCategories, actualVideo.categories());
        Assertions.assertEquals(expectedGenres, actualVideo.genres());
        Assertions.assertEquals(expectedMembers, actualVideo.castMembers());
        Assertions.assertEquals(expectedVideo,actualVideo.video());
        Assertions.assertEquals(expectedTrailer,actualVideo.trailer());
        Assertions.assertEquals(expectedBanner, actualVideo.banner());
        Assertions.assertEquals(expectedThumb, actualVideo.thumb());
        Assertions.assertEquals(expectedThumbHalf, actualVideo.thumbHalf());


    }

    @Test
    public void givenValidVideo_whenCallVideoWithShouldInstantiate(){
        final var expectedId = IdUtils.uniqueId();
        final var expectedTitle = "System Design Interview";
        final var expectedDescription = """
            Lets design the high-level architecture of youtube - similar to 
            how we'd tackle this in a system design interview.
            """;
        final var expectedLaunchedAt = 2022;
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = "L";
        final var expectedCreatedAt = InstantUtils.now().toString();
        final var expectedUpdatedAt = InstantUtils.now().toString();
        final var expectedCategories = Set.of(IdUtils.uniqueId());
        final var expectedGenres = Set.of(IdUtils.uniqueId());
        final var expectedMembers = Set.of(IdUtils.uniqueId());
        final var expectedVideo= "http://video";
        final var expectedTrailer = "http://trailer";
        final var expectedBanner = "http://banner";
        final var expectedThumb = "http://thumbnaill";
        final var expectedThumbHalf =  "http://thumbnaill-half";



        final var video = Video.with(
                expectedId,
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCreatedAt,
                expectedUpdatedAt,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf,
                expectedTrailer,
                expectedVideo,
                expectedCategories,
                expectedGenres,
                expectedMembers

        );

        final var actualVideo = Video.with(video);

        Assertions.assertNotNull(actualVideo);
        Assertions.assertNotNull(actualVideo.id());
        Assertions.assertEquals(expectedTitle, actualVideo.title());
        Assertions.assertEquals(expectedDescription, actualVideo.description());
        Assertions.assertEquals(expectedLaunchedAt, actualVideo.launchedAt().getValue());
        Assertions.assertEquals(expectedDuration, actualVideo.duration());
        Assertions.assertEquals(expectedOpened, actualVideo.opened());
        Assertions.assertEquals(expectedPublished, actualVideo.published());
        Assertions.assertEquals(expectedRating, actualVideo.rating().getName());
        Assertions.assertEquals(expectedCreatedAt, actualVideo.createdAt().toString());
        Assertions.assertEquals(expectedUpdatedAt, actualVideo.updatedAt().toString());
        Assertions.assertEquals(expectedCategories, actualVideo.categories());
        Assertions.assertEquals(expectedGenres, actualVideo.genres());
        Assertions.assertEquals(expectedMembers, actualVideo.castMembers());
        Assertions.assertEquals(expectedVideo,actualVideo.video());
        Assertions.assertEquals(expectedTrailer,actualVideo.trailer());
        Assertions.assertEquals(expectedBanner, actualVideo.banner());
        Assertions.assertEquals(expectedThumb, actualVideo.thumb());
        Assertions.assertEquals(expectedThumbHalf, actualVideo.thumbHalf());


    }

    @Test
    public void givenEmptyVideo_whenCallVideoWithShouldOverwritePublishedToFalse(){
        final var expectedId = IdUtils.uniqueId();
        final var expectedTitle = "System Design Interview";
        final var expectedDescription = """
            Lets design the high-level architecture of youtube - similar to 
            how we'd tackle this in a system design interview.
            """;
        final var expectedLaunchedAt = 2022;
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = "L";
        final var expectedCreatedAt = InstantUtils.now().toString();
        final var expectedUpdatedAt = InstantUtils.now().toString();
        final var expectedCategories = Set.of(IdUtils.uniqueId());
        final var expectedGenres = Set.of(IdUtils.uniqueId());
        final var expectedMembers = Set.of(IdUtils.uniqueId());
        final var expectedVideo= "";
        final var expectedTrailer = "http://trailer";
        final var expectedBanner = "http://banner";
        final var expectedThumb = "http://thumbnaill";
        final var expectedThumbHalf =  "http://thumbnaill-half";



        final var actualVideo = Video.with(
                expectedId,
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                true,
                expectedCreatedAt,
                expectedUpdatedAt,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf,
                expectedTrailer,
                expectedVideo,
                expectedCategories,
                expectedGenres,
                expectedMembers

        );

        Assertions.assertNotNull(actualVideo);
        Assertions.assertNotNull(actualVideo.id());
        Assertions.assertEquals(expectedTitle, actualVideo.title());
        Assertions.assertEquals(expectedDescription, actualVideo.description());
        Assertions.assertEquals(expectedLaunchedAt, actualVideo.launchedAt().getValue());
        Assertions.assertEquals(expectedDuration, actualVideo.duration());
        Assertions.assertEquals(expectedOpened, actualVideo.opened());
        Assertions.assertEquals(expectedPublished, actualVideo.published());
        Assertions.assertEquals(expectedRating, actualVideo.rating().getName());
        Assertions.assertEquals(expectedCreatedAt, actualVideo.createdAt().toString());
        Assertions.assertEquals(expectedUpdatedAt, actualVideo.updatedAt().toString());
        Assertions.assertEquals(expectedCategories, actualVideo.categories());
        Assertions.assertEquals(expectedGenres, actualVideo.genres());
        Assertions.assertEquals(expectedMembers, actualVideo.castMembers());
        Assertions.assertEquals(expectedVideo,actualVideo.video());
        Assertions.assertEquals(expectedTrailer,actualVideo.trailer());
        Assertions.assertEquals(expectedBanner, actualVideo.banner());
        Assertions.assertEquals(expectedThumb, actualVideo.thumb());
        Assertions.assertEquals(expectedThumbHalf, actualVideo.thumbHalf());


    }

    @Test
    public void givenEmptyBanner_whenCallVideoWithShouldOverwritePublishedToFalse(){
        final var expectedId = IdUtils.uniqueId();
        final var expectedTitle = "System Design Interview";
        final var expectedDescription = """
            Lets design the high-level architecture of youtube - similar to 
            how we'd tackle this in a system design interview.
            """;
        final var expectedLaunchedAt = 2022;
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = "L";
        final var expectedCreatedAt = InstantUtils.now().toString();
        final var expectedUpdatedAt = InstantUtils.now().toString();
        final var expectedCategories = Set.of(IdUtils.uniqueId());
        final var expectedGenres = Set.of(IdUtils.uniqueId());
        final var expectedMembers = Set.of(IdUtils.uniqueId());
        final var expectedVideo= "http://video";
        final var expectedTrailer = "http://trailer";
        final var expectedBanner = "";
        final var expectedThumb = "http://thumbnaill";
        final var expectedThumbHalf =  "http://thumbnaill-half";



        final var actualVideo = Video.with(
                expectedId,
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                true,
                expectedCreatedAt,
                expectedUpdatedAt,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf,
                expectedTrailer,
                expectedVideo,
                expectedCategories,
                expectedGenres,
                expectedMembers

        );

        Assertions.assertNotNull(actualVideo);
        Assertions.assertNotNull(actualVideo.id());
        Assertions.assertEquals(expectedTitle, actualVideo.title());
        Assertions.assertEquals(expectedDescription, actualVideo.description());
        Assertions.assertEquals(expectedLaunchedAt, actualVideo.launchedAt().getValue());
        Assertions.assertEquals(expectedDuration, actualVideo.duration());
        Assertions.assertEquals(expectedOpened, actualVideo.opened());
        Assertions.assertEquals(expectedPublished, actualVideo.published());
        Assertions.assertEquals(expectedRating, actualVideo.rating().getName());
        Assertions.assertEquals(expectedCreatedAt, actualVideo.createdAt().toString());
        Assertions.assertEquals(expectedUpdatedAt, actualVideo.updatedAt().toString());
        Assertions.assertEquals(expectedCategories, actualVideo.categories());
        Assertions.assertEquals(expectedGenres, actualVideo.genres());
        Assertions.assertEquals(expectedMembers, actualVideo.castMembers());
        Assertions.assertEquals(expectedVideo,actualVideo.video());
        Assertions.assertEquals(expectedTrailer,actualVideo.trailer());
        Assertions.assertEquals(expectedBanner, actualVideo.banner());
        Assertions.assertEquals(expectedThumb, actualVideo.thumb());
        Assertions.assertEquals(expectedThumbHalf, actualVideo.thumbHalf());


    }

    @Test
    public void givenEmptyTrailer_whenCallVideoWithShouldOverwritePublishedToFalse(){
        final var expectedId = IdUtils.uniqueId();
        final var expectedTitle = "System Design Interview";
        final var expectedDescription = """
            Lets design the high-level architecture of youtube - similar to 
            how we'd tackle this in a system design interview.
            """;
        final var expectedLaunchedAt = 2022;
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = "L";
        final var expectedCreatedAt = InstantUtils.now().toString();
        final var expectedUpdatedAt = InstantUtils.now().toString();
        final var expectedCategories = Set.of(IdUtils.uniqueId());
        final var expectedGenres = Set.of(IdUtils.uniqueId());
        final var expectedMembers = Set.of(IdUtils.uniqueId());
        final var expectedVideo= "http://video";
        final var expectedTrailer = "";
        final var expectedBanner = "http://banner";
        final var expectedThumb = "http://thumbnaill";
        final var expectedThumbHalf =  "http://thumbnaill-half";



        final var actualVideo = Video.with(
                expectedId,
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                true,
                expectedCreatedAt,
                expectedUpdatedAt,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf,
                expectedTrailer,
                expectedVideo,
                expectedCategories,
                expectedGenres,
                expectedMembers

        );

        Assertions.assertNotNull(actualVideo);
        Assertions.assertNotNull(actualVideo.id());
        Assertions.assertEquals(expectedTitle, actualVideo.title());
        Assertions.assertEquals(expectedDescription, actualVideo.description());
        Assertions.assertEquals(expectedLaunchedAt, actualVideo.launchedAt().getValue());
        Assertions.assertEquals(expectedDuration, actualVideo.duration());
        Assertions.assertEquals(expectedOpened, actualVideo.opened());
        Assertions.assertEquals(expectedPublished, actualVideo.published());
        Assertions.assertEquals(expectedRating, actualVideo.rating().getName());
        Assertions.assertEquals(expectedCreatedAt, actualVideo.createdAt().toString());
        Assertions.assertEquals(expectedUpdatedAt, actualVideo.updatedAt().toString());
        Assertions.assertEquals(expectedCategories, actualVideo.categories());
        Assertions.assertEquals(expectedGenres, actualVideo.genres());
        Assertions.assertEquals(expectedMembers, actualVideo.castMembers());
        Assertions.assertEquals(expectedVideo,actualVideo.video());
        Assertions.assertEquals(expectedTrailer,actualVideo.trailer());
        Assertions.assertEquals(expectedBanner, actualVideo.banner());
        Assertions.assertEquals(expectedThumb, actualVideo.thumb());
        Assertions.assertEquals(expectedThumbHalf, actualVideo.thumbHalf());


    }

    @Test
    public void givenEmptyThumbnail_whenCallVideoWithShouldOverwritePublishedToFalse(){
        final var expectedId = IdUtils.uniqueId();
        final var expectedTitle = "System Design Interview";
        final var expectedDescription = """
            Lets design the high-level architecture of youtube - similar to 
            how we'd tackle this in a system design interview.
            """;
        final var expectedLaunchedAt = 2022;
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = "L";
        final var expectedCreatedAt = InstantUtils.now().toString();
        final var expectedUpdatedAt = InstantUtils.now().toString();
        final var expectedCategories = Set.of(IdUtils.uniqueId());
        final var expectedGenres = Set.of(IdUtils.uniqueId());
        final var expectedMembers = Set.of(IdUtils.uniqueId());
        final var expectedVideo= "http://video";
        final var expectedTrailer = "http;//trailer";
        final var expectedBanner = "http://banner";
        final var expectedThumb = "";
        final var expectedThumbHalf =  "http://thumbnaill-half";



        final var actualVideo = Video.with(
                expectedId,
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                true,
                expectedCreatedAt,
                expectedUpdatedAt,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf,
                expectedTrailer,
                expectedVideo,
                expectedCategories,
                expectedGenres,
                expectedMembers

        );

        Assertions.assertNotNull(actualVideo);
        Assertions.assertNotNull(actualVideo.id());
        Assertions.assertEquals(expectedTitle, actualVideo.title());
        Assertions.assertEquals(expectedDescription, actualVideo.description());
        Assertions.assertEquals(expectedLaunchedAt, actualVideo.launchedAt().getValue());
        Assertions.assertEquals(expectedDuration, actualVideo.duration());
        Assertions.assertEquals(expectedOpened, actualVideo.opened());
        Assertions.assertEquals(expectedPublished, actualVideo.published());
        Assertions.assertEquals(expectedRating, actualVideo.rating().getName());
        Assertions.assertEquals(expectedCreatedAt, actualVideo.createdAt().toString());
        Assertions.assertEquals(expectedUpdatedAt, actualVideo.updatedAt().toString());
        Assertions.assertEquals(expectedCategories, actualVideo.categories());
        Assertions.assertEquals(expectedGenres, actualVideo.genres());
        Assertions.assertEquals(expectedMembers, actualVideo.castMembers());
        Assertions.assertEquals(expectedVideo,actualVideo.video());
        Assertions.assertEquals(expectedTrailer,actualVideo.trailer());
        Assertions.assertEquals(expectedBanner, actualVideo.banner());
        Assertions.assertEquals(expectedThumb, actualVideo.thumb());
        Assertions.assertEquals(expectedThumbHalf, actualVideo.thumbHalf());


    }

    @Test
    public void givenEmptyThumbnailHalf_whenCallVideoWithShouldOverwritePublishedToFalse(){
        final var expectedId = IdUtils.uniqueId();
        final var expectedTitle = "System Design Interview";
        final var expectedDescription = """
            Lets design the high-level architecture of youtube - similar to 
            how we'd tackle this in a system design interview.
            """;
        final var expectedLaunchedAt = 2022;
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = "L";
        final var expectedCreatedAt = InstantUtils.now().toString();
        final var expectedUpdatedAt = InstantUtils.now().toString();
        final var expectedCategories = Set.of(IdUtils.uniqueId());
        final var expectedGenres = Set.of(IdUtils.uniqueId());
        final var expectedMembers = Set.of(IdUtils.uniqueId());
        final var expectedVideo= "http://video";
        final var expectedTrailer = "http;//trailer";
        final var expectedBanner = "http://banner";
        final var expectedThumb = "http://thumbnail";
        final var expectedThumbHalf = "";



        final var actualVideo = Video.with(
                expectedId,
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                true,
                expectedCreatedAt,
                expectedUpdatedAt,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf,
                expectedTrailer,
                expectedVideo,
                expectedCategories,
                expectedGenres,
                expectedMembers

        );

        Assertions.assertNotNull(actualVideo);
        Assertions.assertNotNull(actualVideo.id());
        Assertions.assertEquals(expectedTitle, actualVideo.title());
        Assertions.assertEquals(expectedDescription, actualVideo.description());
        Assertions.assertEquals(expectedLaunchedAt, actualVideo.launchedAt().getValue());
        Assertions.assertEquals(expectedDuration, actualVideo.duration());
        Assertions.assertEquals(expectedOpened, actualVideo.opened());
        Assertions.assertEquals(expectedPublished, actualVideo.published());
        Assertions.assertEquals(expectedRating, actualVideo.rating().getName());
        Assertions.assertEquals(expectedCreatedAt, actualVideo.createdAt().toString());
        Assertions.assertEquals(expectedUpdatedAt, actualVideo.updatedAt().toString());
        Assertions.assertEquals(expectedCategories, actualVideo.categories());
        Assertions.assertEquals(expectedGenres, actualVideo.genres());
        Assertions.assertEquals(expectedMembers, actualVideo.castMembers());
        Assertions.assertEquals(expectedVideo,actualVideo.video());
        Assertions.assertEquals(expectedTrailer,actualVideo.trailer());
        Assertions.assertEquals(expectedBanner, actualVideo.banner());
        Assertions.assertEquals(expectedThumb, actualVideo.thumb());
        Assertions.assertEquals(expectedThumbHalf, actualVideo.thumbHalf());


    }

    @Test
    public void givenAnInvalidNullIdWhenCallWithAndValidate_thenShouldReturnAnException() {
        final String expectedId = null;
        final var expectedTitle = "System Design Interview";
        final var expectedDescription = """
            Lets design the high-level architecture of youtube - similar to 
            how we'd tackle this in a system design interview.
            """;
        final var expectedLaunchedAt = 2022;
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = "L";
        final var expectedCreatedAt = InstantUtils.now().toString();
        final var expectedUpdatedAt = InstantUtils.now().toString();
        final var expectedCategories = Set.of(IdUtils.uniqueId());
        final var expectedGenres = Set.of(IdUtils.uniqueId());
        final var expectedMembers = Set.of(IdUtils.uniqueId());
        final var expectedVideo= "http://video";
        final var expectedTrailer = "http;//trailer";
        final var expectedBanner = "http://banner";
        final var expectedThumb = "http://thumbnail";
        final var expectedThumbHalf =  "http://thumbnail-half";

        final var expectedErrorMessage = "'id' should not be empty";
        final var expectedErrorCount = 1;

        
        final var actualException =
                Assertions.assertThrows(DomainException.class, () ->
                        Video.with(
                                expectedId,
                                expectedTitle,
                                expectedDescription,
                                expectedLaunchedAt,
                                expectedDuration,
                                expectedRating,
                                expectedOpened,
                                expectedPublished,
                                expectedCreatedAt,
                                expectedUpdatedAt,
                                expectedBanner,
                                expectedThumb,
                                expectedThumbHalf,
                                expectedTrailer,
                                expectedVideo,
                                expectedCategories,
                                expectedGenres,
                                expectedMembers

                        ));
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());

    }


    @Test
    public void givenAnInvalidEmptyIdWhenCallWithAndValidate_thenShouldReturnAnException() {
        final String expectedId = "";
        final var expectedTitle = "System Design Interview";
        final var expectedDescription = """
            Lets design the high-level architecture of youtube - similar to 
            how we'd tackle this in a system design interview.
            """;
        final var expectedLaunchedAt = 2022;
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = "L";
        final var expectedCreatedAt = InstantUtils.now().toString();
        final var expectedUpdatedAt = InstantUtils.now().toString();
        final var expectedCategories = Set.of(IdUtils.uniqueId());
        final var expectedGenres = Set.of(IdUtils.uniqueId());
        final var expectedMembers = Set.of(IdUtils.uniqueId());
        final var expectedVideo= "http://video";
        final var expectedTrailer = "http;//trailer";
        final var expectedBanner = "http://banner";
        final var expectedThumb = "http://thumbnail";
        final var expectedThumbHalf =  "http://thumbnail-half";

        final var expectedErrorMessage = "'id' should not be empty";
        final var expectedErrorCount = 1;

        final var actualException =
                Assertions.assertThrows(DomainException.class, () ->
                        Video.with(
                                expectedId,
                                expectedTitle,
                                expectedDescription,
                                expectedLaunchedAt,
                                expectedDuration,
                                expectedRating,
                                expectedOpened,
                                expectedPublished,
                                expectedCreatedAt,
                                expectedUpdatedAt,
                                expectedBanner,
                                expectedThumb,
                                expectedThumbHalf,
                                expectedTrailer,
                                expectedVideo,
                                expectedCategories,
                                expectedGenres,
                                expectedMembers

                        ));
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());

    }

    @Test
    public void givenAnInvalidNullTitleWhenCallWithAndValidate_thenShouldReturnAnException() {
        final var expectedId = IdUtils.uniqueId();
        final String expectedTitle = null;
        final var expectedDescription = """
            Lets design the high-level architecture of youtube - similar to 
            how we'd tackle this in a system design interview.
            """;
        final var expectedLaunchedAt = 2022;
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = "L";
        final var expectedCreatedAt = InstantUtils.now().toString();
        final var expectedUpdatedAt = InstantUtils.now().toString();
        final var expectedCategories = Set.of(IdUtils.uniqueId());
        final var expectedGenres = Set.of(IdUtils.uniqueId());
        final var expectedMembers = Set.of(IdUtils.uniqueId());
        final var expectedVideo= "http://video";
        final var expectedTrailer = "http;//trailer";
        final var expectedBanner = "http://banner";
        final var expectedThumb = "http://thumbnail";
        final var expectedThumbHalf =  "http://thumbnail-half";

        final var expectedErrorMessage = "'title' should not be empty";
        final var expectedErrorCount = 1;


        final var actualException =
                Assertions.assertThrows(DomainException.class, () ->
                        Video.with(
                                expectedId,
                                expectedTitle,
                                expectedDescription,
                                expectedLaunchedAt,
                                expectedDuration,
                                expectedRating,
                                expectedOpened,
                                expectedPublished,
                                expectedCreatedAt,
                                expectedUpdatedAt,
                                expectedBanner,
                                expectedThumb,
                                expectedThumbHalf,
                                expectedTrailer,
                                expectedVideo,
                                expectedCategories,
                                expectedGenres,
                                expectedMembers

                        ));
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());

    }


    @Test
    public void givenAnInvalidEmptyTitleWhenCallWithAndValidate_thenShouldReturnAnException() {
        final var expectedId = IdUtils.uniqueId();
        final var expectedTitle = "";
        final var expectedDescription = """
            Lets design the high-level architecture of youtube - similar to 
            how we'd tackle this in a system design interview.
            """;
        final var expectedLaunchedAt =2022;
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = "L";
        final var expectedCreatedAt = InstantUtils.now().toString();
        final var expectedUpdatedAt = InstantUtils.now().toString();
        final var expectedCategories = Set.of(IdUtils.uniqueId());
        final var expectedGenres = Set.of(IdUtils.uniqueId());
        final var expectedMembers = Set.of(IdUtils.uniqueId());
        final var expectedVideo= "http://video";
        final var expectedTrailer = "http;//trailer";
        final var expectedBanner = "http://banner";
        final var expectedThumb = "http://thumbnail";
        final var expectedThumbHalf =  "http://thumbnail-half";

        final var expectedErrorMessage = "'title' should not be empty";
        final var expectedErrorCount = 1;

        final var actualException =
                Assertions.assertThrows(DomainException.class, () ->
                        Video.with(
                                expectedId,
                                expectedTitle,
                                expectedDescription,
                                expectedLaunchedAt,
                                expectedDuration,
                                expectedRating,
                                expectedOpened,
                                expectedPublished,
                                expectedCreatedAt,
                                expectedUpdatedAt,
                                expectedBanner,
                                expectedThumb,
                                expectedThumbHalf,
                                expectedTrailer,
                                expectedVideo,
                                expectedCategories,
                                expectedGenres,
                                expectedMembers

                        ));
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());

    }

    @Test
    public void givenAnInvalidNullLaunchedAtWhenCallWithAndValidate_thenShouldReturnAnException() {
        final var expectedId = IdUtils.uniqueId();
        final var expectedTitle = "System Design Interview";
        final var expectedDescription = """
            Lets design the high-level architecture of youtube - similar to 
            how we'd tackle this in a system design interview.
            """;
        final Integer expectedLaunchedAt = null;
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = "L";
        final var expectedCreatedAt = InstantUtils.now().toString();
        final var expectedUpdatedAt = InstantUtils.now().toString();
        final var expectedCategories = Set.of(IdUtils.uniqueId());
        final var expectedGenres = Set.of(IdUtils.uniqueId());
        final var expectedMembers = Set.of(IdUtils.uniqueId());
        final var expectedVideo= "http://video";
        final var expectedTrailer = "http;//trailer";
        final var expectedBanner = "http://banner";
        final var expectedThumb = "http://thumbnail";
        final var expectedThumbHalf =  "http://thumbnail-half";

        final var expectedErrorMessage = "'launchedAt' should not be empty";
        final var expectedErrorCount = 1;


        final var actualException =
                Assertions.assertThrows(DomainException.class, () ->
                        Video.with(
                                expectedId,
                                expectedTitle,
                                expectedDescription,
                                expectedLaunchedAt,
                                expectedDuration,
                                expectedRating,
                                expectedOpened,
                                expectedPublished,
                                expectedCreatedAt,
                                expectedUpdatedAt,
                                expectedBanner,
                                expectedThumb,
                                expectedThumbHalf,
                                expectedTrailer,
                                expectedVideo,
                                expectedCategories,
                                expectedGenres,
                                expectedMembers

                        ));
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());

    }

    @Test
    public void givenAnInvalidNullRatingAtWhenCallWithAndValidate_thenShouldReturnAnException() {
        final var expectedId = IdUtils.uniqueId();
        final var expectedTitle = "System Design Interview";
        final var expectedDescription = """
            Lets design the high-level architecture of youtube - similar to 
            how we'd tackle this in a system design interview.
            """;
        final var expectedLaunchedAt = 2022;
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final String expectedRating = null;
        final var expectedCreatedAt = InstantUtils.now().toString();
        final var expectedUpdatedAt = InstantUtils.now().toString();
        final var expectedCategories = Set.of(IdUtils.uniqueId());
        final var expectedGenres = Set.of(IdUtils.uniqueId());
        final var expectedMembers = Set.of(IdUtils.uniqueId());
        final var expectedVideo= "http://video";
        final var expectedTrailer = "http;//trailer";
        final var expectedBanner = "http://banner";
        final var expectedThumb = "http://thumbnail";
        final var expectedThumbHalf =  "http://thumbnail-half";

        final var expectedErrorMessage = "'rating' should not be empty";
        final var expectedErrorCount = 1;


        final var actualException =
                Assertions.assertThrows(DomainException.class, () ->
                        Video.with(
                                expectedId,
                                expectedTitle,
                                expectedDescription,
                                expectedLaunchedAt,
                                expectedDuration,
                                expectedRating,
                                expectedOpened,
                                expectedPublished,
                                expectedCreatedAt,
                                expectedUpdatedAt,
                                expectedBanner,
                                expectedThumb,
                                expectedThumbHalf,
                                expectedTrailer,
                                expectedVideo,
                                expectedCategories,
                                expectedGenres,
                                expectedMembers

                        ));
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());

    }
    @Test
    public void givenAnInvalidNullCreatedAtWhenCallWithAndValidate_thenShouldReturnAnException() {
        final var expectedId = IdUtils.uniqueId();
        final var expectedTitle = "System Design Interview";
        final var expectedDescription = """
            Lets design the high-level architecture of youtube - similar to 
            how we'd tackle this in a system design interview.
            """;
        final var expectedLaunchedAt = 2022;
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = "L";
        final String expectedCreatedAt = null;
        final var expectedUpdatedAt = InstantUtils.now().toString();
        final var expectedCategories = Set.of(IdUtils.uniqueId());
        final var expectedGenres = Set.of(IdUtils.uniqueId());
        final var expectedMembers = Set.of(IdUtils.uniqueId());
        final var expectedVideo= "http://video";
        final var expectedTrailer = "http;//trailer";
        final var expectedBanner = "http://banner";
        final var expectedThumb = "http://thumbnail";
        final var expectedThumbHalf =  "http://thumbnail-half";

        final var expectedErrorMessage = "'createdAt' should not be empty";
        final var expectedErrorCount = 1;


        final var actualException =
                Assertions.assertThrows(DomainException.class, () ->
                        Video.with(
                                expectedId,
                                expectedTitle,
                                expectedDescription,
                                expectedLaunchedAt,
                                expectedDuration,
                                expectedRating,
                                expectedOpened,
                                expectedPublished,
                                expectedCreatedAt,
                                expectedUpdatedAt,
                                expectedBanner,
                                expectedThumb,
                                expectedThumbHalf,
                                expectedTrailer,
                                expectedVideo,
                                expectedCategories,
                                expectedGenres,
                                expectedMembers

                        ));
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());

    }

    @Test
    public void givenAnInvalidNullUpdatedAtWhenCallWithAndValidate_thenShouldReturnAnException() {
        final var expectedId = IdUtils.uniqueId();
        final var expectedTitle = "System Design Interview";
        final var expectedDescription = """
            Lets design the high-level architecture of youtube - similar to 
            how we'd tackle this in a system design interview.
            """;
        final var expectedLaunchedAt = 2022;
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = "L";
        final var expectedCreatedAt = InstantUtils.now().toString();
        final String expectedUpdatedAt = null;
        final var expectedCategories = Set.of(IdUtils.uniqueId());
        final var expectedGenres = Set.of(IdUtils.uniqueId());
        final var expectedMembers = Set.of(IdUtils.uniqueId());
        final var expectedVideo= "http://video";
        final var expectedTrailer = "http;//trailer";
        final var expectedBanner = "http://banner";
        final var expectedThumb = "http://thumbnail";
        final var expectedThumbHalf =  "http://thumbnail-half";

        final var expectedErrorMessage = "'updatedAt' should not be empty";
        final var expectedErrorCount = 1;


        final var actualException =
                Assertions.assertThrows(DomainException.class, () ->
                        Video.with(
                                expectedId,
                                expectedTitle,
                                expectedDescription,
                                expectedLaunchedAt,
                                expectedDuration,
                                expectedRating,
                                expectedOpened,
                                expectedPublished,
                                expectedCreatedAt,
                                expectedUpdatedAt,
                                expectedBanner,
                                expectedThumb,
                                expectedThumbHalf,
                                expectedTrailer,
                                expectedVideo,
                                expectedCategories,
                                expectedGenres,
                                expectedMembers

                        ));
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());

    }

    @Test
    public void givenAnInvalidNullCollectionsWhenCallWithAndValidate_thenShouldAssignAEmptySet() {
        final var expectedId = IdUtils.uniqueId();
        final var expectedTitle = "System Design Interview";
        final var expectedDescription = """
            Lets design the high-level architecture of youtube - similar to 
            how we'd tackle this in a system design interview.
            """;
        final var expectedLaunchedAt = 2022;
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = "L";
        final var expectedCreatedAt = InstantUtils.now().toString();
        final var expectedUpdatedAt = InstantUtils.now().toString();
        final var expectedCategories = Set.<String>of();
        final var expectedGenres = Set.<String>of();
        final var expectedMembers = Set.<String>of();
        final var expectedVideo= "http://video";
        final var expectedTrailer = "http;//trailer";
        final var expectedBanner = "http://banner";
        final var expectedThumb = "http://thumbnail";
        final var expectedThumbHalf =  "http://thumbnail-half";

        final var video = Video.with(
                expectedId,
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCreatedAt,
                expectedUpdatedAt,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf,
                expectedTrailer,
                expectedVideo,
                null,
                null,
                null

        );

        final var actualVideo = Video.with(video);

        Assertions.assertNotNull(actualVideo);
        Assertions.assertNotNull(actualVideo.id());
        Assertions.assertEquals(expectedTitle, actualVideo.title());
        Assertions.assertEquals(expectedDescription, actualVideo.description());
        Assertions.assertEquals(expectedLaunchedAt, actualVideo.launchedAt().getValue());
        Assertions.assertEquals(expectedDuration, actualVideo.duration());
        Assertions.assertEquals(expectedOpened, actualVideo.opened());
        Assertions.assertEquals(expectedPublished, actualVideo.published());
        Assertions.assertEquals(expectedRating, actualVideo.rating().getName());
        Assertions.assertEquals(expectedCreatedAt, actualVideo.createdAt().toString());
        Assertions.assertEquals(expectedUpdatedAt, actualVideo.updatedAt().toString());
        Assertions.assertEquals(expectedCategories, actualVideo.categories());
        Assertions.assertEquals(expectedGenres, actualVideo.genres());
        Assertions.assertEquals(expectedMembers, actualVideo.castMembers());
        Assertions.assertEquals(expectedVideo,actualVideo.video());
        Assertions.assertEquals(expectedTrailer,actualVideo.trailer());
        Assertions.assertEquals(expectedBanner, actualVideo.banner());
        Assertions.assertEquals(expectedThumb, actualVideo.thumb());
        Assertions.assertEquals(expectedThumbHalf, actualVideo.thumbHalf());

    }





}
