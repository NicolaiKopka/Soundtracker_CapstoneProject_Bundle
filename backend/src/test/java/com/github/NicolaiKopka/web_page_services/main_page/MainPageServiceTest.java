package com.github.NicolaiKopka.web_page_services.main_page;

import com.github.NicolaiKopka.api_services.MovieDBApiConnect;
import com.github.NicolaiKopka.api_services.SpotifyApiConnect;
import com.github.NicolaiKopka.db_models.movieDBModels.Movie;
import com.github.NicolaiKopka.db_models.spotifyModels.SpotifyAlbum;
import com.github.NicolaiKopka.db_models.spotifyModels.SpotifyAlbumExternalURLs;
import com.github.NicolaiKopka.dto.StreamingStatusDTO;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.List;


class MainPageServiceTest {

    @Test
    void shouldReturnListOfCurrentPopularMovies() {
        Movie movie1 = Movie.builder().title("movie1").build();
        Movie movie2 = Movie.builder().title("movie2").build();

        List<Movie> expected = List.of(movie1, movie2);

        MovieDBApiConnect movieDBApiConnect = Mockito.mock(MovieDBApiConnect.class);
        Mockito.when(movieDBApiConnect.getPopularMovies()).thenReturn(List.of(movie1, movie2));

        SpotifyApiConnect spotifyApiConnect = Mockito.mock(SpotifyApiConnect.class);

        MainPageService mainPageService = new MainPageService(movieDBApiConnect, spotifyApiConnect);

        List<Movie> actual = mainPageService.getPopularMovies();

        Assertions.assertThat(actual).containsAll(expected);
    }

    @Test
    void shouldReturnStreamingStatusObjectWithKeySpotifySetToTrueAndLinkToAlbum() throws OAuthProblemException, OAuthSystemException {
        SpotifyAlbum album1 = SpotifyAlbum.builder().name("movieAlbum (Official Motion Picture Soundtrack")
                .externalURLs(new SpotifyAlbumExternalURLs("falseLink")).build();
        SpotifyAlbum album2 = SpotifyAlbum.builder().name("testMovie (Official Motion Picture Soundtrack")
                .externalURLs(new SpotifyAlbumExternalURLs("link.com")).build();
        MovieDBApiConnect movieDBApiConnect = Mockito.mock(MovieDBApiConnect.class);

        SpotifyApiConnect spotifyApiConnect = Mockito.mock(SpotifyApiConnect.class);
        Mockito.when(spotifyApiConnect.getSpotifyListOfMovieAlbums("testMovie")).thenReturn(List.of(album1, album2));

        MainPageService mainPageService = new MainPageService(movieDBApiConnect, spotifyApiConnect);

        StreamingStatusDTO actual = mainPageService.getSoundtrackOnSpotify("testMovie");

        Assertions.assertThat(actual.getMovieName()).isEqualTo("testMovie");
        Assertions.assertThat(actual.getStreamingServiceStatus().get("spotify")).isEqualTo(true);
        Assertions.assertThat(actual.getAlbumLinks().get("spotify")).isEqualTo("link.com");
    }

    @Test
    void shouldReturnStreamingStatusObjectWithSpotifyStatusSetFalseIfNoMovieFound() throws OAuthProblemException, OAuthSystemException {
        MovieDBApiConnect movieDBApiConnect = Mockito.mock(MovieDBApiConnect.class);

        SpotifyApiConnect spotifyApiConnect = Mockito.mock(SpotifyApiConnect.class);
        Mockito.when(spotifyApiConnect.getSpotifyListOfMovieAlbums("noMovie")).thenReturn(List.of());

        MainPageService mainPageService = new MainPageService(movieDBApiConnect, spotifyApiConnect);

        StreamingStatusDTO actual = mainPageService.getSoundtrackOnSpotify("noMovie");

        Assertions.assertThat(actual.getMovieName()).isEqualTo("noMovie");
        Assertions.assertThat(actual.getStreamingServiceStatus().get("spotify")).isEqualTo(false);
    }

}