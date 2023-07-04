package com.helion.admin.catalog.infrastructure.video;

import com.helion.admin.catalog.IntegrationTest;
import com.helion.admin.catalog.domain.Fixture;
import com.helion.admin.catalog.domain.castmember.CastMember;
import com.helion.admin.catalog.domain.castmember.CastMemberGateway;
import com.helion.admin.catalog.domain.castmember.CastMemberID;
import com.helion.admin.catalog.domain.category.Category;
import com.helion.admin.catalog.domain.category.CategoryGateway;
import com.helion.admin.catalog.domain.category.CategoryID;
import com.helion.admin.catalog.domain.genre.Genre;
import com.helion.admin.catalog.domain.genre.GenreGateway;
import com.helion.admin.catalog.domain.genre.GenreID;
import com.helion.admin.catalog.domain.video.*;
import com.helion.admin.catalog.infrastructure.video.persistence.VideoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.Set;



@IntegrationTest
public class DefaultVideoGatewayTest {

    @Autowired
    private DefaultVideoGateway videoGateway;

    @Autowired
    private CastMemberGateway castMemberGateway;

    @Autowired
    private CategoryGateway categoryGateway;

    @Autowired
    private GenreGateway genreGateway;

    @Autowired
    private VideoRepository videoRepository;

    private CastMember wesley;
    private CastMember helion;
    private Category aulas;
    private Category lives;
    private Genre tech;
    private Genre business;

    @BeforeEach
    private void setUp(){
        wesley = castMemberGateway.create(Fixture.CastMembers.wesley());
        helion = castMemberGateway.create(Fixture.CastMembers.helion());
        aulas = categoryGateway.create(Fixture.Categories.aulas());
        lives = categoryGateway.create(Fixture.Categories.lives());
        tech = genreGateway.create(Fixture.Genres.tech());
        business = genreGateway.create(Fixture.Genres.business());
    }

    @Test
    public void testInjection(){
        Assertions.assertNotNull(videoGateway);
        Assertions.assertNotNull(castMemberGateway);
        Assertions.assertNotNull(categoryGateway);
        Assertions.assertNotNull(genreGateway);
        Assertions.assertNotNull(videoRepository);
    }

