package com.github.NicolaiKopka.web_page_services.main_page;

import com.github.NicolaiKopka.api_services.DeezerApiConnect;
import com.github.NicolaiKopka.api_services.MovieDBApiConnect;
import com.github.NicolaiKopka.api_services.SpotifyApiConnect;
import com.github.NicolaiKopka.db_models.deezerModels.DeezerAlbum;
import com.github.NicolaiKopka.db_models.deezerModels.DeezerSearchData;
import com.github.NicolaiKopka.db_models.deezerModels.DeezerSearchObject;
import com.github.NicolaiKopka.db_models.movieDBModels.Movie;
import com.github.NicolaiKopka.db_models.spotifyModels.SpotifyAlbum;
import com.github.NicolaiKopka.db_models.spotifyModels.SpotifyAlbumExternalURLs;
import com.github.NicolaiKopka.dto.StreamingStatusDTO;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.List;

import static com.mongodb.assertions.Assertions.fail;


class MainPageServiceTest {

    @Test
    void shouldReturnListOfCurrentPopularMovies() {
        Movie movie1 = Movie.builder().title("movie1").build();
        Movie movie2 = Movie.builder().title("movie2").build();

        List<Movie> expected = List.of(movie1, movie2);

        MovieDBApiConnect movieDBApiConnect = Mockito.mock(MovieDBApiConnect.class);
        Mockito.when(movieDBApiConnect.getPopularMovies()).thenReturn(List.of(movie1, movie2));

        SpotifyApiConnect spotifyApiConnect = Mockito.mock(SpotifyApiConnect.class);

        DeezerApiConnect deezerApiConnect = Mockito.mock(DeezerApiConnect.class);

        MainPageService mainPageService = new MainPageService(movieDBApiConnect, spotifyApiConnect, deezerApiConnect);

        List<Movie> actual = mainPageService.getPopularMovies();

        Assertions.assertThat(actual).containsAll(expected);
    }

    @Test
    void shouldReturnStreamingStatusObjectWithKeySpotifySetToTrueAndLinkToAlbum() throws OAuthProblemException, OAuthSystemException {
        SpotifyAlbum album1 = SpotifyAlbum.builder().name("movieAlbum (Official Motion Picture Soundtrack")
                .externalURLs(new SpotifyAlbumExternalURLs("falseLink")).build();
        SpotifyAlbum album2 = SpotifyAlbum.builder().name("testMovie (Official Motion Picture Soundtrack")
                .externalURLs(new SpotifyAlbumExternalURLs("link.com")).build();
        DeezerAlbum album3 = DeezerAlbum.builder().title("movieAlbum (Official Motion Picture Soundtrack")
                .id(1234).link("falseLink.com").build();
        DeezerAlbum album4 = DeezerAlbum.builder().title("testMovie (Official Motion Picture Soundtrack")
                .id(5678).link("link.com").build();

        DeezerSearchData deezerSearchData = new DeezerSearchData();
        deezerSearchData.setData(List.of(new DeezerSearchObject(album3), new DeezerSearchObject(album4)));

        MovieDBApiConnect movieDBApiConnect = Mockito.mock(MovieDBApiConnect.class);

        DeezerApiConnect deezerApiConnect = Mockito.mock(DeezerApiConnect.class);
        Mockito.when(deezerApiConnect.getFullSearchData("testMovie")).thenReturn(deezerSearchData);
        Mockito.when(deezerApiConnect.getAlbumById(5678)).thenReturn(album4);

        SpotifyApiConnect spotifyApiConnect = Mockito.mock(SpotifyApiConnect.class);
        Mockito.when(spotifyApiConnect.getSpotifyListOfMovieAlbums("testMovie")).thenReturn(List.of(album1, album2));

        MainPageService mainPageService = new MainPageService(movieDBApiConnect, spotifyApiConnect, deezerApiConnect);

        StreamingStatusDTO actual = mainPageService.getSoundtrackOnSpotify("testMovie");

        Assertions.assertThat(actual.getMovieName()).isEqualTo("testMovie");
        Assertions.assertThat(actual.getStreamingServiceStatus().get("spotify")).isEqualTo(true);
        Assertions.assertThat(actual.getAlbumLinks().get("spotify")).isEqualTo("link.com");

        Assertions.assertThat(actual.getMovieName()).isEqualTo("testMovie");
        Assertions.assertThat(actual.getStreamingServiceStatus().get("deezer")).isEqualTo(true);
        Assertions.assertThat(actual.getAlbumLinks().get("deezer")).isEqualTo("link.com");
    }

