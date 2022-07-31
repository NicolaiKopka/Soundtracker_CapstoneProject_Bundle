package com.github.NicolaiKopka.api_services;
import com.github.NicolaiKopka.db_models.AlbumReferenceDTO;
import com.github.NicolaiKopka.db_models.spotifyModels.*;
import com.github.NicolaiKopka.db_models.spotifyModels.authenticationModels.SpotifyOAuthResponse;
import com.github.NicolaiKopka.db_models.spotifyModels.spotifyPlaylistModels.AddPlaylistTransferData;
import com.github.NicolaiKopka.db_models.spotifyModels.spotifyPlaylistModels.SpotifyPlaylist;
import com.github.NicolaiKopka.db_models.spotifyModels.spotifyPlaylistModels.SpotifyUserPlaylists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Objects;

@Service
public class SpotifyApiConnect {

    private final RestTemplate restTemplate;
    private final String SPOTIFY_ID;
    private final String SPOTIFY_SECRET;

    public SpotifyApiConnect(RestTemplate restTemplate, @Value("${spotify.api.id}") String SPOTIFY_ID, @Value("${spotify.api.secret}") String SPOTIFY_SECRET) {
        this.restTemplate = restTemplate;
        this.SPOTIFY_ID = SPOTIFY_ID;
        this.SPOTIFY_SECRET = SPOTIFY_SECRET;
    }

    public String getAccessToken(){
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "client_credentials");
        HttpHeaders headers = createTokenHeaders();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<SpotifyOAuthResponse> accessTokenResponse = restTemplate.exchange(
                "https://accounts.spotify.com/api/token",
                HttpMethod.POST,
                request,
                SpotifyOAuthResponse.class
        );
        return accessTokenResponse.getBody().getAccessToken();
    }
    HttpHeaders createTokenHeaders(){
        HttpHeaders header = new HttpHeaders();
        header.setBasicAuth(SPOTIFY_ID ,SPOTIFY_SECRET);
        return header;
    }

    // TODO find a way to check if correct movie is returned, including netflix
    public List<AlbumReferenceDTO> getSpotifyListOfMovieAlbums(String movieName){
        String accessToken = getAccessToken();

        String queryUrl = "https://api.spotify.com/v1/search?q="
                + movieName
//                + " motion picture"
                + "&type=album";

        ResponseEntity<SpotifyFirstQueryObject> queryResponse = restTemplate.exchange(
                queryUrl,
                HttpMethod.GET,
                new HttpEntity<>(createAuthBearerHeader(accessToken)),
                SpotifyFirstQueryObject.class);

        //Throws NullPointerException if empty
        SpotifyAlbumQueryObject albumQuery = Objects.requireNonNull(queryResponse.getBody()).getAlbums();

        return albumQuery.getAlbumQueryList().stream().map(spotifyAlbum -> {
            AlbumReferenceDTO albumReference = new AlbumReferenceDTO();
            albumReference.setMovieTitle(spotifyAlbum.getName());
            albumReference.setAlbumUrl(spotifyAlbum.getExternalURLs().getAlbumUrl());
            albumReference.setSpotifyReleaseDate(spotifyAlbum.getReleaseDate());
            albumReference.setSpotifyAlbumId(spotifyAlbum.getId());
            return albumReference;
        }).toList();


//        return albumQuery.getAlbumQueryList();
    }
    public SpotifyUserPlaylists getAllUserPlaylists(String spotifyToken, String userId) {
        String queryUrl = "https://api.spotify.com/v1/users/" + userId + "/playlists";
        ResponseEntity<SpotifyUserPlaylists> allUserPlaylists = restTemplate.exchange(
                queryUrl,
                HttpMethod.GET,
                new HttpEntity<>(createAuthBearerHeader(spotifyToken)),
                SpotifyUserPlaylists.class);
        return allUserPlaylists.getBody();
    }

    public List<SpotifyTrack> getSpotifyAlbumTracksById(String id){
        String queryUrl = "https://api.spotify.com/v1/albums/" + id;
        String accessToken = getAccessToken();
        ResponseEntity<SpotifyAlbum> allAlbumTracks = restTemplate.exchange(
                queryUrl,
                HttpMethod.GET,
                new HttpEntity<>(createAuthBearerHeader(accessToken)),
                SpotifyAlbum.class);
        return Objects.requireNonNull(allAlbumTracks.getBody()).getTracks().getItems();
    }

    public SpotifyPlaylist addSpotifyPlaylist(String spotifyToken, String userId, AddPlaylistTransferData data) {
        String queryUrl = "https://api.spotify.com/v1/users/" + userId + "/playlists";

        if(data.isCollaborative() && data.isPublic()) {
            throw new IllegalArgumentException("A collaborative playlist list must be private");
        }

        ResponseEntity<SpotifyPlaylist> addPlaylistResponse = restTemplate.exchange(
                queryUrl,
                HttpMethod.POST,
                new HttpEntity<>(data, createAuthBearerHeader(spotifyToken)),
                SpotifyPlaylist.class
        );
        return addPlaylistResponse.getBody();
    }
    public List<SpotifyTrack> getMultipleSpotifyTracksById(List<String> playlistTracks) {
        String accessToken = getAccessToken();

        StringBuilder builder = new StringBuilder();
        playlistTracks.forEach(track -> {
            builder.append(track);
            builder.append(",");
        });
        builder.deleteCharAt(builder.length() - 1);
        String allTrackIds = builder.toString();

        String queryUrl = "https://api.spotify.com/v1/tracks?ids=" + allTrackIds;

        ResponseEntity<SpotifyMultiTracks> multiTrackResponse = restTemplate.exchange(queryUrl,
                HttpMethod.GET,
                new HttpEntity<>(createAuthBearerHeader(accessToken)),
                SpotifyMultiTracks.class);

        return multiTrackResponse.getBody().getTracks();
    }
    private HttpHeaders createAuthBearerHeader(String token) {
        String authValue = "Bearer " + token;
        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", authValue);
        return header;
    }


}
