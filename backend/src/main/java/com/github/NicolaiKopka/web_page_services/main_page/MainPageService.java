package com.github.NicolaiKopka.web_page_services.main_page;

import com.github.NicolaiKopka.api_services.MovieDBApiConnect;
import com.github.NicolaiKopka.api_services.SpotifyApiConnect;
import com.github.NicolaiKopka.db_models.movieDBModels.Movie;
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