    @Test
    @Transactional
    public void givenAValidVideo_whenCallCreate_shouldPersistIt(){

        final var wesley =castMemberGateway.create(Fixture.CastMembers.wesley());
        final var aulas = categoryGateway.create(Fixture.Categories.aulas());
        final var tech = genreGateway.create(Fixture.Genres.tech());

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(aulas.getId());
        final var expectedGenres = Set.of(tech.getId());
        final var expectedMembers = Set.of(wesley.getId());

        final AudioVideoMedia expectedVideo = AudioVideoMedia.with("123", "video", "/media/video");
        final AudioVideoMedia expectedTrailer = AudioVideoMedia.with("123", "trailer", "/media/trailer");
        final ImageMedia expectedBanner = ImageMedia.with("123", "banner","/media/banner");
        final ImageMedia expectedThumb = ImageMedia.with("123", "thumbnail","/media/thumbnail");
        final ImageMedia expectedThumbHalf = ImageMedia.with("123", "thumbhalf","/media/thumbhalf");

        final var aVideo = Video.newVideo(
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
                )
                .setVideo(expectedVideo)
                .setTrailer(expectedTrailer)
                .setBanner(expectedBanner)
                .setThumbnail(expectedThumb)
                .setThumbnailHalf(expectedThumbHalf);




        final var actualVideo = videoGateway.create(aVideo);

        Assertions.assertNotNull(actualVideo);
        Assertions.assertNotNull(actualVideo.getId());
        Assertions.assertEquals(expectedTitle,actualVideo.getTitle());
        Assertions.assertEquals(expectedDescription,actualVideo.getDescription());
        Assertions.assertEquals(expectedLaunchedAt,actualVideo.getLaunchedAt());
        Assertions.assertEquals(expectedDuration,actualVideo.getDuration());
        Assertions.assertEquals(expectedOpened,actualVideo.isOpened());
        Assertions.assertEquals(expectedPublished,actualVideo.isPublished());
        Assertions.assertEquals(expectedRating,actualVideo.getRating());
        Assertions.assertEquals(expectedCategories,actualVideo.getCategories());
        Assertions.assertEquals(expectedGenres,actualVideo.getGenres());
        Assertions.assertEquals(expectedMembers,actualVideo.getCastMembers());
        Assertions.assertEquals(expectedVideo.name(), actualVideo.getVideo().get().name());
        Assertions.assertEquals(expectedTrailer.name(), actualVideo.getTrailer().get().name());
        Assertions.assertEquals(expectedBanner.name(), actualVideo.getBanner().get().name());
        Assertions.assertEquals(expectedThumb.name(), actualVideo.getThumbnail().get().name());
        Assertions.assertEquals(expectedThumbHalf.name(), actualVideo.getThumbnailHalf().get().name());

        final var persistedVideo = videoRepository.findById(aVideo.getId().getValue()).get();

        Assertions.assertNotNull(persistedVideo);
        Assertions.assertNotNull(persistedVideo.getId());
        Assertions.assertEquals(expectedTitle,persistedVideo.getTitle());
        Assertions.assertEquals(expectedDescription,persistedVideo.getDescription());
        Assertions.assertEquals(expectedLaunchedAt,Year.of(persistedVideo.getYearLaunched()));
        Assertions.assertEquals(expectedDuration,persistedVideo.getDuration());
        Assertions.assertEquals(expectedOpened,persistedVideo.isOpened());
        Assertions.assertEquals(expectedPublished,persistedVideo.isPublished());
        Assertions.assertEquals(expectedRating,persistedVideo.getRating());
        Assertions.assertEquals(expectedCategories,persistedVideo.getCategoriesID());
        Assertions.assertEquals(expectedGenres,persistedVideo.getGenresID());
        Assertions.assertEquals(expectedMembers,persistedVideo.getCastMembersID());
        Assertions.assertEquals(expectedVideo.name(), persistedVideo.getVideo().getName());
        Assertions.assertEquals(expectedTrailer.name(), persistedVideo.getTrailer().getName());
        Assertions.assertEquals(expectedBanner.name(), persistedVideo.getBanner().getName());
        Assertions.assertEquals(expectedThumb.name(), persistedVideo.getThumbnail().getName());
        Assertions.assertEquals(expectedThumbHalf.name(), persistedVideo.getThumbnailHalf().getName());
    }

