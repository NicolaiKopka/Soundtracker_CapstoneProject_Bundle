package com.github.NicolaiKopka.api_services;

import com.github.NicolaiKopka.db_models.spotifyModels.*;
import com.github.NicolaiKopka.db_models.spotifyModels.spotifyPlaylistModels.AddPlaylistTransferData;
import com.github.NicolaiKopka.db_models.spotifyModels.spotifyPlaylistModels.SpotifyPlaylist;
import com.github.NicolaiKopka.db_models.spotifyModels.spotifyPlaylistModels.SpotifyUserPlaylists;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SpotifyApiConnectTest {

//    @Test
//    void shouldReturnListOfSoundtrackAlbums() throws OAuthProblemException, OAuthSystemException {
//        SpotifyAlbum spotifyAlbum1 = new SpotifyAlbum();
//        spotifyAlbum1.setName("album1");
//
//        SpotifyAlbum spotifyAlbum2 = new SpotifyAlbum();
//        spotifyAlbum2.setName("album2");
//
//        SpotifyAlbumQueryObject spotifyAlbumQueryObject = new SpotifyAlbumQueryObject();
//        spotifyAlbumQueryObject.setAlbumQueryList(List.of(spotifyAlbum1, spotifyAlbum2));
//
//        SpotifyFirstQueryObject spotifyFirstQueryObject = new SpotifyFirstQueryObject();
//        spotifyFirstQueryObject.setAlbums(spotifyAlbumQueryObject);
//
//
//        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
//        HttpHeaders header = new HttpHeaders();
//        header.set("Authorization", "Bearer accessToken");
//        Mockito.when(restTemplate.exchange("https://api.spotify.com/v1/search?q=searchAlbum&type=album",
//                HttpMethod.GET,
//                new HttpEntity<>(header),
//                SpotifyFirstQueryObject.class)).thenReturn(ResponseEntity.of(Optional.of(spotifyFirstQueryObject)));
//
//        SpotifyApiConnect spotifyApiConnect = new SpotifyApiConnect(restTemplate, "testId", "testSecret");
//        Mockito.when(spotifyApiConnect.getAccessToken()).thenReturn("accessToken");
//
//        List<SpotifyAlbum> actual = spotifyApiConnect.getSpotifyListOfMovieAlbums("searchAlbum");
//
//        Assertions.assertThat(actual).contains(spotifyAlbum1, spotifyAlbum2);
//}

    @Test
    void shouldReturnAllSpotifyPlaylistsObjectByUser() {
        SpotifyPlaylist spotifyPlaylist1 = new SpotifyPlaylist();
        spotifyPlaylist1.setName("playlist1");

        SpotifyPlaylist spotifyPlaylist2 = new SpotifyPlaylist();
        spotifyPlaylist2.setName("playlist2");

        SpotifyUserPlaylists spotifyUserPlaylists = new SpotifyUserPlaylists();
        spotifyUserPlaylists.setItems(List.of(spotifyPlaylist1, spotifyPlaylist2));

        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", "Bearer 1234");
        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
        Mockito.when(restTemplate.exchange(
                "https://api.spotify.com/v1/users/spotifyId/playlists",
                HttpMethod.GET,
                new HttpEntity<>(header),
                SpotifyUserPlaylists.class)
        ).thenReturn(ResponseEntity.of(Optional.of(spotifyUserPlaylists)));

        SpotifyApiConnect spotifyApiConnect = new SpotifyApiConnect(restTemplate, "testId", "testSecret");
        SpotifyUserPlaylists actual = spotifyApiConnect.getAllUserPlaylists("1234", "spotifyId");
        Assertions.assertThat(actual).isEqualTo(spotifyUserPlaylists);
    }

    @Test
    void shouldAddPlaylistToSpotify() {
        AddPlaylistTransferData data = new AddPlaylistTransferData();
        data.setName("playlist1");

        SpotifyPlaylist spotifyPlaylist = new SpotifyPlaylist();
        spotifyPlaylist.setName("playlist1");

        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", "Bearer 1234");
        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
        Mockito.when(restTemplate.exchange(
                "https://api.spotify.com/v1/users/spotifyId/playlists",
                HttpMethod.POST,
                new HttpEntity<>(data, header),
                SpotifyPlaylist.class
        )).thenReturn(ResponseEntity.of(Optional.of(spotifyPlaylist)));

        SpotifyApiConnect spotifyApiConnect = new SpotifyApiConnect(restTemplate, "testId", "testSecret");
        SpotifyPlaylist actual = spotifyApiConnect.addSpotifyPlaylist("1234", "spotifyId", data);
        Assertions.assertThat(actual).isEqualTo(spotifyPlaylist);
    }

    @Test
    void shouldThrowIfPlaylistIsCollaborativeWhileAlsoPublic() {
        AddPlaylistTransferData data = new AddPlaylistTransferData();
        data.setName("playlist1");
        data.setPublic(true);
        data.setCollaborative(true);

        SpotifyPlaylist spotifyPlaylist = new SpotifyPlaylist();
        spotifyPlaylist.setName("playlist1");

        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", "Bearer 1234");
        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
        Mockito.when(restTemplate.exchange(
                "https://api.spotify.com/v1/users/spotifyId/playlists",
                HttpMethod.POST,
                new HttpEntity<>(data, header),
                SpotifyPlaylist.class
        )).thenReturn(ResponseEntity.of(Optional.of(spotifyPlaylist)));

        SpotifyApiConnect spotifyApiConnect = new SpotifyApiConnect(restTemplate, "testId", "testSecret");

        try {
            spotifyApiConnect.addSpotifyPlaylist("1234", "spotifyId", data);
            fail();
        } catch (IllegalArgumentException e) {
            Assertions.assertThat(e.getMessage()).isEqualTo("A collaborative playlist list must be private");
        }
    }

    @Test
    void shouldGetSpotifyAlbumById() {
        SpotifyTrack track1 = new SpotifyTrack();
        track1.setName("track1");
        SpotifyTrack track2 = new SpotifyTrack();
        track2.setName("track2");

        SpotifyAlbum spotifyAlbum = new SpotifyAlbum();
        spotifyAlbum.setTracks(new SpotifyMultiTracks(List.of(track1, track2)));

        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", "Bearer 1234");
        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
        Mockito.when(restTemplate.exchange(
                "https://api.spotify.com/v1/albums/5678",
                HttpMethod.GET,
                new HttpEntity<>(header),
                SpotifyAlbum.class
        )).thenReturn(ResponseEntity.of(Optional.of(spotifyAlbum)));

        SpotifyApiConnect spotifyApiConnect = new SpotifyApiConnect(restTemplate, "test", "test");
        List<SpotifyTrack> actual = spotifyApiConnect.getSpotifyAlbumTracksById("1234", "5678");

        Assertions.assertThat(actual).contains(track1, track2);
    }
}