package com.github.NicolaiKopka.web_page_services.main_page;

import com.github.NicolaiKopka.api_services.DeezerApiConnect;
import com.github.NicolaiKopka.api_services.MovieDBApiConnect;
import com.github.NicolaiKopka.api_services.SpotifyApiConnect;
import com.github.NicolaiKopka.db_models.AlbumReferenceDTO;
import com.github.NicolaiKopka.db_models.movieDBModels.Movie;
import com.github.NicolaiKopka.dto.StreamingStatusDTO;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
        AlbumReferenceDTO album1 = AlbumReferenceDTO.builder().movieTitle("movieAlbum (Official Motion Picture Soundtrack")
                .albumUrl("falseLink").build();
        AlbumReferenceDTO album2 = AlbumReferenceDTO.builder().movieTitle("testMovie (Official Motion Picture Soundtrack")
                .albumUrl("link.com").build();

        AlbumReferenceDTO album3 = AlbumReferenceDTO.builder().movieTitle("movieAlbum (Official Motion Picture Soundtrack")
                .deezerAlbumId(1234).albumUrl("falseLink.com").build();
        AlbumReferenceDTO album4 = AlbumReferenceDTO.builder().movieTitle("testMovie (Official Motion Picture Soundtrack")
                .deezerAlbumId(5678).albumUrl("link.com").build();

        MovieDBApiConnect movieDBApiConnect = Mockito.mock(MovieDBApiConnect.class);

        DeezerApiConnect deezerApiConnect = Mockito.mock(DeezerApiConnect.class);
        Mockito.when(deezerApiConnect.getFullSearchData("testMovie")).thenReturn(List.of(album3, album4));
        Mockito.when(deezerApiConnect.getAlbumById(5678, album4)).thenReturn(album4);

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

        Mockito.when(deezerApiConnect.getFullSearchData("noMovie")).thenReturn(List.of());

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