    @Test
    @Transactional
    public void givenAValidVideoWithoutRelations_whenCallCreate_shouldPersistIt(){

        final var wesley =castMemberGateway.create(Fixture.CastMembers.wesley());
        final var aulas = categoryGateway.create(Fixture.Categories.aulas());
        final var tech = genreGateway.create(Fixture.Genres.tech());

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();



        final var aVideo = Video.newVideo(
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




        final var actualVideo = videoGateway.create(aVideo);

        Assertions.assertNotNull(actualVideo);
        Assertions.assertNotNull(actualVideo.getId());
        Assertions.assertEquals(expectedTitle,actualVideo.getTitle());
        Assertions.assertEquals(expectedDescription,actualVideo.getDescription());
        Assertions.assertEquals(expectedLaunchedAt,actualVideo.getLaunchedAt());
        Assertions.assertEquals(expectedDuration,actualVideo.getDuration());
        Assertions.assertEquals(expectedOpened,actualVideo.isOpened());
        Assertions.assertEquals(expectedPublished,actualVideo.isPublished());
        Assertions.assertEquals(expectedRating,actualVideo.getRating());
        Assertions.assertEquals(expectedCategories,actualVideo.getCategories());
        Assertions.assertEquals(expectedGenres,actualVideo.getGenres());
        Assertions.assertEquals(expectedMembers,actualVideo.getCastMembers());
        Assertions.assertTrue(actualVideo.getVideo().isEmpty());
        Assertions.assertTrue(actualVideo.getTrailer().isEmpty());
        Assertions.assertTrue(actualVideo.getBanner().isEmpty());
        Assertions.assertTrue(actualVideo.getThumbnail().isEmpty());
        Assertions.assertTrue(actualVideo.getThumbnailHalf().isEmpty());

        final var persistedVideo = videoRepository.findById(aVideo.getId().getValue()).get();

        Assertions.assertNotNull(persistedVideo);
        Assertions.assertNotNull(persistedVideo.getId());
        Assertions.assertEquals(expectedTitle,persistedVideo.getTitle());
        Assertions.assertEquals(expectedDescription,persistedVideo.getDescription());
        Assertions.assertEquals(expectedLaunchedAt,Year.of(persistedVideo.getYearLaunched()));
        Assertions.assertEquals(expectedDuration,persistedVideo.getDuration());
        Assertions.assertEquals(expectedOpened,persistedVideo.isOpened());
        Assertions.assertEquals(expectedPublished,persistedVideo.isPublished());
        Assertions.assertEquals(expectedRating,persistedVideo.getRating());
        Assertions.assertEquals(expectedCategories,persistedVideo.getCategoriesID());
        Assertions.assertEquals(expectedGenres,persistedVideo.getGenresID());
        Assertions.assertEquals(expectedMembers,persistedVideo.getCastMembersID());
        Assertions.assertNull(persistedVideo.getVideo());
        Assertions.assertNull(persistedVideo.getTrailer());
        Assertions.assertNull(persistedVideo.getBanner());
        Assertions.assertNull(persistedVideo.getThumbnail());
        Assertions.assertNull(persistedVideo.getThumbnailHalf());
    }

    @Test
    @Transactional
    public void givenAValidVideo_whenCallsUpdate_shouldPersistIt(){

        final var aVideo = videoGateway.create(Video.newVideo(
                Fixture.title(),
                Fixture.Videos.description(),
                Year.of(Fixture.year()),
                Fixture.duration(),
                Fixture.bool(),
                Fixture.bool(),
                Fixture.Videos.rating(),
                Set.of(),
                Set.of(),
                Set.of()
        ));

        final var wesley =castMemberGateway.create(Fixture.CastMembers.wesley());
        final var aulas = categoryGateway.create(Fixture.Categories.aulas());
        final var tech = genreGateway.create(Fixture.Genres.tech());

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(aulas.getId());
        final var expectedGenres = Set.of(tech.getId());
        final var expectedMembers = Set.of(wesley.getId());

        final AudioVideoMedia expectedVideo = AudioVideoMedia.with("123", "video", "/media/video");
        final AudioVideoMedia expectedTrailer = AudioVideoMedia.with("123", "trailer", "/media/trailer");
        final ImageMedia expectedBanner = ImageMedia.with("123", "banner","/media/banner");
        final ImageMedia expectedThumb = ImageMedia.with("123", "thumbnail","/media/thumbnail");
        final ImageMedia expectedThumbHalf = ImageMedia.with("123", "thumbhalf","/media/thumbhalf");

        final var updatedVideo = Video.with(aVideo).update(
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
                )
                .setVideo(expectedVideo)
                .setTrailer(expectedTrailer)
                .setBanner(expectedBanner)
                .setThumbnail(expectedThumb)
                .setThumbnailHalf(expectedThumbHalf);




        final var actualVideo = videoGateway.update(updatedVideo);

        Assertions.assertNotNull(actualVideo);
        Assertions.assertNotNull(actualVideo.getId());
        Assertions.assertEquals(expectedTitle,actualVideo.getTitle());
        Assertions.assertEquals(expectedDescription,actualVideo.getDescription());
        Assertions.assertEquals(expectedLaunchedAt,actualVideo.getLaunchedAt());
        Assertions.assertEquals(expectedDuration,actualVideo.getDuration());
        Assertions.assertEquals(expectedOpened,actualVideo.isOpened());
        Assertions.assertEquals(expectedPublished,actualVideo.isPublished());
        Assertions.assertEquals(expectedRating,actualVideo.getRating());
        Assertions.assertEquals(expectedCategories,actualVideo.getCategories());
        Assertions.assertEquals(expectedGenres,actualVideo.getGenres());
        Assertions.assertEquals(expectedMembers,actualVideo.getCastMembers());
        Assertions.assertEquals(expectedVideo.name(), actualVideo.getVideo().get().name());
        Assertions.assertEquals(expectedTrailer.name(), actualVideo.getTrailer().get().name());
        Assertions.assertEquals(expectedBanner.name(), actualVideo.getBanner().get().name());
        Assertions.assertEquals(expectedThumb.name(), actualVideo.getThumbnail().get().name());
        Assertions.assertEquals(expectedThumbHalf.name(), actualVideo.getThumbnailHalf().get().name());
        Assertions.assertNotNull(actualVideo.getCreatedAt());
        Assertions.assertTrue(actualVideo.getUpdatedAt().isAfter(aVideo.getUpdatedAt()));
        final var persistedVideo = videoRepository.findById(aVideo.getId().getValue()).get();

        Assertions.assertNotNull(persistedVideo);
        Assertions.assertNotNull(persistedVideo.getId());
        Assertions.assertEquals(expectedTitle,persistedVideo.getTitle());
        Assertions.assertEquals(expectedDescription,persistedVideo.getDescription());
        Assertions.assertEquals(expectedLaunchedAt,Year.of(persistedVideo.getYearLaunched()));
        Assertions.assertEquals(expectedDuration,persistedVideo.getDuration());
        Assertions.assertEquals(expectedOpened,persistedVideo.isOpened());
        Assertions.assertEquals(expectedPublished,persistedVideo.isPublished());
        Assertions.assertEquals(expectedRating,persistedVideo.getRating());
        Assertions.assertEquals(expectedCategories,persistedVideo.getCategoriesID());
        Assertions.assertEquals(expectedGenres,persistedVideo.getGenresID());
        Assertions.assertEquals(expectedMembers,persistedVideo.getCastMembersID());
        Assertions.assertEquals(expectedVideo.name(), persistedVideo.getVideo().getName());
        Assertions.assertEquals(expectedTrailer.name(), persistedVideo.getTrailer().getName());
        Assertions.assertEquals(expectedBanner.name(), persistedVideo.getBanner().getName());
        Assertions.assertEquals(expectedThumb.name(), persistedVideo.getThumbnail().getName());
        Assertions.assertEquals(expectedThumbHalf.name(), persistedVideo.getThumbnailHalf().getName());
        Assertions.assertNotNull(persistedVideo.getCreatedAt());
        Assertions.assertTrue(persistedVideo.getUpdatedAt().isAfter(aVideo.getUpdatedAt()));
    }

    @Test
    public void givenAValidVideoID_whenCallsDeleteByID_shouldDeleteIt(){

        final var aVideo = videoGateway.create(Video.newVideo(
                Fixture.title(),
                Fixture.Videos.description(),
                Year.of(Fixture.year()),
                Fixture.duration(),
                Fixture.bool(),
                Fixture.bool(),
                Fixture.Videos.rating(),
                Set.of(),
                Set.of(),
                Set.of()
        ));



        Assertions.assertEquals(1, videoRepository.count());

        final var anId = aVideo.getId();
        videoGateway.deleteById(anId);

        Assertions.assertEquals(0, videoRepository.count());

    }
    @Test
    public void givenAnInValidVideoID_whenCallsDeleteByID_shouldNotDeleteIt(){

        final var aVideo = videoGateway.create(Video.newVideo(
                Fixture.title(),
                Fixture.Videos.description(),
                Year.of(Fixture.year()),
                Fixture.duration(),
                Fixture.bool(),
                Fixture.bool(),
                Fixture.Videos.rating(),
                Set.of(),
                Set.of(),
                Set.of()
        ));



        Assertions.assertEquals(1, videoRepository.count());

        final var anId = VideoID.unique();
        videoGateway.deleteById(anId);

        Assertions.assertEquals(1, videoRepository.count());

    }

    @Test
    public void givenAValidVideoID_whenCallFIndById_shouldReturnIt(){

        final var wesley =castMemberGateway.create(Fixture.CastMembers.wesley());
        final var aulas = categoryGateway.create(Fixture.Categories.aulas());
        final var tech = genreGateway.create(Fixture.Genres.tech());

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(aulas.getId());
        final var expectedGenres = Set.of(tech.getId());
        final var expectedMembers = Set.of(wesley.getId());

        final AudioVideoMedia expectedVideo = AudioVideoMedia.with("123", "video", "/media/video");
        final AudioVideoMedia expectedTrailer = AudioVideoMedia.with("123", "trailer", "/media/trailer");
        final ImageMedia expectedBanner = ImageMedia.with("123", "banner","/media/banner");
        final ImageMedia expectedThumb = ImageMedia.with("123", "thumbnail","/media/thumbnail");
        final ImageMedia expectedThumbHalf = ImageMedia.with("123", "thumbhalf","/media/thumbhalf");

        final var aVideo = videoGateway.create(Video.newVideo(
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
                )
                .setVideo(expectedVideo)
                .setTrailer(expectedTrailer)
                .setBanner(expectedBanner)
                .setThumbnail(expectedThumb)
                .setThumbnailHalf(expectedThumbHalf));

        final var actualVideo = videoGateway.findById(aVideo.getId()).get();

        Assertions.assertNotNull(actualVideo);
        Assertions.assertNotNull(actualVideo.getId());
        Assertions.assertEquals(expectedTitle,actualVideo.getTitle());
        Assertions.assertEquals(expectedDescription,actualVideo.getDescription());
        Assertions.assertEquals(expectedLaunchedAt,actualVideo.getLaunchedAt());
        Assertions.assertEquals(expectedDuration,actualVideo.getDuration());
        Assertions.assertEquals(expectedOpened,actualVideo.isOpened());
        Assertions.assertEquals(expectedPublished,actualVideo.isPublished());
        Assertions.assertEquals(expectedRating,actualVideo.getRating());
        Assertions.assertEquals(expectedCategories,actualVideo.getCategories());
        Assertions.assertEquals(expectedGenres,actualVideo.getGenres());
        Assertions.assertEquals(expectedMembers,actualVideo.getCastMembers());
        Assertions.assertEquals(expectedVideo.name(), actualVideo.getVideo().get().name());
        Assertions.assertEquals(expectedTrailer.name(), actualVideo.getTrailer().get().name());
        Assertions.assertEquals(expectedBanner.name(), actualVideo.getBanner().get().name());
        Assertions.assertEquals(expectedThumb.name(), actualVideo.getThumbnail().get().name());
        Assertions.assertEquals(expectedThumbHalf.name(), actualVideo.getThumbnailHalf().get().name());

    }

    @Test
    public void givenAnInValidVideoID_whenCallFIndById_shouldReturnEmpty(){

        final var wesley =castMemberGateway.create(Fixture.CastMembers.wesley());
        final var aulas = categoryGateway.create(Fixture.Categories.aulas());
        final var tech = genreGateway.create(Fixture.Genres.tech());

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(aulas.getId());
        final var expectedGenres = Set.of(tech.getId());
        final var expectedMembers = Set.of(wesley.getId());

        final AudioVideoMedia expectedVideo = AudioVideoMedia.with("123", "video", "/media/video");
        final AudioVideoMedia expectedTrailer = AudioVideoMedia.with("123", "trailer", "/media/trailer");
        final ImageMedia expectedBanner = ImageMedia.with("123", "banner","/media/banner");
        final ImageMedia expectedThumb = ImageMedia.with("123", "thumbnail","/media/thumbnail");
        final ImageMedia expectedThumbHalf = ImageMedia.with("123", "thumbhalf","/media/thumbhalf");

        final var aVideo = videoGateway.create(Video.newVideo(
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
                )
                .setVideo(expectedVideo)
                .setTrailer(expectedTrailer)
                .setBanner(expectedBanner)
                .setThumbnail(expectedThumb)
                .setThumbnailHalf(expectedThumbHalf));

        final var anId = VideoID.unique();
        final var actualVideo = videoGateway.findById(anId);

        Assertions.assertTrue(actualVideo.isEmpty());

    }

    @Test
    public void givenEmptyParams_whenCallFindAll_shouldReturnAll(){
        mockVideos();
        final var expectedPage=0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 4;

        final var aQuery =
                new VideoSearchQuery(
                        expectedPage,
                        expectedPerPage,
                        expectedTerms,
                        expectedSort,
                        expectedDirection,
                        Set.of(),
                        Set.of(),
                        Set.of()
                );

        final var actualPage = videoGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedTotal, actualPage.items().size());
    }

