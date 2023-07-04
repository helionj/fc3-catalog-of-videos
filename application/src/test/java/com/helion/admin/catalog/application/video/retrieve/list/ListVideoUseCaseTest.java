package com.helion.admin.catalog.application.video.retrieve.list;

import com.helion.admin.catalog.application.UseCaseTest;
import com.helion.admin.catalog.domain.Fixture;
import com.helion.admin.catalog.domain.category.pagination.Pagination;
import com.helion.admin.catalog.domain.video.VideoGateway;
import com.helion.admin.catalog.domain.video.VideoPreview;
import com.helion.admin.catalog.domain.video.VideoSearchQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;

public class ListVideoUseCaseTest extends UseCaseTest {


    @InjectMocks
    private DefaultListVideosUseCase useCase;

    @Mock
    private VideoGateway videoGateway;
    @Override
    protected List<Object> getMocks() {
        return List.of(videoGateway);
    }

    @Test
    public void givenAValidQuery_whenCallsListVideos_shouldReturnVideos(){
        final var videos = List.of(
                new VideoPreview(Fixture.video()),
                new VideoPreview(Fixture.video())
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 2;

        final var expectedItems = videos.stream()
                .map(VideoListOutput::from)
                .toList();

        final var aQuery = new VideoSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection, null, null, null);


        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, videos.size(), videos);
        final var expectedResult = expectedPagination.map(VideoListOutput::from);
        Mockito.when(videoGateway.findAll(eq(aQuery))).thenReturn(expectedPagination);

        final var actualResult = useCase.execute(aQuery);
        Assertions.assertEquals(expectedItems, actualResult.items());
        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedResult, actualResult);
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(videos.size(), actualResult.total());

        Mockito.verify(videoGateway, times(1)).findAll(eq(aQuery));
    }

    @Test
    public void givenAValidQuery_whenCallsListVideosAndHasNoResults_shouldReturnEmpty(){
        final var videos = List.<VideoPreview>of();
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 0;

        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                Set.of(),
                Set.of(),
                Set.of()
        );


        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, videos.size(), videos);
        final var expectedResult = expectedPagination.map(VideoListOutput::from);
        Mockito.when(videoGateway.findAll(eq(aQuery))).thenReturn(expectedPagination);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedResult, actualResult);
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(videos.size(), actualResult.total());
    }

    @Test
    public void givenAValidQuery_whenCallsListVideosAndGatewayThrowsException_shouldReturnException(){

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedErrorMessage = "Gateway Error";

        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                Set.of(),
                Set.of(),
                Set.of()
        );

        Mockito.when(videoGateway.findAll(eq(aQuery))).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualException = Assertions.assertThrows(IllegalStateException.class,
                () -> useCase.execute(aQuery));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

    }
}
