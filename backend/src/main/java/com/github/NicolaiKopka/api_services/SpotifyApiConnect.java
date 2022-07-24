package com.github.NicolaiKopka.api_services;
import com.github.NicolaiKopka.db_models.AlbumReferenceDTO;
import com.github.NicolaiKopka.db_models.spotifyModels.SpotifyAlbum;
import com.github.NicolaiKopka.db_models.spotifyModels.SpotifyAlbumQueryObject;
import com.github.NicolaiKopka.db_models.spotifyModels.SpotifyFirstQueryObject;
import com.github.NicolaiKopka.db_models.spotifyModels.spotifyPlaylistModels.AddPlaylistTransferData;
import com.github.NicolaiKopka.db_models.spotifyModels.spotifyPlaylistModels.SpotifyPlaylist;
import com.github.NicolaiKopka.db_models.spotifyModels.spotifyPlaylistModels.SpotifyUserPlaylists;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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

    public String getAccessToken() throws OAuthSystemException, OAuthProblemException {

        OAuthClientRequest clientReqAccessToken = OAuthClientRequest
                .tokenLocation("https://accounts.spotify.com/api/token")
                .setGrantType(GrantType.CLIENT_CREDENTIALS).setClientId(SPOTIFY_ID).setClientSecret(SPOTIFY_SECRET)
                .buildBodyMessage();

        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
        OAuthAccessTokenResponse oAuthResponse = oAuthClient.accessToken(clientReqAccessToken);

        return oAuthResponse.getAccessToken();
    }

    // TODO find a way to check if correct movie is returned, including netflix
    public List<AlbumReferenceDTO> getSpotifyListOfMovieAlbums(String movieName) throws OAuthProblemException, OAuthSystemException {
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
    private HttpHeaders createAuthBearerHeader(String token) {
        String authValue = "Bearer " + token;
        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", authValue);
        return header;
    }
}
