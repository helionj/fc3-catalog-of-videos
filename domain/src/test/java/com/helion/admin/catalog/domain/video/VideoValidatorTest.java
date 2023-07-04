package com.helion.admin.catalog.domain.video;

import com.helion.admin.catalog.domain.castmember.CastMemberID;
import com.helion.admin.catalog.domain.category.CategoryID;
import com.helion.admin.catalog.domain.exceptions.DomainException;
import com.helion.admin.catalog.domain.genre.GenreID;
import com.helion.admin.catalog.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.Set;

public class VideoValidatorTest {

    @Test
    public void givenNullTitle_whenCallValidate_shouldReceiveAnError(){

        final String expectedTitle = null;
        final var expectedDescription = """
            Lets design the high-level architecture of youtube - similar to 
            how we'd tackle this in a system design interview.
            """;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'title' should not be null";


        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        final var actualError = Assertions.assertThrows(DomainException.class,
                () -> actualVideo.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());


    }
    @Test
    public void givenEmptyTitle_whenCallValidate_shouldReceiveAnError(){
        final String expectedTitle = "";
        final var expectedDescription = """
            Lets design the high-level architecture of youtube - similar to 
            how we'd tackle this in a system design interview.
            """;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'title' should not be empty";


        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );
        final var actualError = Assertions.assertThrows(DomainException.class,
                () -> actualVideo.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }
    @Test
    public void givenTitleLengthGreaterThan255_whenCallValidate_shouldReceiveAnError(){
        final String expectedTitle = """
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. 
                Quisque ultricies ligula ac elit tempor, ac tincidunt ex mattis. 
                Proin vulputate odio vitae fringilla porttitor. Cras gravida, nunc 
                vel eleifend sodales, tortor est fermentum ex, id ultricies augue ligula quis urna. 
                Pellentesque orci libero, bibendum vel mollis eu, aliquam ac risus
                """;

        final var expectedDescription = """
            Lets design the high-level architecture of youtube - similar to 
            how we'd tackle this in a system design interview.
            """;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'title' must be between 3 and 255 characters";


        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        final var actualError = Assertions.assertThrows(DomainException.class,
                () -> actualVideo.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }
    @Test
    public void givenNullDescription_whenCallValidate_shouldReceiveAnError(){
        final var expectedTitle = "System Design Interview";
        final String expectedDescription = null;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'description' should not be null";


        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        final var actualError = Assertions.assertThrows(DomainException.class,
                () -> actualVideo.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }
    @Test
    public void givenEmptyDescription_whenCallValidate_shouldReceiveAnError(){
        final var expectedTitle = "System Design Interview";
        final String expectedDescription = " ";
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'description' should not be empty";


        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        final var actualError = Assertions.assertThrows(DomainException.class,
                () -> actualVideo.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }
    @Test
    public void givenDescriptionLengthGreaterThan4000_whenCallValidate_shouldReceiveAnError(){
        final var expectedTitle = "System Design Interview";
        final String expectedDescription = """
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam a turpis justo. Morbi fringilla consectetur ipsum, 
                in dapibus tellus dictum sed. Pellentesque lobortis nisl sit amet posuere consectetur. Proin dapibus felis in auctor molestie. 
                Nunc a condimentum nibh. Mauris sit amet volutpat justo, ut molestie odio. Vestibulum sagittis erat ut arcu tincidunt scelerisque. 
                Nunc congue nibh non nulla tincidunt, sagittis facilisis dolor scelerisque. Curabitur ornare vitae metus quis convallis. Cras viverra 
                ligula at ante porta aliquet. Nulla facilisi. Sed dapibus sollicitudin malesuada. Nam ipsum lacus, eleifend et rutrum vel, scelerisque 
                at enim. Vestibulum eu neque at ex fermentum vehicula a id erat. Quisque quis sem metus. Phasellus cursus tincidunt sapien nec congue.
                                
                Quisque faucibus ex augue, sed eleifend erat porta ac. Suspendisse potenti. Nam maximus lectus et lorem venenatis, vitae ultrices purus
                tempus. In lorem erat, auctor eu magna at, cursus congue neque. Aliquam ultrices nisi nec elit scelerisque consequat. Nam ut enim nisl.
                Quisque odio ligula, euismod ut tristique nec, egestas quis dolor. Nullam maximus, est at placerat euismod, nunc risus vulputate felis,
                eu sollicitudin ligula quam ac enim. Morbi malesuada aliquam consectetur. Nam placerat tempus metus.
                                
                Morbi rhoncus dictum pretium. Pellentesque suscipit, mi in vehicula ultrices, sem purus pulvinar lacus, eget consequat neque tortor et 
                lorem. Maecenas varius blandit justo vitae dignissim. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos 
                himenaeos. Mauris aliquam eget elit non volutpat. Donec id quam orci. Duis non suscipit orci. Sed vehicula placerat ipsum, a ultricies 
                dui maximus eu. Quisque imperdiet, lorem sit amet condimentum rhoncus, eros ligula volutpat arcu, a aliquam tortor est sit amet tortor. 
                Vivamus tellus lectus, vulputate tempor ullamcorper vel, sodales id ipsum. Nulla et bibendum eros. Vestibulum iaculis accumsan elit vel 
                dignissim. Aliquam nec risus tincidunt, sollicitudin sapien quis, consequat nunc. Integer metus ligula, posuere ac luctus in, auctor at 
                lorem. Sed consectetur, ligula sit amet fringilla finibus, lacus velit varius tellus, nec ullamcorper turpis lacus in purus.
                                
                Pellentesque ultricies dolor sit amet ligula ornare, eget elementum ante finibus. Duis interdum odio eget commodo volutpat. Morbi 
                pulvinar vestibulum purus nec sollicitudin. Proin elementum quis sapien eget lobortis. Sed eleifend ullamcorper placerat. Curabitur eu 
                aliquet tortor. Aliquam at neque sed arcu bibendum lacinia vel sed mi. Mauris auctor orci eu velit luctus, quis tincidunt sapien varius. 
                Praesent tristique massa at augue placerat, vitae sollicitudin ipsum ullamcorper.
                                
                Duis sit amet ex id nulla fringilla accumsan. Duis dapibus porttitor augue, in egestas dui ornare id. Duis viverra at purus in feugiat. 
                Vestibulum quis scelerisque odio. Mauris imperdiet ex vitae tellus ultrices, ut eleifend magna semper. Mauris ut laoreet neque, vitae 
                tempus sapien. Sed auctor felis in ligula maximus faucibus. Vivamus at velit eros. Praesent vitae tincidunt nisl, laoreet hendrerit mi. 
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam rhoncus euismod dui in pretium. Mauris enim dolor, luctus sed dui at, 
                sollicitudin tempus purus.
                                
                Quisque at faucibus ante, gravida maximus lacus. Donec sed tempor eros, nec placerat tortor. Sed imperdiet, neque eget elementum auctor, 
                velit quam porta lectus, id elementum velit magna eget metus. Vestibulum ultricies enim blandit ligula tempus, at lacinia turpis commodo. 
                Proin tortor ex, feugiat sed pharetra at, consequat et dui. Pellentesque nec urna in lectus imperdiet bibendum non in ante. Sed suscipit 
                tortor eu varius bibendum. Vivamus vitae finibus purus, eu congue odio. Ut at sollicitudin lacus, sed faucibus nibh. Nullam lobortis id lorem vitae pulvinar. Donec quis cursus nunc. Cras odio elit, luctus eget egestas quis, condimentum sed diam. Vestibulum vestibulum magna in ex pretium accumsan.
                                
                Morbi quam lorem, sagittis sed erat eu, tempor feugiat leo. Maecenas rhoncus libero dolor, a facilisis velit blandit sed metus.
                """;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'description' must be between 3 and 4000 characters";


        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        final var actualError = Assertions.assertThrows(DomainException.class,
                () -> actualVideo.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }
    @Test
    public void givenNullLaunchedAt_whenCallValidate_shouldReceiveAnError(){
        final var expectedTitle = "System Design Interview";
        final var expectedDescription =  """
            Lets design the high-level architecture of youtube - similar to 
            how we'd tackle this in a system design interview.
            """;
        final Year expectedLaunchedAt = null;
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'launchedAt' should not be null";


        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        final var actualError = Assertions.assertThrows(DomainException.class,
                () -> actualVideo.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenNullRating_whenCallValidate_shouldReceiveAnError(){
        final var expectedTitle = "System Design Interview";
        final var expectedDescription =  """
            Lets design the high-level architecture of youtube - similar to 
            how we'd tackle this in a system design interview.
            """;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final Rating expectedRating = null;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'rating' should not be null";


        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        final var actualError = Assertions.assertThrows(DomainException.class,
                () -> actualVideo.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }


}