    @Test
    void shouldReturnStreamingStatusObjectWithSpotifyStatusSetFalseIfNoMovieFound() throws OAuthProblemException, OAuthSystemException {
        MovieDBApiConnect movieDBApiConnect = Mockito.mock(MovieDBApiConnect.class);

        DeezerApiConnect deezerApiConnect = Mockito.mock(DeezerApiConnect.class);
        DeezerSearchData deezerSearchData = new DeezerSearchData();
        deezerSearchData.setData(List.of());

        Mockito.when(deezerApiConnect.getFullSearchData("noMovie")).thenReturn(deezerSearchData);

        SpotifyApiConnect spotifyApiConnect = Mockito.mock(SpotifyApiConnect.class);
        Mockito.when(spotifyApiConnect.getSpotifyListOfMovieAlbums("noMovie")).thenReturn(List.of());

        MainPageService mainPageService = new MainPageService(movieDBApiConnect, spotifyApiConnect, deezerApiConnect);

        StreamingStatusDTO actual = mainPageService.getSoundtrackOnSpotify("noMovie");

        Assertions.assertThat(actual.getMovieName()).isEqualTo("noMovie");
        Assertions.assertThat(actual.getStreamingServiceStatus().get("spotify")).isEqualTo(false);
        Assertions.assertThat(actual.getMovieName()).isEqualTo("noMovie");
        Assertions.assertThat(actual.getStreamingServiceStatus().get("deezer")).isEqualTo(false);
    }

    @Test
    void shouldReturnListOfMoviesFromSearchQuery() {
        Movie movie1 = Movie.builder().title("movie1").build();
        Movie movie2 = Movie.builder().title("movie2").build();

        List<Movie> expected = List.of(movie1, movie2);

        MovieDBApiConnect movieDBApiConnect = Mockito.mock(MovieDBApiConnect.class);
        Mockito.when(movieDBApiConnect.getMoviesByQuery("movie")).thenReturn(List.of(movie1, movie2));

        SpotifyApiConnect spotifyApiConnect = Mockito.mock(SpotifyApiConnect.class);

        DeezerApiConnect deezerApiConnect = Mockito.mock(DeezerApiConnect.class);

        MainPageService mainPageService = new MainPageService(movieDBApiConnect, spotifyApiConnect, deezerApiConnect);

        Collection<Movie> actual = mainPageService.getMoviesByQuery("movie");

        Assertions.assertThat(actual).containsAll(expected);
    }

    @Test
    void shouldFailSearchQueryIfNoMoviesFound() {
        Movie movie1 = Movie.builder().title("movie1").build();
        Movie movie2 = Movie.builder().title("movie2").build();

        List<Movie> expected = List.of(movie1, movie2);

        MovieDBApiConnect movieDBApiConnect = Mockito.mock(MovieDBApiConnect.class);
        Mockito.when(movieDBApiConnect.getMoviesByQuery("movie")).thenReturn(List.of(movie1, movie2));

        SpotifyApiConnect spotifyApiConnect = Mockito.mock(SpotifyApiConnect.class);

        DeezerApiConnect deezerApiConnect = Mockito.mock(DeezerApiConnect.class);

        MainPageService mainPageService = new MainPageService(movieDBApiConnect, spotifyApiConnect, deezerApiConnect);

        try {
            mainPageService.getMoviesByQuery("noMovie");
            fail();
        } catch (RuntimeException e) {
            Assertions.assertThat(e.getMessage()).isEqualTo("No movies found");
        }
    }

}