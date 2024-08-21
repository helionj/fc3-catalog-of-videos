package com.helion.catalog.application.video.list;

import com.helion.catalog.application.UseCaseTest;
import com.helion.catalog.domain.Fixture;
import com.helion.catalog.domain.pagination.Pagination;
import com.helion.catalog.domain.video.VideoGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ListVideoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private ListVideoUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Test
    public void givenAValidQuery_whenCallsListVideos_shouldReturnVideos() {
        final var videos = List.of(
                Fixture.Videos.systemDesign(), Fixture.Videos.java21()
        );

        final var expectedItems = videos.stream().map(ListVideoUseCase.Output::from).toList();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "algo";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 2;
        final var expectedRating = "L";
        final var expectedYear = 2022;

        final var expectedCategories = Set.of("c1");
        final var expectedGenres = Set.of("c1");
        final var expectedCastMembers = Set.of("c1");


        final var aQuery =
                new ListVideoUseCase.Input(
                        expectedPage,
                        expectedPerPage,
                        expectedTerms,
                        expectedSort,
                        expectedDirection,
                        expectedRating,
                        expectedYear,
                        expectedCategories,
                        expectedCastMembers,
                        expectedGenres);

        final var pagination =
                new Pagination<>(expectedPage, expectedPerPage, videos.size(), videos);


        when(this.videoGateway.findAll(any())).thenReturn(pagination);

        final var actualOutput = this.useCase.execute(aQuery);

        Assertions.assertEquals(expectedPage, actualOutput.meta().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.meta().perPage());
        Assertions.assertEquals(expectedItemsCount, actualOutput.data().size());
        Assertions.assertTrue(expectedItems.size() == actualOutput.data().size() && expectedItems.containsAll(actualOutput.data()));


    }

}