    @Test
    public void givenEmptyVideos_whenCallFindAll_shouldReturnEmptyList() {
        final var expectedPage=0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var aQuery =
                new VideoSearchQuery(
                        expectedPage,
                        expectedPerPage,
                        expectedTerms,
                        expectedSort,
                        expectedDirection,
                        Set.of(),
                        Set.of(),
                        Set.of()
                );

        final var actualPage = videoGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedTotal, actualPage.items().size());

    }

    @Test
    public void givenAValidCategory_whenCallsFindAll_shouldReturnFilteredList(){
        mockVideos();
        final var expectedPage=0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var aQuery =
                new VideoSearchQuery(
                        expectedPage,
                        expectedPerPage,
                        expectedTerms,
                        expectedSort,
                        expectedDirection,
                        Set.of(),
                        Set.of(aulas.getId()),
                        Set.of()
                );

        final var actualPage = videoGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedTotal, actualPage.items().size());
        Assertions.assertEquals("21.1 Implementação dos testes integrados do findAll", actualPage.items().get(0).title());
        Assertions.assertEquals("Aula de empreendedorismo", actualPage.items().get(1).title());
    }
    @Test
    public void givenAValidGenre_whenCallsFindAll_shouldReturnFilteredList(){
        mockVideos();
        final var expectedPage=0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 1;

        final var aQuery =
                new VideoSearchQuery(
                        expectedPage,
                        expectedPerPage,
                        expectedTerms,
                        expectedSort,
                        expectedDirection,
                        Set.of(),
                        Set.of(),
                        Set.of(business.getId())
                );

        final var actualPage = videoGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedTotal, actualPage.items().size());
        Assertions.assertEquals("Aula de empreendedorismo", actualPage.items().get(0).title());

    }
    @Test
    public void givenAValidCastMember_whenCallsFindAll_shouldReturnFilteredList(){
        mockVideos();
        final var expectedPage=0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var aQuery =
                new VideoSearchQuery(
                        expectedPage,
                        expectedPerPage,
                        expectedTerms,
                        expectedSort,
                        expectedDirection,
                        Set.of(wesley.getId()),
                        Set.of(),
                        Set.of()
                );

        final var actualPage = videoGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedTotal, actualPage.items().size());
        Assertions.assertEquals("Aula de empreendedorismo", actualPage.items().get(0).title());
        Assertions.assertEquals("System design no Mercado Livre na Prática", actualPage.items().get(1).title());

    }
    @Test
    public void givenAllParameters_whenCallFindAll_shouldReturnFilteredList(){
        mockVideos();
        final var expectedPage=0;
        final var expectedPerPage = 10;
        final var expectedTerms = "empreendedorismo";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 1;

        final var aQuery =
                new VideoSearchQuery(
                        expectedPage,
                        expectedPerPage,
                        expectedTerms,
                        expectedSort,
                        expectedDirection,
                        Set.of(wesley.getId()),
                        Set.of(aulas.getId()),
                        Set.of(business.getId())
                );

        final var actualPage = videoGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedTotal, actualPage.items().size());
        Assertions.assertEquals("Aula de empreendedorismo", actualPage.items().get(0).title());

    }

    @ParameterizedTest
    @CsvSource({
            "title,asc,0,10,4,4,21.1 Implementação dos testes integrados do findAll",
            "title,desc,0,10,4,4,System design no Mercado Livre na Prática",
            "createdAt,asc,0,10,4,4,System design no Mercado Livre na Prática",
            "createdAt,desc,0,10,4,4,Aula de empreendedorismo"
    })
    public void givenAValidSortAndDirection_whenCallFindAll_shouldReturnOrdered(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedTitle

    ) {
        mockVideos();
        final var expectedTerms = "";

        final var aQuery =
                new VideoSearchQuery(
                        expectedPage,
                        expectedPerPage,
                        expectedTerms,
                        expectedSort,
                        expectedDirection,
                        Set.of(),
                        Set.of(),
                        Set.of()
                );


        final var actualPage = videoGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
        Assertions.assertEquals(expectedTitle, actualPage.items().get(0).title());

    }

    @ParameterizedTest
    @CsvSource({
            "system,0,10,1,1,System design no Mercado Livre na Prática",
            "microser,0,10,1,1,Não cometa estes erros com Microserviços",
            "empreendedorismo,0,10,1,1,Aula de empreendedorismo",
            "testes,0,10,1,1,21.1 Implementação dos testes integrados do findAll",
    })
    public void givenAValidTerms_whenCallFindAll_shouldReturnFiltered(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedTitle

    ) {

        final var expectedSort = "title";
        final var expectedDirection = "asc";

        mockVideos();

        final var aQuery =
                new VideoSearchQuery(
                        expectedPage,
                        expectedPerPage,
                        expectedTerms,
                        expectedSort,
                        expectedDirection,
                        Set.of(),
                        Set.of(),
                        Set.of()
                );



        final var actualPage = videoGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
        Assertions.assertEquals(expectedTitle, actualPage.items().get(0).title());

    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,4,21.1 Implementação dos testes integrados do findAll;Aula de empreendedorismo",
            "1,2,2,4,Não cometa estes erros com Microserviços;System design no Mercado Livre na Prática",

    })
    public void givenAValidPaging_whenCallFindAll_shouldReturnPaged(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedVideos

    ) {

        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection= "asc";

        mockVideos();

        final var aQuery =
                new VideoSearchQuery(
                        expectedPage,
                        expectedPerPage,
                        expectedTerms,
                        expectedSort,
                        expectedDirection,
                        Set.of(),
                        Set.of(),
                        Set.of()
                );


        final var actualPage = videoGateway.findAll(aQuery);

        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());

        int index = 0;
        for(final var expectedTitle : expectedVideos.split(";")){
            final var actualTitle = actualPage.items().get(index).title();
            Assertions.assertEquals(expectedTitle, actualTitle);
            index++;
        }


    }

    private void mockVideos(){


        videoGateway.create(Video.newVideo(
                "System design no Mercado Livre na Prática",
                Fixture.Videos.description(),
                Year.of(2022),
                Fixture.duration(),
                Fixture.bool(),
                Fixture.bool(),
                Fixture.Videos.rating(),
                Set.of(lives.getId()),
                Set.of(tech.getId()),
                Set.of(wesley.getId(), helion.getId())
        ));
        videoGateway.create(Video.newVideo(
                "Não cometa estes erros com Microserviços",
                Fixture.Videos.description(),
                Year.of(Fixture.year()),
                Fixture.duration(),
                Fixture.bool(),
                Fixture.bool(),
                Fixture.Videos.rating(),
                Set.of(),
                Set.of(),
                Set.of()
        ));

        videoGateway.create(Video.newVideo(
                "21.1 Implementação dos testes integrados do findAll",
                Fixture.Videos.description(),
                Year.of(Fixture.year()),
                Fixture.duration(),
                Fixture.bool(),
                Fixture.bool(),
                Fixture.Videos.rating(),
                Set.of(aulas.getId()),
                Set.of(tech.getId()),
                Set.of(helion.getId())
        ));
        videoGateway.create(Video.newVideo(
                "Aula de empreendedorismo",
                Fixture.Videos.description(),
                Year.of(Fixture.year()),
                Fixture.duration(),
                Fixture.bool(),
                Fixture.bool(),
                Fixture.Videos.rating(),
                Set.of(aulas.getId()),
                Set.of(business.getId()),
                Set.of(wesley.getId())
        ));

    }
}
