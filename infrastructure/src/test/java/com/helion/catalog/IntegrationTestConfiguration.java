package com.helion.catalog;

import com.helion.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import com.helion.catalog.infrastructure.category.persistence.CategoryRepository;
import com.helion.catalog.infrastructure.genre.persistence.GenreRepository;
import com.helion.catalog.infrastructure.video.persistence.VideoRepository;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;


public class IntegrationTestConfiguration {

    @Bean
    public CategoryRepository categoryRepository(){
            return Mockito.mock(CategoryRepository.class);
        }

    @Bean
    public CastMemberRepository castMemberRepository(){
        return Mockito.mock(CastMemberRepository.class);
    }

    @Bean
    public GenreRepository genreRepository(){
        return Mockito.mock(GenreRepository.class);
    }

    @Bean
    public VideoRepository videoRepository(){
        return Mockito.mock(VideoRepository.class);
    }
}

