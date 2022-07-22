package com.github.NicolaiKopka.web_page_services.user_pages.user_favorites;

import com.github.NicolaiKopka.api_services.MovieDBApiConnect;
import com.github.NicolaiKopka.api_services.SpotifyApiConnect;
import com.github.NicolaiKopka.db_models.movieDBModels.Movie;
import com.github.NicolaiKopka.db_models.spotifyModels.spotifyPlaylistModels.AddPlaylistTransferData;
import com.github.NicolaiKopka.db_models.spotifyModels.spotifyPlaylistModels.SpotifyPlaylist;
import com.github.NicolaiKopka.db_models.spotifyModels.spotifyPlaylistModels.SpotifyUserPlaylists;
import com.github.NicolaiKopka.users.MyUser;
import com.github.NicolaiKopka.users.MyUserRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserFavoritesServiceTest {

    @Test
    void shouldReturnListOfFavoriteMovies() {
        MyUser user = MyUser.builder().username("user").id("1234").build();
        UserFavoritesSaveObject saveObject = UserFavoritesSaveObject.builder().movieIds(List.of(1, 2)).build();
        Movie movie1 = Movie.builder().title("movie1").build();
        Movie movie2 = Movie.builder().title("movie2").build();

        SpotifyApiConnect spotifyApiConnect = Mockito.mock(SpotifyApiConnect.class);

        MyUserRepo myUserRepo = Mockito.mock(MyUserRepo.class);
        Mockito.when(myUserRepo.findByUsername("user")).thenReturn(Optional.of(user));

        UserFavoritesRepo userFavoritesRepo = Mockito.mock(UserFavoritesRepo.class);
        Mockito.when(userFavoritesRepo.findByUserId("1234")).thenReturn(Optional.of(saveObject));

        MovieDBApiConnect movieDBApiConnect = Mockito.mock(MovieDBApiConnect.class);
        Mockito.when(movieDBApiConnect.getMovieById(1)).thenReturn(movie1);
        Mockito.when(movieDBApiConnect.getMovieById(2)).thenReturn(movie2);

        UserFavoritesService userFavoritesService = new UserFavoritesService(myUserRepo, userFavoritesRepo, movieDBApiConnect, spotifyApiConnect);
        Collection<Movie> actual = userFavoritesService.getAllFavoriteMoviesFromDbByUser("user");

        Assertions.assertThat(actual).contains(movie1, movie2);
    }

    @Test
    void shouldThrowIfUserNotFoundOnGetFavorites() {
        MyUser user = MyUser.builder().username("noUser").id("1234").build();

        MyUserRepo myUserRepo = Mockito.mock(MyUserRepo.class);
        Mockito.when(myUserRepo.findByUsername("noUser")).thenReturn(Optional.empty());

        SpotifyApiConnect spotifyApiConnect = Mockito.mock(SpotifyApiConnect.class);

        UserFavoritesRepo userFavoritesRepo = Mockito.mock(UserFavoritesRepo.class);

        MovieDBApiConnect movieDBApiConnect = Mockito.mock(MovieDBApiConnect.class);

        UserFavoritesService userFavoritesService = new UserFavoritesService(myUserRepo, userFavoritesRepo, movieDBApiConnect, spotifyApiConnect);

        try{
            userFavoritesService.getAllFavoriteMoviesFromDbByUser(user.getUsername());
            fail();
        } catch (NoSuchElementException e) {}
    }

    @Test
    void shouldThrowIfFavoritesNotFoundOnGetFavorites() {
        MyUser user = MyUser.builder().username("user").id("1234").build();

        MyUserRepo myUserRepo = Mockito.mock(MyUserRepo.class);
        Mockito.when(myUserRepo.findByUsername("user")).thenReturn(Optional.of(user));

        SpotifyApiConnect spotifyApiConnect = Mockito.mock(SpotifyApiConnect.class);

        UserFavoritesRepo userFavoritesRepo = Mockito.mock(UserFavoritesRepo.class);
        Mockito.when(userFavoritesRepo.findByUserId("1234")).thenReturn(Optional.empty());

        MovieDBApiConnect movieDBApiConnect = Mockito.mock(MovieDBApiConnect.class);

        UserFavoritesService userFavoritesService = new UserFavoritesService(myUserRepo, userFavoritesRepo, movieDBApiConnect, spotifyApiConnect);

        try{
            userFavoritesService.getAllFavoriteMoviesFromDbByUser(user.getUsername());
            fail();
        } catch (NoSuchElementException e) {}
    }

    @Test
    void shouldAddMovieIdToFavoriteObject() {
        MyUser user = MyUser.builder().username("user").id("1234").build();

        UserFavoritesSaveObject existingFavorites = new UserFavoritesSaveObject();
        existingFavorites.setUserId("1234");

        MyUserRepo myUserRepo = Mockito.mock(MyUserRepo.class);
        Mockito.when(myUserRepo.findByUsername("user")).thenReturn(Optional.of(user));

        UserFavoritesRepo userFavoritesRepo = Mockito.mock(UserFavoritesRepo.class);
        Mockito.when(userFavoritesRepo.findByUserId("1234")).thenReturn(Optional.of(existingFavorites));

        SpotifyApiConnect spotifyApiConnect = Mockito.mock(SpotifyApiConnect.class);

        MovieDBApiConnect movieDBApiConnect = Mockito.mock(MovieDBApiConnect.class);

        UserFavoritesSaveObject expectedFavorites = new UserFavoritesSaveObject();
        expectedFavorites.setUserId("1234");
        expectedFavorites.setMovieIds(List.of(2));

        UserFavoritesService userFavoritesService = new UserFavoritesService(myUserRepo, userFavoritesRepo, movieDBApiConnect, spotifyApiConnect);
        userFavoritesService.addMovieToFavorites(2, "user");

        Mockito.verify(userFavoritesRepo).save(expectedFavorites);
    }

    @Test
    void shouldCreateFavoriteObjectAndAddMovieId() {
        MyUser user = MyUser.builder().username("user").id("1234").build();

        MyUserRepo myUserRepo = Mockito.mock(MyUserRepo.class);
        Mockito.when(myUserRepo.findByUsername("user")).thenReturn(Optional.of(user));

        UserFavoritesRepo userFavoritesRepo = Mockito.mock(UserFavoritesRepo.class);
        Mockito.when(userFavoritesRepo.findByUserId("1234")).thenReturn(Optional.empty());

        SpotifyApiConnect spotifyApiConnect = Mockito.mock(SpotifyApiConnect.class);

        MovieDBApiConnect movieDBApiConnect = Mockito.mock(MovieDBApiConnect.class);

        UserFavoritesSaveObject expectedFavorites = UserFavoritesSaveObject.builder()
                .userId("1234")
                .movieIds(List.of(1))
                .build();

        UserFavoritesService userFavoritesService = new UserFavoritesService(myUserRepo, userFavoritesRepo, movieDBApiConnect, spotifyApiConnect);
        userFavoritesService.addMovieToFavorites(1, "user");

        Mockito.verify(userFavoritesRepo).save(expectedFavorites);
    }

    @Test
    void shouldThrowIfUserNotFoundOnAddMovieID() {
        MyUser user = MyUser.builder().username("noUser").id("1234").build();

        MyUserRepo myUserRepo = Mockito.mock(MyUserRepo.class);
        Mockito.when(myUserRepo.findByUsername("noUser")).thenReturn(Optional.empty());

        SpotifyApiConnect spotifyApiConnect = Mockito.mock(SpotifyApiConnect.class);

        UserFavoritesRepo userFavoritesRepo = Mockito.mock(UserFavoritesRepo.class);

        MovieDBApiConnect movieDBApiConnect = Mockito.mock(MovieDBApiConnect.class);

        UserFavoritesService userFavoritesService = new UserFavoritesService(myUserRepo, userFavoritesRepo, movieDBApiConnect, spotifyApiConnect);

        try{
            userFavoritesService.addMovieToFavorites(1, user.getUsername());
            fail();
        } catch (NoSuchElementException e) {}
    }

    @Test
    void shouldDeleteMovieIdFromFavoritesList() {
        MyUser user = MyUser.builder().username("testUser").id("1234").build();
        UserFavoritesSaveObject favoritesObject = new UserFavoritesSaveObject();
        favoritesObject.addMovieId(12);
        favoritesObject.addMovieId(13);
        favoritesObject.addMovieId(14);
        UserFavoritesSaveObject expectedFavorites = UserFavoritesSaveObject.builder().movieIds(List.of(12, 14)).build();

        MyUserRepo myUserRepo = Mockito.mock(MyUserRepo.class);
        Mockito.when(myUserRepo.findByUsername("testUser")).thenReturn(Optional.of(user));

        UserFavoritesRepo userFavoritesRepo = Mockito.mock(UserFavoritesRepo.class);
        Mockito.when(userFavoritesRepo.findByUserId("1234")).thenReturn(Optional.of(favoritesObject));

        SpotifyApiConnect spotifyApiConnect = Mockito.mock(SpotifyApiConnect.class);

        MovieDBApiConnect movieDBApiConnect = Mockito.mock(MovieDBApiConnect.class);

        UserFavoritesService userFavoritesService = new UserFavoritesService(myUserRepo, userFavoritesRepo, movieDBApiConnect, spotifyApiConnect);
        userFavoritesService.deleteMovieFromFavorites(13, "testUser");

        Mockito.verify(userFavoritesRepo).save(expectedFavorites);
    }

    @Test
    void shouldReturnAllSpotifyPlaylistsObjectByUser() {
        MyUser user = MyUser.builder().username("testUser").spotifyId("spotifyId").build();

        SpotifyPlaylist spotifyPlaylist1 = new SpotifyPlaylist();
        spotifyPlaylist1.setName("playlist1");

        SpotifyPlaylist spotifyPlaylist2 = new SpotifyPlaylist();
        spotifyPlaylist2.setName("playlist2");

        SpotifyUserPlaylists spotifyUserPlaylists = new SpotifyUserPlaylists();
        spotifyUserPlaylists.setItems(List.of(spotifyPlaylist1, spotifyPlaylist2));

        MyUserRepo myUserRepo = Mockito.mock(MyUserRepo.class);
        Mockito.when(myUserRepo.findByUsername("testUser")).thenReturn(Optional.of(user));

        SpotifyApiConnect spotifyApiConnect = Mockito.mock(SpotifyApiConnect.class);
        Mockito.when(spotifyApiConnect.getAllUserPlaylists("1234", "spotifyId"))
                .thenReturn(spotifyUserPlaylists);

        MovieDBApiConnect movieDBApiConnect = Mockito.mock(MovieDBApiConnect.class);

        UserFavoritesRepo userFavoritesRepo = Mockito.mock(UserFavoritesRepo.class);

        UserFavoritesService userFavoritesService = new UserFavoritesService(myUserRepo, userFavoritesRepo, movieDBApiConnect, spotifyApiConnect);
        SpotifyUserPlaylists actual = userFavoritesService.getAllSpotifyPlaylistsFromUser("testUser", "1234");

        Assertions.assertThat(actual).isEqualTo(spotifyUserPlaylists);
    }

    @Test
    void shouldAddNewSpotifyPlaylist() {
        MyUser user = MyUser.builder().username("testUser").spotifyId("spotifyId").build();

        AddPlaylistTransferData data = new AddPlaylistTransferData();
        data.setName("playlist1");

        SpotifyPlaylist spotifyPlaylist = new SpotifyPlaylist();
        spotifyPlaylist.setName("playlist1");

        MyUserRepo myUserRepo = Mockito.mock(MyUserRepo.class);
        Mockito.when(myUserRepo.findByUsername("testUser")).thenReturn(Optional.of(user));

        SpotifyApiConnect spotifyApiConnect = Mockito.mock(SpotifyApiConnect.class);
        Mockito.when(spotifyApiConnect.addSpotifyPlaylist(
                "1234",
                "spotifyId",
                data)).thenReturn(spotifyPlaylist);

        MovieDBApiConnect movieDBApiConnect = Mockito.mock(MovieDBApiConnect.class);

        UserFavoritesRepo userFavoritesRepo = Mockito.mock(UserFavoritesRepo.class);

        UserFavoritesService userFavoritesService = new UserFavoritesService(myUserRepo, userFavoritesRepo, movieDBApiConnect, spotifyApiConnect);

        SpotifyPlaylist actual = userFavoritesService.addSpotifyPlaylist("1234", "testUser", data);

        Assertions.assertThat(actual).isEqualTo(spotifyPlaylist);
    }
}