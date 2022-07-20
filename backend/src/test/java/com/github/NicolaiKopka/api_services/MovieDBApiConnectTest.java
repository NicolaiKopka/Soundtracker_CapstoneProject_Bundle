package com.github.NicolaiKopka.api_services;

import com.github.NicolaiKopka.db_models.movieDBModels.Movie;
import com.github.NicolaiKopka.db_models.movieDBModels.MovieList;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MovieDBApiConnectTest {

    @Test
    void shouldReturnListOfPopularMovies() {

        Movie movie1 = Movie.builder().title("superPopularMovie1").build();
        Movie movie2 = Movie.builder().title("superPopularMovie2").build();

        MovieList movieList = new MovieList();
        movieList.setResults(List.of(movie1, movie2));

        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
        Mockito.when(restTemplate.getForObject(
                "https://api.themoviedb.org/3/discover/movie?" +
                        "api_key=testToken&language=en-US&sort_by=popularity.desc"
                , MovieList.class)).thenReturn(movieList);

        MovieDBApiConnect movieDBApiConnect = new MovieDBApiConnect(restTemplate, "testToken");

        List<Movie> actual = movieDBApiConnect.getPopularMovies();

        Assertions.assertThat(actual).contains(movie1, movie2);
    }

    @Test
    void shouldReturnCollectionOfMoviesBySearchQuery() {
        Movie movie1 = Movie.builder().title("searchMovie1").build();
        Movie movie2 = Movie.builder().title("searchMovie2").build();

        MovieList movieList = new MovieList();
        movieList.setResults(List.of(movie1, movie2));

        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
        Mockito.when(restTemplate.getForObject(
                "https://api.themoviedb.org/3/search/movie?" +
                        "api_key=testToken&query=movieSearch&include_adult=false"
                , MovieList.class)).thenReturn(movieList);

        MovieDBApiConnect movieDBApiConnect = new MovieDBApiConnect(restTemplate, "testToken");

        Collection<Movie> actual = movieDBApiConnect.getMoviesByQuery("movieSearch");

        Assertions.assertThat(actual).contains(movie1, movie2);
    }

    @Test
    void shouldReturnMovieById() {
        Movie movie1 = Movie.builder().title("searchMovie1").build();

        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
        Mockito.when(restTemplate.getForObject("https://api.themoviedb.org/3/movie/1234?" +
                "api_key=testToken"
                , Movie.class)).thenReturn(movie1);

        MovieDBApiConnect movieDBApiConnect = new MovieDBApiConnect(restTemplate, "testToken");
        Movie actual = movieDBApiConnect.getMovieById(1234);

        Assertions.assertThat(actual).isEqualTo(movie1);

    }

}