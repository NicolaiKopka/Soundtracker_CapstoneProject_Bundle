package com.github.NicolaiKopka.web_page_services.user_pages.user_favorites;

import com.github.NicolaiKopka.api_services.MovieDBApiConnect;
import com.github.NicolaiKopka.api_services.SpotifyApiConnect;
import com.github.NicolaiKopka.db_models.movieDBModels.Movie;
import com.github.NicolaiKopka.db_models.spotifyModels.spotifyPlaylistModels.AddPlaylistTransferData;
import com.github.NicolaiKopka.db_models.spotifyModels.spotifyPlaylistModels.SpotifyPlaylist;
import com.github.NicolaiKopka.db_models.spotifyModels.spotifyPlaylistModels.SpotifyUserPlaylists;
import com.github.NicolaiKopka.db_models.userPlaylistModels.UserPlaylist;
import com.github.NicolaiKopka.db_models.userPlaylistModels.UserPlaylistSendObject;
import com.github.NicolaiKopka.users.MyUser;
import com.github.NicolaiKopka.users.MyUserRepo;
import lombok.NoArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.User;

import java.util.*;

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
        } catch (NoSuchElementException ignored) {}
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
        } catch (NoSuchElementException ignored) {}
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
        } catch (NoSuchElementException ignored) {}
    }
    @Test
    void shouldDeleteMovieIdFromFavoritesList() {
        MyUser user = MyUser.builder().username("testUser").id("1234").build();
        UserFavoritesSaveObject favoritesObject = new UserFavoritesSaveObject();
        favoritesObject.addMovieId(12);
        favoritesObject.addMovieId(13);
        favoritesObject.addMovieId(14);
        UserFavoritesSaveObject expectedFavorites = UserFavoritesSaveObject.builder()
                .movieIds(List.of(12, 14))
                .userPlaylists(new HashMap<>())
                .build();

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

    @Test
    void shouldCreateNewUserPlaylist() {
        MyUser user = MyUser.builder().username("testUser").id("userId").build();

        UserPlaylist userPlaylist = new UserPlaylist();
        userPlaylist.setPlaylistName("newPlaylist");

        Map<String, UserPlaylist> newPlaylist = new HashMap<>();
        newPlaylist.put("newPlaylist", userPlaylist);

        UserFavoritesSaveObject saveObject = UserFavoritesSaveObject.builder().id("userId")
                .userPlaylists(new HashMap<>())
                .build();
        UserFavoritesSaveObject expected = UserFavoritesSaveObject.builder().id("userId")
                .userPlaylists(newPlaylist)
                .build();

        MyUserRepo myUserRepo = Mockito.mock(MyUserRepo.class);
        Mockito.when(myUserRepo.findByUsername("testUser")).thenReturn(Optional.of(user));

        UserFavoritesRepo userFavoritesRepo = Mockito.mock(UserFavoritesRepo.class);
        Mockito.when(userFavoritesRepo.findByUserId("userId")).thenReturn(Optional.of(saveObject));

        MovieDBApiConnect movieDBApiConnect = Mockito.mock(MovieDBApiConnect.class);

        SpotifyApiConnect spotifyApiConnect = Mockito.mock(SpotifyApiConnect.class);

        UserFavoritesService userFavoritesService = new UserFavoritesService(myUserRepo, userFavoritesRepo, movieDBApiConnect, spotifyApiConnect);
        userFavoritesService.createNewUserPlaylist("testUser", "newPlaylist");

        Mockito.verify(userFavoritesRepo).save(expected);
    }

    @Test
    void shouldThrowIfUserPlaylistNameExistsOnCreation() {
        MyUser user = MyUser.builder().username("testUser").id("userId").build();

        UserPlaylist userPlaylist = new UserPlaylist();
        userPlaylist.setPlaylistName("newPlaylist");

        Map<String, UserPlaylist> newPlaylist = new HashMap<>();
        newPlaylist.put("newPlaylist", userPlaylist);

        UserFavoritesSaveObject saveObject = UserFavoritesSaveObject.builder().id("userId")
                .userPlaylists(newPlaylist)
                .build();

        MyUserRepo myUserRepo = Mockito.mock(MyUserRepo.class);
        Mockito.when(myUserRepo.findByUsername("testUser")).thenReturn(Optional.of(user));

        UserFavoritesRepo userFavoritesRepo = Mockito.mock(UserFavoritesRepo.class);
        Mockito.when(userFavoritesRepo.findByUserId("userId")).thenReturn(Optional.of(saveObject));

        MovieDBApiConnect movieDBApiConnect = Mockito.mock(MovieDBApiConnect.class);

        SpotifyApiConnect spotifyApiConnect = Mockito.mock(SpotifyApiConnect.class);

        UserFavoritesService userFavoritesService = new UserFavoritesService(myUserRepo, userFavoritesRepo, movieDBApiConnect, spotifyApiConnect);

        Assertions.assertThatThrownBy(() -> userFavoritesService.createNewUserPlaylist("testUser", "newPlaylist"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Playlist already exists");
    }

    @Test
    void shouldThrowIfUserPlaylistNameIsInvalid() {
        MyUser user = MyUser.builder().username("testUser").id("userId").build();

        UserPlaylist userPlaylist = new UserPlaylist();
        userPlaylist.setPlaylistName("playlist1");

        Map<String, UserPlaylist> newPlaylist = new HashMap<>();
        newPlaylist.put("playlist1", userPlaylist);

        UserFavoritesSaveObject saveObject = UserFavoritesSaveObject.builder().id("userId")
                .userPlaylists(newPlaylist)
                .build();

        MyUserRepo myUserRepo = Mockito.mock(MyUserRepo.class);
        Mockito.when(myUserRepo.findByUsername("testUser")).thenReturn(Optional.of(user));

        UserFavoritesRepo userFavoritesRepo = Mockito.mock(UserFavoritesRepo.class);
        Mockito.when(userFavoritesRepo.findByUserId("userId")).thenReturn(Optional.of(saveObject));

        MovieDBApiConnect movieDBApiConnect = Mockito.mock(MovieDBApiConnect.class);

        SpotifyApiConnect spotifyApiConnect = Mockito.mock(SpotifyApiConnect.class);

        UserFavoritesService userFavoritesService = new UserFavoritesService(myUserRepo, userFavoritesRepo, movieDBApiConnect, spotifyApiConnect);

        Assertions.assertThatThrownBy(() -> userFavoritesService.createNewUserPlaylist("testUser", "New Playlist"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The chosen playlist name is not compatible");

        Assertions.assertThatThrownBy(() -> userFavoritesService.createNewUserPlaylist("testUser", ""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The chosen playlist name is not compatible");

        Assertions.assertThatThrownBy(() -> userFavoritesService.createNewUserPlaylist("testUser", " "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The chosen playlist name is not compatible");
    }

    @Test
    void shouldCreatePlaylistAndAddTracks() {
        MyUser user = MyUser.builder().username("testUser").id("userId").build();

        UserPlaylistSendObject sendObject = UserPlaylistSendObject.builder()
                .playlistName("playlist1")
                .spotifyTrackId("1234")
                .deezerTrackId("5678").build();

        UserFavoritesSaveObject expected = UserFavoritesSaveObject.builder().userId("userId")
                .userPlaylists(Map.of("playlist1",
                        new UserPlaylist(
                                "playlist1",
                                List.of("1234"),
                                List.of("5678")
                        ))).build();

        MyUserRepo myUserRepo = Mockito.mock(MyUserRepo.class);
        Mockito.when(myUserRepo.findByUsername("testUser")).thenReturn(Optional.of(user));

        UserFavoritesSaveObject saveObject = UserFavoritesSaveObject.builder().userId("userId")
                .userPlaylists(new HashMap<>())
                .build();

        UserFavoritesRepo userFavoritesRepo = Mockito.mock(UserFavoritesRepo.class);
        Mockito.when(userFavoritesRepo.findByUserId("userId")).thenReturn(Optional.of(saveObject));

        MovieDBApiConnect movieDBApiConnect = Mockito.mock(MovieDBApiConnect.class);

        SpotifyApiConnect spotifyApiConnect = Mockito.mock(SpotifyApiConnect.class);

        UserFavoritesService userFavoritesService = new UserFavoritesService(myUserRepo, userFavoritesRepo, movieDBApiConnect, spotifyApiConnect);

        userFavoritesService.addTrackToUserPlaylist("testUser", sendObject);

        Mockito.verify(userFavoritesRepo).save(expected);
    }

    @Test
    void shouldAddTracksToExistingPlaylist() {
        MyUser user = MyUser.builder().username("testUser").id("userId").build();

        UserPlaylistSendObject sendObject = UserPlaylistSendObject.builder()
                .playlistName("playlist1")
                .spotifyTrackId("1234")
                .deezerTrackId("5678").build();

        UserFavoritesSaveObject expected = UserFavoritesSaveObject.builder().userId("userId")
                .userPlaylists(Map.of("playlist1",
                        new UserPlaylist(
                                "playlist1",
                                List.of("1234"),
                                List.of("5678")
                        ))).build();

        MyUserRepo myUserRepo = Mockito.mock(MyUserRepo.class);
        Mockito.when(myUserRepo.findByUsername("testUser")).thenReturn(Optional.of(user));

        UserFavoritesSaveObject saveObject = UserFavoritesSaveObject.builder().userId("userId")
                .userPlaylists(Map.of("playlist1",
                        new UserPlaylist("playlist1", new ArrayList<>(), new ArrayList<>())))
                .build();

        UserFavoritesRepo userFavoritesRepo = Mockito.mock(UserFavoritesRepo.class);
        Mockito.when(userFavoritesRepo.findByUserId("userId")).thenReturn(Optional.of(saveObject));

        MovieDBApiConnect movieDBApiConnect = Mockito.mock(MovieDBApiConnect.class);

        SpotifyApiConnect spotifyApiConnect = Mockito.mock(SpotifyApiConnect.class);

        UserFavoritesService userFavoritesService = new UserFavoritesService(myUserRepo, userFavoritesRepo, movieDBApiConnect, spotifyApiConnect);

        userFavoritesService.addTrackToUserPlaylist("testUser", sendObject);

        Mockito.verify(userFavoritesRepo).save(expected);
    }

    @Test
    void shouldThrowIfTracksAlreadyExistInUserPlaylist() {
        MyUser user = MyUser.builder().username("testUser").id("userId").build();

        UserPlaylistSendObject sendObject = UserPlaylistSendObject.builder()
                .playlistName("playlist1")
                .spotifyTrackId("1234")
                .deezerTrackId("5678").build();

        MyUserRepo myUserRepo = Mockito.mock(MyUserRepo.class);
        Mockito.when(myUserRepo.findByUsername("testUser")).thenReturn(Optional.of(user));

        UserFavoritesSaveObject saveObject = UserFavoritesSaveObject.builder().userId("userId")
                .userPlaylists(Map.of("playlist1",
                        new UserPlaylist("playlist1", List.of("1234"), List.of("5678"))))
                .build();

        UserFavoritesRepo userFavoritesRepo = Mockito.mock(UserFavoritesRepo.class);
        Mockito.when(userFavoritesRepo.findByUserId("userId")).thenReturn(Optional.of(saveObject));

        MovieDBApiConnect movieDBApiConnect = Mockito.mock(MovieDBApiConnect.class);

        SpotifyApiConnect spotifyApiConnect = Mockito.mock(SpotifyApiConnect.class);

        UserFavoritesService userFavoritesService = new UserFavoritesService(myUserRepo, userFavoritesRepo, movieDBApiConnect, spotifyApiConnect);

        Assertions.assertThatThrownBy(() -> userFavoritesService.addTrackToUserPlaylist("testUser", sendObject))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Track already in playlist");
    }

    @Test
    void shouldRemoveTrackIdFromPlaylist() {
        MyUser user = MyUser.builder().username("testUser").id("userId").build();

        UserPlaylistSendObject sendObject = UserPlaylistSendObject.builder()
                .playlistName("playlist1")
                .spotifyTrackId("1234")
                .deezerTrackId("5678").build();

        List<String> spotifyIds = new ArrayList<>();
        spotifyIds.add("1234");
        spotifyIds.add("5555");

        List<String> deezerIds = new ArrayList<>();
        deezerIds.add("5678");
        deezerIds.add("5555");

        MyUserRepo myUserRepo = Mockito.mock(MyUserRepo.class);
        Mockito.when(myUserRepo.findByUsername("testUser")).thenReturn(Optional.of(user));

        UserFavoritesSaveObject saveObject = UserFavoritesSaveObject.builder().userId("userId")
                .userPlaylists(Map.of("playlist1",
                        new UserPlaylist("playlist1", spotifyIds, deezerIds)))
                .build();

        UserFavoritesSaveObject expected = UserFavoritesSaveObject.builder().userId("userId")
                .userPlaylists(Map.of("playlist1",
                        new UserPlaylist("playlist1", List.of("5555"), List.of("5555"))))
                .build();

        UserFavoritesRepo userFavoritesRepo = Mockito.mock(UserFavoritesRepo.class);
        Mockito.when(userFavoritesRepo.findByUserId("userId")).thenReturn(Optional.of(saveObject));

        MovieDBApiConnect movieDBApiConnect = Mockito.mock(MovieDBApiConnect.class);

        SpotifyApiConnect spotifyApiConnect = Mockito.mock(SpotifyApiConnect.class);

        UserFavoritesService userFavoritesService = new UserFavoritesService(myUserRepo, userFavoritesRepo, movieDBApiConnect, spotifyApiConnect);
        userFavoritesService.removeTrackFromPlaylist("testUser", sendObject);

        Mockito.verify(userFavoritesRepo).save(expected);
    }

    @Test
    void shouldThrowIfNoPlaylistFoundOnDeleteTracks() {
        MyUser user = MyUser.builder().username("testUser").id("userId").build();

        UserPlaylistSendObject sendObject = UserPlaylistSendObject.builder()
                .playlistName("playlist1")
                .spotifyTrackId("1234")
                .deezerTrackId("5678").build();

        MyUserRepo myUserRepo = Mockito.mock(MyUserRepo.class);
        Mockito.when(myUserRepo.findByUsername("testUser")).thenReturn(Optional.of(user));

        UserFavoritesSaveObject saveObject = UserFavoritesSaveObject.builder().userId("userId")
                .userPlaylists(Map.of("playlist2",
                        new UserPlaylist("playlist2", List.of("123"), List.of("123"))))
                .build();

        UserFavoritesRepo userFavoritesRepo = Mockito.mock(UserFavoritesRepo.class);
        Mockito.when(userFavoritesRepo.findByUserId("userId")).thenReturn(Optional.of(saveObject));

        MovieDBApiConnect movieDBApiConnect = Mockito.mock(MovieDBApiConnect.class);

        SpotifyApiConnect spotifyApiConnect = Mockito.mock(SpotifyApiConnect.class);

        UserFavoritesService userFavoritesService = new UserFavoritesService(myUserRepo, userFavoritesRepo, movieDBApiConnect, spotifyApiConnect);

        Assertions.assertThatThrownBy(() -> userFavoritesService.removeTrackFromPlaylist("testUser", sendObject))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Playlist not found");
    }

    @Test
    void shouldRemoveExistingUserPlaylist() {
        MyUser user = MyUser.builder().username("testUser").id("userId").build();

        UserPlaylist playlist1 = new UserPlaylist();
        playlist1.setPlaylistName("playlist1");
        playlist1.setSpotifyTrackIds(new ArrayList<>());
        playlist1.setDeezerTrackIds(new ArrayList<>());

        UserPlaylist playlist2 = new UserPlaylist();
        playlist2.setPlaylistName("playlist2");
        playlist2.setSpotifyTrackIds(new ArrayList<>());
        playlist2.setDeezerTrackIds(new ArrayList<>());


        Map<String, UserPlaylist> originalPlaylists = new HashMap<>();
        originalPlaylists.put("playlist1", playlist1);
        originalPlaylists.put("playlist2", playlist2);

        Map<String, UserPlaylist> expectedPlaylists = new HashMap<>();
        expectedPlaylists.put("playlist1", playlist1);

        MyUserRepo myUserRepo = Mockito.mock(MyUserRepo.class);
        Mockito.when(myUserRepo.findByUsername("testUser")).thenReturn(Optional.of(user));

        UserFavoritesSaveObject saveObject = UserFavoritesSaveObject.builder().userId("userId")
                .userPlaylists(originalPlaylists).build();

        UserFavoritesSaveObject expected = UserFavoritesSaveObject.builder().userId("userId")
                .userPlaylists(expectedPlaylists).build();

        UserFavoritesRepo userFavoritesRepo = Mockito.mock(UserFavoritesRepo.class);
        Mockito.when(userFavoritesRepo.findByUserId("userId")).thenReturn(Optional.of(saveObject));

        MovieDBApiConnect movieDBApiConnect = Mockito.mock(MovieDBApiConnect.class);

        SpotifyApiConnect spotifyApiConnect = Mockito.mock(SpotifyApiConnect.class);

        UserFavoritesService userFavoritesService = new UserFavoritesService(myUserRepo, userFavoritesRepo, movieDBApiConnect, spotifyApiConnect);
        userFavoritesService.removeUserPlaylist("testUser", "playlist2");

        Mockito.verify(userFavoritesRepo).save(expected);
    }
    @Test
    void shouldThrowIfPlaylistToDeleteDoesNotExist() {
        MyUser user = MyUser.builder().username("testUser").id("userId").build();

        MyUserRepo myUserRepo = Mockito.mock(MyUserRepo.class);
        Mockito.when(myUserRepo.findByUsername("testUser")).thenReturn(Optional.of(user));

        UserFavoritesSaveObject saveObject = UserFavoritesSaveObject.builder().userId("userId")
                .userPlaylists(Map.of("playlist1",
                        new UserPlaylist("playlist1", List.of("123"), List.of("123"))))
                .build();

        UserFavoritesRepo userFavoritesRepo = Mockito.mock(UserFavoritesRepo.class);
        Mockito.when(userFavoritesRepo.findByUserId("userId")).thenReturn(Optional.of(saveObject));

        MovieDBApiConnect movieDBApiConnect = Mockito.mock(MovieDBApiConnect.class);

        SpotifyApiConnect spotifyApiConnect = Mockito.mock(SpotifyApiConnect.class);

        UserFavoritesService userFavoritesService = new UserFavoritesService(myUserRepo, userFavoritesRepo, movieDBApiConnect, spotifyApiConnect);

        Assertions.assertThatThrownBy(() -> userFavoritesService.removeUserPlaylist("testUser", "playlist2"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("No Playlist found with this name");
    }
}