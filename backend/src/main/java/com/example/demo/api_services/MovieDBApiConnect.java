package com.example.demo.api_services;

import com.example.demo.db_models.movieDBModels.Movie;
import com.example.demo.db_models.movieDBModels.MovieList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class MovieDBApiConnect {

    private final RestTemplate restTemplate;
    private final String API_KEY;

    public MovieDBApiConnect(RestTemplate restTemplate, @Value("${moviedb.api.key}") String API_KEY) {
        this.restTemplate = restTemplate;
        this.API_KEY = API_KEY;
    }

    private final String urlPrefix = "https://api.themoviedb.org/3/";

    public List<Movie> getPopularMovies() {

        String discoverByPopularityEndpoint = urlPrefix + "discover/movie?api_key=" +
                API_KEY + "&language=en-US&sort_by=popularity.desc";

        MovieList response = restTemplate.getForObject(discoverByPopularityEndpoint, MovieList.class);
        List<Movie> movies = response.getResults().stream().toList();
        return movies;
    }
}
