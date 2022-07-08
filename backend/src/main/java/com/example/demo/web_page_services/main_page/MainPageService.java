package com.example.demo.web_page_services.main_page;

import com.example.demo.api_services.MovieDBApiConnect;
import com.example.demo.api_services.SpotifyApiConnect;
import com.example.demo.db_models.movieDBModels.Movie;
import lombok.RequiredArgsConstructor;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class MainPageService {

    private final MovieDBApiConnect movieDBApiConnect;
    private final SpotifyApiConnect spotifyApiConnect;

    public List<Movie> getPopularMovies() {
        return movieDBApiConnect.getPopularMovies();
    }

    public void getSoundtrackOnSpotify() throws OAuthProblemException, OAuthSystemException {
        spotifyApiConnect.getSpotifyStatusForMovie();
    }
}
