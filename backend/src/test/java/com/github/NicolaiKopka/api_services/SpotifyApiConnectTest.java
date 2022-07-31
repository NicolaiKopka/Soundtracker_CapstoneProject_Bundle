package com.github.NicolaiKopka.api_services;

import com.github.NicolaiKopka.db_models.AlbumReferenceDTO;
import com.github.NicolaiKopka.db_models.spotifyModels.*;
import com.github.NicolaiKopka.db_models.spotifyModels.authenticationModels.SpotifyOAuthResponse;
import com.github.NicolaiKopka.db_models.spotifyModels.spotifyPlaylistModels.AddPlaylistTransferData;
import com.github.NicolaiKopka.db_models.spotifyModels.spotifyPlaylistModels.SpotifyPlaylist;
import com.github.NicolaiKopka.db_models.spotifyModels.spotifyPlaylistModels.SpotifyUserPlaylists;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SpotifyApiConnectTest {

    @Test
    void shouldReturnListOfSoundtrackAlbums(){
        SpotifyAlbum spotifyAlbum1 = new SpotifyAlbum();
        spotifyAlbum1.setId("album1");
        spotifyAlbum1.setExternalURLs(new SpotifyAlbumExternalURLs());

        SpotifyAlbum spotifyAlbum2 = new SpotifyAlbum();
        spotifyAlbum2.setId("album2");
        spotifyAlbum2.setExternalURLs(new SpotifyAlbumExternalURLs());

        SpotifyAlbumQueryObject spotifyAlbumQueryObject = new SpotifyAlbumQueryObject();
        spotifyAlbumQueryObject.setAlbumQueryList(List.of(spotifyAlbum1, spotifyAlbum2));

        SpotifyFirstQueryObject spotifyFirstQueryObject = new SpotifyFirstQueryObject();
        spotifyFirstQueryObject.setAlbums(spotifyAlbumQueryObject);


        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", "Bearer accessToken");
        Mockito.when(restTemplate.exchange("https://api.spotify.com/v1/search?q=searchAlbum&type=album",
                HttpMethod.GET,
                new HttpEntity<>(header),
                SpotifyFirstQueryObject.class)).thenReturn(ResponseEntity.of(Optional.of(spotifyFirstQueryObject)));

        SpotifyApiConnect spotifyApiConnect = new SpotifyApiConnect(restTemplate, "testId", "testSecret");

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "client_credentials");
        HttpHeaders accessHeader = new HttpHeaders();
        accessHeader.setBasicAuth("testId", "testSecret");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, accessHeader);

        SpotifyOAuthResponse response = new SpotifyOAuthResponse();
        response.setAccessToken("accessToken");

        Mockito.when(restTemplate.exchange("https://accounts.spotify.com/api/token",
                HttpMethod.POST,
                request,
                SpotifyOAuthResponse.class
                )).thenReturn(ResponseEntity.of(Optional.of(response)));

        List<AlbumReferenceDTO> actual = spotifyApiConnect.getSpotifyListOfMovieAlbums("searchAlbum");

        Assertions.assertThat(actual.get(0).getSpotifyAlbumId()).isEqualTo("album1");
        Assertions.assertThat(actual.get(1).getSpotifyAlbumId()).isEqualTo("album2");
}

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
    void shouldGetSpotifyAlbumById(){
        SpotifyTrack track1 = new SpotifyTrack();
        track1.setName("track1");
        SpotifyTrack track2 = new SpotifyTrack();
        track2.setName("track2");

        SpotifyAlbum spotifyAlbum = new SpotifyAlbum();
        spotifyAlbum.setTracks(new SpotifyMultiTracks(List.of(track1, track2)));

        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "client_credentials");
        HttpHeaders accessHeader = new HttpHeaders();
        accessHeader.setBasicAuth("testId", "testSecret");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, accessHeader);

        SpotifyOAuthResponse response = new SpotifyOAuthResponse();
        response.setAccessToken("accessToken");

        Mockito.when(restTemplate.exchange("https://accounts.spotify.com/api/token",
                HttpMethod.POST,
                request,
                SpotifyOAuthResponse.class
        )).thenReturn(ResponseEntity.of(Optional.of(response)));

        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", "Bearer accessToken");

        Mockito.when(restTemplate.exchange(
                "https://api.spotify.com/v1/albums/5678",
                HttpMethod.GET,
                new HttpEntity<>(header),
                SpotifyAlbum.class
        )).thenReturn(ResponseEntity.of(Optional.of(spotifyAlbum)));

        SpotifyApiConnect spotifyApiConnect = new SpotifyApiConnect(restTemplate, "testId", "testSecret");
        List<SpotifyTrack> actual = spotifyApiConnect.getSpotifyAlbumTracksById("5678");

        Assertions.assertThat(actual).contains(track1, track2);
    }
}