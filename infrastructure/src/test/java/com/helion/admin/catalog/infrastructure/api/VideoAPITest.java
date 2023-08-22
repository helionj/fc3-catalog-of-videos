package com.helion.admin.catalog.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.helion.admin.catalog.ApiTest;
import com.helion.admin.catalog.ControllerTest;
import com.helion.admin.catalog.application.video.create.CreateVideoCommand;
import com.helion.admin.catalog.application.video.create.CreateVideoOutput;
import com.helion.admin.catalog.application.video.create.CreateVideoUseCase;
import com.helion.admin.catalog.application.video.delete.DeleteVideoUseCase;
import com.helion.admin.catalog.application.video.media.get.GetMediaCommand;
import com.helion.admin.catalog.application.video.media.get.GetMediaUseCase;
import com.helion.admin.catalog.application.video.media.get.MediaOutput;
import com.helion.admin.catalog.application.video.media.upload.UploadMediaCommand;
import com.helion.admin.catalog.application.video.media.upload.UploadMediaOutput;
import com.helion.admin.catalog.application.video.media.upload.UploadMediaUseCase;
import com.helion.admin.catalog.application.video.retrieve.get.GetVideoByIdUseCase;
import com.helion.admin.catalog.application.video.retrieve.get.VideoOutput;
import com.helion.admin.catalog.application.video.retrieve.list.ListVideosUseCase;
import com.helion.admin.catalog.application.video.retrieve.list.VideoListOutput;
import com.helion.admin.catalog.application.video.update.UpdateVideoCommand;
import com.helion.admin.catalog.application.video.update.UpdateVideoOutput;
import com.helion.admin.catalog.application.video.update.UpdateVideoUseCase;
import com.helion.admin.catalog.domain.Fixture;
import com.helion.admin.catalog.domain.castmember.CastMemberID;
import com.helion.admin.catalog.domain.category.CategoryID;
import com.helion.admin.catalog.domain.category.pagination.Pagination;
import com.helion.admin.catalog.domain.exceptions.NotFoundException;
import com.helion.admin.catalog.domain.exceptions.NotificationException;
import com.helion.admin.catalog.domain.genre.GenreID;
import com.helion.admin.catalog.domain.utils.CollectionUtils;
import com.helion.admin.catalog.domain.validation.Error;
import com.helion.admin.catalog.domain.video.*;
import com.helion.admin.catalog.infrastructure.api.video.VideoAPI;
import com.helion.admin.catalog.infrastructure.video.models.CreateVideoRequest;
import com.helion.admin.catalog.infrastructure.video.models.UpdateVideoRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = VideoAPI.class)
public class VideoAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateVideoUseCase createVideoUseCase;

    @MockBean
    private UpdateVideoUseCase updateVideoUseCase;

    @MockBean
    private GetVideoByIdUseCase getVideoByIdUseCase;

    @MockBean
    private DeleteVideoUseCase deleteVideoUseCase;

    @MockBean
    private ListVideosUseCase listVideosUseCase;

    @MockBean
    private GetMediaUseCase getMediaUseCase;

    @MockBean
    private UploadMediaUseCase uploadMediaUseCase;

    @Test
    public void givenAValidCommand_whenCallsCreateFull_shouldReturnAnId() throws Exception {

        final var wesley =  Fixture.CastMembers.wesley();
        final var tech = Fixture.Genres.tech();
        final var aulas = Fixture.Categories.aulas();
        final var expectedId = VideoID.unique();
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(aulas.getId().getValue());
        final var expectedGenres = Set.of(tech.getId().getValue());
        final var expectedMembers = Set.of(wesley.getId().getValue());

        final var expectedVideo =
                new MockMultipartFile("video_file", "video.mp4", "video/mp4", "VIDEO".getBytes());

        final var expectedTrailer =
                new MockMultipartFile("trailer_file", "trailer.mp4", "video/mp4", "TRAILER".getBytes());
        final var expectedBanner =
                new MockMultipartFile("banner_file", "banner.jpg", "image/jpg", "BANNER".getBytes());
        final var expectedThumb =
                new MockMultipartFile("thumbnail_file", "thumbnail.jpg", "image/jpg", "THUMBNAIL".getBytes());
        final var expectedThumbHalf =
                new MockMultipartFile("thumbnail_half_file", "thumbnailHalf.jpg", "image/jpg", "THUMBNAIL-HALF".getBytes());

        Mockito.when(createVideoUseCase.execute(any()))
                .thenReturn(new CreateVideoOutput(expectedId.getValue()));

        final var aRequest = MockMvcRequestBuilders.multipart("/videos")
                .file(expectedVideo)
                .file(expectedTrailer)
                .file(expectedBanner)
                .file(expectedThumb)
                .file(expectedThumbHalf)
                .with(ApiTest.VIDEOS_JWT)
                .param("title", expectedTitle)
                .param("description", expectedDescription)
                .param("year_launched", String.valueOf(expectedLaunchedAt.getValue()))
                .param("duration", String.valueOf(expectedDuration))
                .param("opened", String.valueOf(expectedOpened))
                .param("published", String.valueOf(expectedPublished))
                .param("rating", expectedRating.getName())
                .param("cast_members_id", wesley.getId().getValue())
                .param("categories_id", aulas.getId().getValue())
                .param("genres_id", tech.getId().getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA);

        this.mvc.perform(aRequest).andExpect(status()
                .isCreated())
                .andExpect(header().string("Location", "/videos/"+expectedId.getValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId.getValue())));

        final var cmdCaptor = ArgumentCaptor.forClass(CreateVideoCommand.class);

        verify(createVideoUseCase).execute(cmdCaptor.capture());

        final var actualCmd = cmdCaptor.getValue();

        Assertions.assertEquals(expectedTitle, actualCmd.title());
        Assertions.assertEquals(expectedDescription, actualCmd.description());
        Assertions.assertEquals(expectedLaunchedAt.getValue(), actualCmd.launchedAt());
        Assertions.assertEquals(expectedDuration, actualCmd.duration());
        Assertions.assertEquals(expectedOpened, actualCmd.opened());
        Assertions.assertEquals(expectedPublished, actualCmd.published());
        Assertions.assertEquals(expectedRating.getName(), actualCmd.rating());
        Assertions.assertEquals(expectedCategories, actualCmd.categories());
        Assertions.assertEquals(expectedGenres, actualCmd.genres());
        Assertions.assertEquals(expectedMembers, actualCmd.castMembers());
        Assertions.assertEquals(expectedVideo.getOriginalFilename(), actualCmd.video().name());
        Assertions.assertEquals(expectedTrailer.getOriginalFilename(), actualCmd.trailer().name());
        Assertions.assertEquals(expectedBanner.getOriginalFilename(), actualCmd.banner().name());
        Assertions.assertEquals(expectedThumb.getOriginalFilename(), actualCmd.thumbnail().name());
        Assertions.assertEquals(expectedThumbHalf.getOriginalFilename(), actualCmd.thumbnailHalf().name());

    }
    @Test
    public void givenAnInValidCommand_whenCallsCreateFull_shouldReturnError() throws Exception {

        final var expectedErrorMessage = "'title' is required";



        Mockito.when(createVideoUseCase.execute(any()))
                .thenThrow(NotificationException.with(new Error(expectedErrorMessage)));

        final var aRequest = MockMvcRequestBuilders.multipart("/videos")
                .with(ApiTest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA);

        this.mvc.perform(aRequest).andExpect(status()
                        .isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));


    }
    @Test
    public void givenAValidCommand_whenCallsCreatePartial_shouldReturnAnId() throws Exception {

        final var wesley =  Fixture.CastMembers.wesley();
        final var tech = Fixture.Genres.tech();
        final var aulas = Fixture.Categories.aulas();
        final var expectedId = VideoID.unique();
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(aulas.getId().getValue());
        final var expectedGenres = Set.of(tech.getId().getValue());
        final var expectedMembers = Set.of(wesley.getId().getValue());

        final var aCmd = new CreateVideoRequest(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        Mockito.when(createVideoUseCase.execute(any()))
                .thenReturn(new CreateVideoOutput(expectedId.getValue()));

        final var aRequest = post("/videos")
                .with(ApiTest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCmd));


        this.mvc.perform(aRequest).andExpect(status()
                        .isCreated())
                .andExpect(header().string("Location", "/videos/"+expectedId.getValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId.getValue())));

        final var cmdCaptor = ArgumentCaptor.forClass(CreateVideoCommand.class);

        verify(createVideoUseCase).execute(cmdCaptor.capture());

        final var actualCmd = cmdCaptor.getValue();

        Assertions.assertEquals(expectedTitle, actualCmd.title());
        Assertions.assertEquals(expectedDescription, actualCmd.description());
        Assertions.assertEquals(expectedLaunchedAt.getValue(), actualCmd.launchedAt());
        Assertions.assertEquals(expectedDuration, actualCmd.duration());
        Assertions.assertEquals(expectedOpened, actualCmd.opened());
        Assertions.assertEquals(expectedPublished, actualCmd.published());
        Assertions.assertEquals(expectedRating.getName(), actualCmd.rating());
        Assertions.assertEquals(expectedCategories, actualCmd.categories());
        Assertions.assertEquals(expectedGenres, actualCmd.genres());
        Assertions.assertEquals(expectedMembers, actualCmd.castMembers());
        Assertions.assertTrue(actualCmd.getVideo().isEmpty());
        Assertions.assertTrue(actualCmd.getTrailer().isEmpty());
        Assertions.assertTrue(actualCmd.getBanner().isEmpty());
        Assertions.assertTrue(actualCmd.getThumbnail().isEmpty());
        Assertions.assertTrue(actualCmd.getThumbnailHalf().isEmpty());

    }
    @Test
    public void givenAnInValidCommand_whenCallsCreatePartial_shouldReturnError() throws Exception {

        final var expectedErrorMessage = "'title' is required";

        Mockito.when(createVideoUseCase.execute(any()))
                .thenThrow(NotificationException.with(new Error(expectedErrorMessage)));

        final var aRequest = post("/videos")
                .with(ApiTest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                                "title": "any"
                            }
                        """);


        this.mvc.perform(aRequest).andExpect(status()
                        .isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));
    }

    @Test
    public void givenAnEmptyBody_whenCallsCreatePartial_shouldReturnError() throws Exception {


        final var aRequest = post("/videos")
                .with(ApiTest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);


        this.mvc.perform(aRequest).andExpect(status()
                        .isBadRequest());
    }

    @Test
    public void givenAValidId_whenCallsGetsById_shouldReturnVideo() throws Exception {
        final var wesley =  Fixture.CastMembers.wesley();
        final var tech = Fixture.Genres.tech();
        final var aulas = Fixture.Categories.aulas();
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(aulas.getId().getValue());
        final var expectedGenres = Set.of(tech.getId().getValue());
        final var expectedMembers = Set.of(wesley.getId().getValue());

        final var expectedVideo = Fixture.Videos.audioVideo(VideoMediaType.VIDEO);
        final var expectedTrailer = Fixture.Videos.audioVideo(VideoMediaType.TRAILER);
        final var expectedBanner = Fixture.Videos.image(VideoMediaType.BANNER);
        final var expectedThumbnail = Fixture.Videos.image(VideoMediaType.THUMBNAIL);
        final var expectedThumbnailHalf = Fixture.Videos.image(VideoMediaType.THUMBNAIL_HALF);

        final var aVideo =Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                CollectionUtils.mapTo(expectedCategories, CategoryID::from),
                CollectionUtils.mapTo(expectedGenres, GenreID::from),
                CollectionUtils.mapTo(expectedMembers, CastMemberID::from)
        )
                .updateVideoMedia(expectedVideo)
                .updateTrailerMedia(expectedTrailer)
                .updateBannerMedia(expectedBanner)
                .updateThumbnailMedia(expectedThumbnail)
                .updateThumbnailHalfMedia(expectedThumbnailHalf);

        final var expectedId = aVideo.getId().getValue();

        when(getVideoByIdUseCase.execute(any()))
                .thenReturn(VideoOutput.from(aVideo));

        final var aRequest = MockMvcRequestBuilders.get("/videos/{i}", expectedId)
                .with(ApiTest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(aRequest);

        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)))
                .andExpect(jsonPath("$.title", equalTo(expectedTitle)))
                .andExpect(jsonPath("$.description", equalTo(expectedDescription)))
                .andExpect(jsonPath("$.year_launched", equalTo(expectedLaunchedAt.getValue())))
                .andExpect(jsonPath("$.duration", equalTo(expectedDuration)))
                .andExpect(jsonPath("$.opened", equalTo(expectedOpened)))
                .andExpect(jsonPath("$.published", equalTo(expectedPublished)))
                .andExpect(jsonPath("$.rating", equalTo(expectedRating.getName())))
                .andExpect(jsonPath("$.created_at", equalTo(aVideo.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updated_at", equalTo(aVideo.getUpdatedAt().toString())))
                .andExpect(jsonPath("$.banner.id", equalTo(expectedBanner.id())))
                .andExpect(jsonPath("$.banner.name", equalTo(expectedBanner.name())))
                .andExpect(jsonPath("$.banner.location", equalTo(expectedBanner.location())))
                .andExpect(jsonPath("$.banner.checksum", equalTo(expectedBanner.checksum())))
                .andExpect(jsonPath("$.thumbnail.id", equalTo(expectedThumbnail.id())))
                .andExpect(jsonPath("$.thumbnail.name", equalTo(expectedThumbnail.name())))
                .andExpect(jsonPath("$.thumbnail.location", equalTo(expectedThumbnail.location())))
                .andExpect(jsonPath("$.thumbnail.checksum", equalTo(expectedThumbnail.checksum())))
                .andExpect(jsonPath("$.thumbnail_half.id", equalTo(expectedThumbnailHalf.id())))
                .andExpect(jsonPath("$.thumbnail_half.name", equalTo(expectedThumbnailHalf.name())))
                .andExpect(jsonPath("$.thumbnail_half.location", equalTo(expectedThumbnailHalf.location())))
                .andExpect(jsonPath("$.thumbnail_half.checksum", equalTo(expectedThumbnailHalf.checksum())))
                .andExpect(jsonPath("$.video.id", equalTo(expectedVideo.id())))
                .andExpect(jsonPath("$.video.name", equalTo(expectedVideo.name())))
                .andExpect(jsonPath("$.video.checksum", equalTo(expectedVideo.checksum())))
                .andExpect(jsonPath("$.video.location", equalTo(expectedVideo.rawLocation())))
                .andExpect(jsonPath("$.video.encoded_location", equalTo(expectedVideo.encodedLocation())))
                .andExpect(jsonPath("$.video.status", equalTo(expectedVideo.status().name())))
                .andExpect(jsonPath("$.trailer.id", equalTo(expectedTrailer.id())))
                .andExpect(jsonPath("$.trailer.name", equalTo(expectedTrailer.name())))
                .andExpect(jsonPath("$.trailer.checksum", equalTo(expectedTrailer.checksum())))
                .andExpect(jsonPath("$.trailer.location", equalTo(expectedTrailer.rawLocation())))
                .andExpect(jsonPath("$.trailer.encoded_location", equalTo(expectedTrailer.encodedLocation())))
                .andExpect(jsonPath("$.trailer.status", equalTo(expectedTrailer.status().name())))
                .andExpect(jsonPath("$.categories_id", equalTo(new ArrayList(expectedCategories))))
                .andExpect(jsonPath("$.genres_id", equalTo(new ArrayList(expectedGenres))))
                .andExpect(jsonPath("$.cast_members_id", equalTo(new ArrayList(expectedMembers))));
    }

    @Test
    public void givenAnInValidId_whenCallsGetsById_shouldReturnNotFound() throws Exception {

        final var expectedId = VideoID.unique();
        final var expectedErrorMessage = "Video with ID %s was not found".formatted(expectedId.getValue());

        when(getVideoByIdUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Video.class, expectedId));

        final var aRequest = MockMvcRequestBuilders.get("/videos/{id}", expectedId)
                .with(ApiTest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(aRequest);

        response.andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

    }

    @Test
    public void givenAValidCommand_whenCallsUpdateVideo_shouldReturnUpdatedId() throws Exception {

        final var wesley =  Fixture.CastMembers.wesley();
        final var tech = Fixture.Genres.tech();
        final var aulas = Fixture.Categories.aulas();
        final var expectedId = VideoID.unique();
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(aulas.getId().getValue());
        final var expectedGenres = Set.of(tech.getId().getValue());
        final var expectedMembers = Set.of(wesley.getId().getValue());

        final var aCmd = new UpdateVideoRequest(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        Mockito.when(updateVideoUseCase.execute(any()))
                .thenReturn(new UpdateVideoOutput(expectedId.getValue()));

        final var aRequest = put("/videos/{id}", expectedId.getValue())
                .with(ApiTest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCmd));


        this.mvc.perform(aRequest).andExpect(status()
                        .isOk())
                .andExpect(header().string("Location", "/videos/"+expectedId.getValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId.getValue())));

        final var cmdCaptor = ArgumentCaptor.forClass(UpdateVideoCommand.class);

        verify(updateVideoUseCase).execute(cmdCaptor.capture());

        final var actualCmd = cmdCaptor.getValue();

        Assertions.assertEquals(expectedTitle, actualCmd.title());
        Assertions.assertEquals(expectedDescription, actualCmd.description());
        Assertions.assertEquals(expectedLaunchedAt.getValue(), actualCmd.launchedAt());
        Assertions.assertEquals(expectedDuration, actualCmd.duration());
        Assertions.assertEquals(expectedOpened, actualCmd.opened());
        Assertions.assertEquals(expectedPublished, actualCmd.published());
        Assertions.assertEquals(expectedRating.getName(), actualCmd.rating());
        Assertions.assertEquals(expectedCategories, actualCmd.categories());
        Assertions.assertEquals(expectedGenres, actualCmd.genres());
        Assertions.assertEquals(expectedMembers, actualCmd.castMembers());
        Assertions.assertTrue(actualCmd.getVideo().isEmpty());
        Assertions.assertTrue(actualCmd.getTrailer().isEmpty());
        Assertions.assertTrue(actualCmd.getBanner().isEmpty());
        Assertions.assertTrue(actualCmd.getThumbnail().isEmpty());
        Assertions.assertTrue(actualCmd.getThumbnailHalf().isEmpty());

    }

    @Test
    public void givenAnInValidCommand_whenCallsUpdateVideo_shouldReturnNotification() throws Exception {

        final var wesley =  Fixture.CastMembers.wesley();
        final var tech = Fixture.Genres.tech();
        final var aulas = Fixture.Categories.aulas();
        final var expectedId = VideoID.unique();
        final var expectedTitle = "";
        final var expectedErrorMessage = "'title' should not be empty";
        final var expectedErrorCount = 1;
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(aulas.getId().getValue());
        final var expectedGenres = Set.of(tech.getId().getValue());
        final var expectedMembers = Set.of(wesley.getId().getValue());

        final var aCmd = new UpdateVideoRequest(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        when(updateVideoUseCase.execute(any()))
                .thenThrow(NotificationException.with(new Error(expectedErrorMessage)));

        final var aRequest = put("/videos/{id}", expectedId.getValue())
                .with(ApiTest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCmd));


        final var aResponse = this.mvc.perform(aRequest);

        aResponse.andExpect(status()
                .isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)))
                .andExpect(jsonPath("$.errors", hasSize(expectedErrorCount)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(updateVideoUseCase).execute(any());
    }

    @Test
    public void givenAValidId_whenCallsDeleteById_whenCallsDeleteById_shouldDeleteIt() throws Exception {

        final var expectedId = VideoID.unique();

        doNothing().when(deleteVideoUseCase).execute(any());

        final var aRequest = delete("/videos/{id}", expectedId.getValue())
                .with(ApiTest.VIDEOS_JWT);

        final var response = this.mvc.perform(aRequest);

        response.andExpect(status().isNoContent());
        verify(deleteVideoUseCase).execute(eq(expectedId.getValue()));
    }

    @Test
    public void givenValidParams_whenCallsListVideo_shouldReturnPagination() throws Exception {

        final var aVideo = new VideoPreview(Fixture.video());


        final var expectedPage = 50;
        final var expectedPerPage = 50;
        final var expectedTerms = "Algo";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedMembers = "cast1";
        final var expectedGenres = "genre1";
        final var expectedCategories = "cat1";
        final var expectedItemsCount = 1;
        final var expectedTotal = 1;
        final var expectedItems = List.of(VideoListOutput.from(aVideo));

        when(listVideosUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage,expectedTotal, expectedItems));

        final var aRequest = get("/videos")
                .with(ApiTest.VIDEOS_JWT)
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .queryParam("search", expectedTerms)
                .queryParam("categories_ids", expectedCategories)
                .queryParam("genres_ids", expectedGenres)
                .queryParam("cast_members_ids", expectedMembers)
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(aRequest);

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
                .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
                .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
                .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
                .andExpect(jsonPath("$.items[0].id", equalTo(aVideo.id())))
                .andExpect(jsonPath("$.items[0].title", equalTo(aVideo.title())))
                .andExpect(jsonPath("$.items[0].description", equalTo(aVideo.description())))
                .andExpect(jsonPath("$.items[0].created_at", equalTo(aVideo.createdAt().toString())))
                .andExpect(jsonPath("$.items[0].updated_at", equalTo(aVideo.updatedAt().toString())));

        final var captor = ArgumentCaptor.forClass(VideoSearchQuery.class);
        verify(listVideosUseCase).execute(captor.capture());

        final var actualQuery = captor.getValue();

        Assertions.assertEquals(expectedPage, actualQuery.page());
        Assertions.assertEquals(expectedPerPage, actualQuery.perPage());
        Assertions.assertEquals(expectedDirection, actualQuery.direction());
        Assertions.assertEquals(expectedSort, actualQuery.sort());
        Assertions.assertEquals(expectedTerms, actualQuery.terms());
        Assertions.assertEquals(Set.of(CategoryID.from(expectedCategories)), actualQuery.categories());
        Assertions.assertEquals(Set.of(GenreID.from(expectedGenres)), actualQuery.genres());
        Assertions.assertEquals(Set.of(CastMemberID.from(expectedMembers)), actualQuery.castMembers());

    }

    @Test
    public void givenEmptyParams_whenCallsListVideoWithDefaultValues_shouldReturnPagination() throws Exception {

        final var aVideo = new VideoPreview(Fixture.video());


        final var expectedPage = 0;
        final var expectedPerPage = 25;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 1;
        final var expectedTotal = 1;
        final var expectedItems = List.of(VideoListOutput.from(aVideo));

        when(listVideosUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage,expectedTotal, expectedItems));

        final var aRequest = get("/videos")
                .with(ApiTest.VIDEOS_JWT)
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(aRequest);

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
                .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
                .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
                .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
                .andExpect(jsonPath("$.items[0].id", equalTo(aVideo.id())))
                .andExpect(jsonPath("$.items[0].title", equalTo(aVideo.title())))
                .andExpect(jsonPath("$.items[0].description", equalTo(aVideo.description())))
                .andExpect(jsonPath("$.items[0].created_at", equalTo(aVideo.createdAt().toString())))
                .andExpect(jsonPath("$.items[0].updated_at", equalTo(aVideo.updatedAt().toString())));

        final var captor = ArgumentCaptor.forClass(VideoSearchQuery.class);
        verify(listVideosUseCase).execute(captor.capture());

        final var actualQuery = captor.getValue();

        Assertions.assertEquals(expectedPage, actualQuery.page());
        Assertions.assertEquals(expectedPerPage, actualQuery.perPage());
        Assertions.assertEquals(expectedDirection, actualQuery.direction());
        Assertions.assertEquals(expectedSort, actualQuery.sort());
        Assertions.assertEquals(expectedTerms, actualQuery.terms());
        Assertions.assertTrue(actualQuery.categories().isEmpty());
        Assertions.assertTrue(actualQuery.genres().isEmpty());
        Assertions.assertTrue(actualQuery.castMembers().isEmpty());

    }

    @Test
    public void givenAValidVideoIdAndFileType_whenCallsGetMediaById_shouldReturnContent() throws Exception {

        final var expectedVideo = Fixture.Videos.systemDesign();
        final var expectedId = expectedVideo.getId();

        final var expectedType = VideoMediaType.VIDEO;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedMedia = new MediaOutput(expectedResource.content(), expectedResource.contentType(), expectedResource.name());
        when(getMediaUseCase.execute(any()))
                .thenReturn(expectedMedia);

        final var aRequest =
                get("/videos/{id}/medias/{type}", expectedId.getValue(), expectedType.name())
                        .with(ApiTest.VIDEOS_JWT);

        final var response = this.mvc.perform(aRequest);

        response.andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, expectedMedia.contentType()))
                .andExpect(header().string(HttpHeaders.CONTENT_LENGTH, String.valueOf(expectedMedia.content().length)))
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=%s".formatted(expectedMedia.name())))
                .andExpect(content().bytes(expectedMedia.content()));

        final var captor = ArgumentCaptor.forClass(GetMediaCommand.class);

        verify(this.getMediaUseCase).execute(captor.capture());

        final var actualCommand = captor.getValue();

        Assertions.assertEquals(expectedId.getValue(), actualCommand.videoId());
        Assertions.assertEquals(expectedType.name(), actualCommand.mediaType());
    }

    @Test
    public void givenAValidVideoIdAndFile_whenCallsUploadMedia_shouldStoreIt() throws Exception {

        final var expectedId = VideoID.unique();
        final var expectedType = VideoMediaType.VIDEO;
        final var expectedResource = Fixture.Videos.resource(expectedType);

        final var expectedVideo =
                new MockMultipartFile("media_file", expectedResource.name(), expectedResource.contentType(), expectedResource.content());

        when(uploadMediaUseCase.execute(any()))
                .thenReturn(new UploadMediaOutput(expectedId.getValue(),expectedType));

        final var aRequest =
                multipart("/videos/{id}/medias/{type}", expectedId.getValue(), expectedType.name())
                        .file(expectedVideo)
                        .with(ApiTest.VIDEOS_JWT)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.MULTIPART_FORM_DATA);

        final var response = this.mvc.perform(aRequest);

        response.andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "/videos/%s/medias/%s".formatted(expectedId.getValue(), expectedType.name())))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.video_id", equalTo(expectedId.getValue())))
                .andExpect(jsonPath("$.media_type", equalTo(expectedType.name())));

        final var captor = ArgumentCaptor.forClass(UploadMediaCommand.class);

        verify(this.uploadMediaUseCase).execute(captor.capture());

        final var actualCommand = captor.getValue();

        Assertions.assertEquals(expectedId.getValue(), actualCommand.videoId());
        Assertions.assertEquals(expectedResource.content(), actualCommand.videoResource().getResource().content());
        Assertions.assertEquals(expectedResource.name(), actualCommand.videoResource().getResource().name());
        Assertions.assertEquals(expectedResource.contentType(), actualCommand.videoResource().getResource().contentType());
        Assertions.assertEquals(expectedType, actualCommand.videoResource().getType());

    }

    @Test
    public void givenAnInValidMediaType_whenCallsUploadMedia_shouldReturnError() throws Exception {

        final var expectedId = VideoID.unique();
        final var expectedType = "FAKE";
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.VIDEO);

        final var expectedVideo =
                new MockMultipartFile("media_file", expectedResource.name(), expectedResource.contentType(), expectedResource.content());


        final var aRequest =
                multipart("/videos/{id}/medias/{type}", expectedId.getValue(), expectedType)
                        .file(expectedVideo)
                        .with(ApiTest.VIDEOS_JWT)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.MULTIPART_FORM_DATA);

        final var response = this.mvc.perform(aRequest);

        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo("Invalid %s for VideoMediaType".formatted(expectedType))));



    }
}
