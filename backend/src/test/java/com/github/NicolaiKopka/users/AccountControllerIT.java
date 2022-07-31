package com.github.NicolaiKopka.users;

import com.github.NicolaiKopka.api_services.DeezerApiConnect;
import com.github.NicolaiKopka.api_services.MovieDBApiConnect;
import com.github.NicolaiKopka.api_services.SpotifyApiConnect;
import com.github.NicolaiKopka.db_models.AlbumReferenceDTO;
import com.github.NicolaiKopka.db_models.movieDBModels.Movie;
import com.github.NicolaiKopka.db_models.spotifyModels.SpotifyAlbumExternalURLs;
import com.github.NicolaiKopka.db_models.spotifyModels.SpotifyTrack;
import com.github.NicolaiKopka.db_models.spotifyModels.SpotifyTrackDTO;
import com.github.NicolaiKopka.db_models.userPlaylistModels.UserPlaylistSendObject;
import com.github.NicolaiKopka.dto.LoginResponseDTO;
import com.github.NicolaiKopka.dto.RegisterUserDTO;
import com.github.NicolaiKopka.dto.StreamingStatusDTO;
import com.github.NicolaiKopka.dto.UserFavoritesDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountControllerIT {

    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    MyUserRepo userRepo;
    @MockBean
    MovieDBApiConnect movieDBApiConnect;
    @MockBean
    SpotifyApiConnect spotifyApiConnect;
    @MockBean
    DeezerApiConnect deezerApiConnect;
    @AfterEach
    void clearDB() {
        userRepo.deleteAll();
    }
    @Test
    @Order(0)
    void userRegistersAndLogsInWithWrongAndCorrectCredentials() {
        // register User
        RegisterData newUser = RegisterData.builder()
                .username("user1")
                .password("password")
                .checkPassword("password")
                .build();

        ResponseEntity<RegisterUserDTO> registerUserResponse = restTemplate.postForEntity("/api/soundtracker/accounts/register",
                newUser,
                RegisterUserDTO.class);
        Assertions.assertThat(registerUserResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(registerUserResponse.getBody().getUsername()).isEqualTo("user1");

        // login with wrong credentials
        LoginData wrongUser1Login = LoginData.builder().username("user1").password("wrongPassword").build();
        ResponseEntity<LoginResponseDTO> wrongLoginUserResponse = restTemplate.postForEntity("/api/soundtracker/accounts/login",
                wrongUser1Login,
                LoginResponseDTO.class);
        Assertions.assertThat(wrongLoginUserResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        // login with correct credentials
        LoginData user1Login = LoginData.builder().username("user1").password("password").build();
        ResponseEntity<LoginResponseDTO> loginUserResponse = restTemplate.postForEntity("/api/soundtracker/accounts/login",
                user1Login,
                LoginResponseDTO.class);
        Assertions.assertThat(loginUserResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(loginUserResponse.getBody().getToken()).isNotBlank();
    }
    @Test
    @Order(1)
    void registeredUsersAddFavoriteMoviesAndDeleteFavoriteMovies() {
        // register User 1
        RegisterData newUser1 = RegisterData.builder()
                .username("user1")
                .password("password")
                .checkPassword("password")
                .build();

        restTemplate.postForEntity("/api/soundtracker/accounts/register",
                newUser1,
                RegisterUserDTO.class);

        // login user 1
        LoginData user1Login = LoginData.builder().username("user1").password("password").build();
        ResponseEntity<LoginResponseDTO> loginUser1Response = restTemplate.postForEntity("/api/soundtracker/accounts/login",
                user1Login,
                LoginResponseDTO.class);

        // register User 2
        RegisterData newUser2 = RegisterData.builder()
                .username("user2")
                .password("password")
                .checkPassword("password")
                .build();

        restTemplate.postForEntity("/api/soundtracker/accounts/register",
                newUser2,
                RegisterUserDTO.class);

        // login user 2
        LoginData user2Login = LoginData.builder().username("user2").password("password").build();
        ResponseEntity<LoginResponseDTO> loginUser2Response = restTemplate.postForEntity("/api/soundtracker/accounts/login",
                user2Login,
                LoginResponseDTO.class);

        // fetch user token
        String user1Token = loginUser1Response.getBody().getToken();
        String user2Token = loginUser2Response.getBody().getToken();

        // users add favorite movies
        ResponseEntity<UserFavoritesDTO> user1addId1Response = restTemplate.exchange("/api/soundtracker/user-favorites/1",
                HttpMethod.PUT,
                new HttpEntity<>(createHeader(user1Token)),
                UserFavoritesDTO.class);
        Assertions.assertThat(user1addId1Response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(user1addId1Response.getBody().getMovieIds()).contains(1);

        ResponseEntity<UserFavoritesDTO> user1addId2Response = restTemplate.exchange("/api/soundtracker/user-favorites/2",
                HttpMethod.PUT,
                new HttpEntity<>(createHeader(user1Token)),
                UserFavoritesDTO.class);
        Assertions.assertThat(user1addId2Response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(user1addId2Response.getBody().getMovieIds()).contains(1, 2);

        ResponseEntity<UserFavoritesDTO> user2addId1Response = restTemplate.exchange("/api/soundtracker/user-favorites/123",
                HttpMethod.PUT,
                new HttpEntity<>(createHeader(user2Token)),
                UserFavoritesDTO.class);
        Assertions.assertThat(user2addId1Response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(user2addId1Response.getBody().getMovieIds()).contains(123);

        // users get respective favorite movies
        Movie movie1 = Movie.builder().title("movie1").id(1).build();
        Movie movie2 = Movie.builder().title("movie2").id(2).build();
        Movie movie3 = Movie.builder().title("movie3").id(123).build();
        Mockito.when(movieDBApiConnect.getMovieById(1)).thenReturn(movie1);
        Mockito.when(movieDBApiConnect.getMovieById(2)).thenReturn(movie2);
        Mockito.when(movieDBApiConnect.getMovieById(123)).thenReturn(movie3);

        ResponseEntity<Movie[]> user1GetMoviesResponse = restTemplate.exchange("/api/soundtracker/user-favorites",
                HttpMethod.GET,
                new HttpEntity<>(createHeader(user1Token)),
                Movie[].class);
        Assertions.assertThat(user1GetMoviesResponse.getBody().length).isEqualTo(2);
        Assertions.assertThat(user1GetMoviesResponse.getBody()).contains(movie1, movie2);

        ResponseEntity<Movie[]> user2GetMoviesResponse = restTemplate.exchange("/api/soundtracker/user-favorites",
                HttpMethod.GET,
                new HttpEntity<>(createHeader(user2Token)),
                Movie[].class);
        Assertions.assertThat(user2GetMoviesResponse.getBody().length).isEqualTo(1);
        Assertions.assertThat(user2GetMoviesResponse.getBody()).contains(movie3);

        //user1 deletes movie1 from favorites
        restTemplate.exchange("/api/soundtracker/user-favorites/1",
                HttpMethod.DELETE,
                new HttpEntity<>(createHeader(user1Token)),
                UserFavoritesDTO.class);

        ResponseEntity<Movie[]> user1GetMoviesResponseAfterDelete = restTemplate.exchange("/api/soundtracker/user-favorites",
                HttpMethod.GET,
                new HttpEntity<>(createHeader(user1Token)),
                Movie[].class);
        Assertions.assertThat(user1GetMoviesResponseAfterDelete.getBody().length).isEqualTo(1);
        Assertions.assertThat(user1GetMoviesResponseAfterDelete.getBody()).contains(movie2);
    }
    @Test
    @Order(2)
    void userChecksForSoundtrackAvailabilityOnStreamingServices(){
        AlbumReferenceDTO reference1 = AlbumReferenceDTO.builder()
                .movieTitle("wrongMovie(Official)")
                        .albumUrl("wrongMovie.com").build();

        AlbumReferenceDTO reference2 = AlbumReferenceDTO.builder()
                .movieTitle("testMovie(Official)")
                .deezerAlbumId(1234)
                .albumUrl("testMovie.com").build();

        List<AlbumReferenceDTO> referenceReturnList = new ArrayList<>();
        referenceReturnList.add(reference1);
        referenceReturnList.add(reference2);

        Mockito.when(spotifyApiConnect.getSpotifyListOfMovieAlbums("testMovie")).thenReturn(referenceReturnList);
        Mockito.when(deezerApiConnect.getFullSearchData("testMovie")).thenReturn(referenceReturnList);
        Mockito.when(deezerApiConnect.getAlbumById(1234, referenceReturnList.get(1))).thenReturn(reference2);

        StreamingStatusDTO actual = restTemplate.getForObject("/api/soundtracker/streaming/testMovie", StreamingStatusDTO.class);

        Assertions.assertThat(actual.getAlbumLinks()).isEqualTo(Map.of("spotify", "testMovie.com", "deezer", "testMovie.com"));
    }
    @Test
    @Order(3)
    void userSearchesForMovieInMovieDB() {
        Movie movie1 = Movie.builder().title("movie1").build();
        Movie movie2 = Movie.builder().title("movie2").build();

        Mockito.when(movieDBApiConnect.getMoviesByQuery("searchMovie")).thenReturn(List.of(movie1, movie2));

        ResponseEntity<Movie[]> actual = restTemplate.getForEntity("/api/soundtracker/search/searchMovie", Movie[].class);

        Assertions.assertThat(actual.getBody()).contains(movie1, movie2);
    }
    // TODO correct for deezer when implemented. In add track section.
    @Test
    @Order(4)
    void userNavigatesToSoundtrackPageCreatesAPlaylistAddsAndDeletesTracksAndDeletesRespectivePlaylist(){
        // register User 1
        RegisterData newUser1 = RegisterData.builder()
                .username("user1")
                .password("password")
                .checkPassword("password")
                .build();

        restTemplate.postForEntity("/api/soundtracker/accounts/register",
                newUser1,
                RegisterUserDTO.class);

        // login user 1
        LoginData user1Login = LoginData.builder().username("user1").password("password").build();
        ResponseEntity<LoginResponseDTO> loginUser1Response = restTemplate.postForEntity("/api/soundtracker/accounts/login",
                user1Login,
                LoginResponseDTO.class);

        // fetch user token
        String user1Token = loginUser1Response.getBody().getToken();

        //click on movie and navigate to track list. next gets tracks
        SpotifyTrack track1 = SpotifyTrack.builder().id("track1")
                .externalURLs(new SpotifyAlbumExternalURLs()).build();
        SpotifyTrack track2 = SpotifyTrack.builder().id("track2")
                .externalURLs(new SpotifyAlbumExternalURLs()).build();

        List<SpotifyTrack> trackList = new ArrayList<>();
        trackList.add(track1);
        trackList.add(track2);

        Mockito.when(spotifyApiConnect.getSpotifyAlbumTracksById("movieId"))
                .thenReturn(trackList);

        ResponseEntity<SpotifyTrackDTO[]> spotifyTracksResponse = restTemplate.exchange(
                "/api/soundtracker/spotify/album/movieId",
                HttpMethod.GET,
                new HttpEntity<>(createHeader(user1Token)),
                SpotifyTrackDTO[].class);

        Assertions.assertThat(spotifyTracksResponse.getBody().length).isEqualTo(2);
        Assertions.assertThat(spotifyTracksResponse.getBody()[0].getId()).isEqualTo("track1");

        //user creates new playlist by adding track to new playlist name. also adds track to respective playlist
        ResponseEntity<UserFavoritesDTO> newPlaylistResponse = restTemplate.exchange("/api/soundtracker/user-favorites/create-user-playlist/newPlaylist",
                HttpMethod.POST,
                new HttpEntity<>(createHeader(user1Token)),
                UserFavoritesDTO.class);

        Assertions.assertThat(newPlaylistResponse.getBody().getUserPlaylists()).containsKey("newPlaylist");

        UserPlaylistSendObject sendObject = UserPlaylistSendObject.builder()
                .playlistName("newPlaylist")
                .spotifyTrackId(spotifyTracksResponse.getBody()[0].getId())
                .deezerTrackId("track1").build();

        UserPlaylistSendObject sendObject2 = UserPlaylistSendObject.builder()
                .playlistName("newPlaylist")
                .spotifyTrackId(spotifyTracksResponse.getBody()[1].getId())
                .deezerTrackId("track2").build();

        restTemplate.exchange("/api/soundtracker/user-favorites/user-playlist/add-track",
                HttpMethod.POST,
                new HttpEntity<>(sendObject, createHeader(user1Token)),
                UserFavoritesDTO.class);

        ResponseEntity<UserFavoritesDTO> addTrackResponse2 = restTemplate.exchange("/api/soundtracker/user-favorites/user-playlist/add-track",
                HttpMethod.POST,
                new HttpEntity<>(sendObject2, createHeader(user1Token)),
                UserFavoritesDTO.class);

        Assertions.assertThat(addTrackResponse2.getBody().getUserPlaylists().get(sendObject.getPlaylistName())
                .getSpotifyTrackIds()).contains("track1", "track2");

        // user deletes track from playlist
        UserPlaylistSendObject sendObjectForDelete = UserPlaylistSendObject.builder()
                .playlistName("newPlaylist")
                .spotifyTrackId("track1")
                .deezerTrackId("track1").build();

        ResponseEntity<UserFavoritesDTO> deletedTrackResponse = restTemplate.exchange("/api/soundtracker/user-favorites/user-playlist/delete-track",
                HttpMethod.DELETE,
                new HttpEntity<>(sendObjectForDelete, createHeader(user1Token)),
                UserFavoritesDTO.class);

        Assertions.assertThat(deletedTrackResponse.getBody().getUserPlaylists()
                .get("newPlaylist").getSpotifyTrackIds()).contains("track2");

        // user gets all playlist on playlist page and deletes playlist
        ResponseEntity<UserFavoritesDTO> allPlaylistsResponse = restTemplate.exchange("/api/soundtracker/user-favorites//all-user-playlists",
                HttpMethod.GET,
                new HttpEntity<>(createHeader(user1Token)),
                UserFavoritesDTO.class);

        Assertions.assertThat(allPlaylistsResponse.getBody().getUserPlaylists()).containsKey("newPlaylist");

        String playlistKey = allPlaylistsResponse.getBody().getUserPlaylists().keySet().stream().findFirst().orElseThrow();

        ResponseEntity<UserFavoritesDTO> deletedPlaylistResponse = restTemplate.exchange("/api/soundtracker/user-favorites/user-playlist/" + playlistKey,
                HttpMethod.DELETE,
                new HttpEntity<>(createHeader(user1Token)),
                UserFavoritesDTO.class);

        Assertions.assertThat(deletedPlaylistResponse.getBody().getUserPlaylists()).isEmpty();
    }
    @Test
    @Order(5)
    void registeredUserPushesExistingInternalPlaylistToStreamingServiceAccount() {

    }

    public HttpHeaders createHeader(String token) {
        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", "Bearer " + token);
        return header;
    }


}