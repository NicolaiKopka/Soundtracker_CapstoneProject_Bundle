package com.github.NicolaiKopka.api_services;


import com.github.NicolaiKopka.db_models.spotifyModels.SpotifyFirstQueryObject;
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

import java.lang.reflect.Array;

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

    private String getAccessToken() throws OAuthSystemException, OAuthProblemException {
//        String tokenUrl = "https://accounts.spotify.com/api/token";
//        String idAndSecretNotEncoded = SPOTIFY_ID + ":" + SPOTIFY_SECRET;
//        String idAndSecretBase64Encoded = Base64.getEncoder().encodeToString(idAndSecretNotEncoded.getBytes(StandardCharsets.UTF_8));
//
//        String authValue = "Basic " + idAndSecretBase64Encoded;
//
//        HttpHeaders header = new HttpHeaders();
//        header.setBasicAuth(SPOTIFY_ID, SPOTIFY_SECRET);
//        header.set("Authorization", authValue);
//        header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        Map<String, String> body = new HashMap<>();
//        body.put("grant_type", "client_credentials");
//
//        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, header);
//
//        ResponseEntity<SpotifyClientCredentials> tokenResponse = restTemplate.exchange(tokenUrl,
//                HttpMethod.POST,
//                request,
//                SpotifyClientCredentials.class);
//
//        System.out.println(tokenResponse.getBody().getAccessToken());

        OAuthClientRequest clientReqAccessToken = OAuthClientRequest
                .tokenLocation("https://accounts.spotify.com/api/token")
                .setGrantType(GrantType.CLIENT_CREDENTIALS).setClientId(SPOTIFY_ID).setClientSecret(SPOTIFY_SECRET)
                .buildBodyMessage();

        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
        OAuthAccessTokenResponse oAuthResponse = oAuthClient.accessToken(clientReqAccessToken);

        return oAuthResponse.getAccessToken();

    }


    public Array getSpotifyStatusForMovie() throws OAuthProblemException, OAuthSystemException {
        String accessToken = getAccessToken();
        //System.out.println(accessToken);

        String queryUrl = "https://api.spotify.com/v1/search?q=Doctor Strange in the Multiverse of Madness&type=album";

        ResponseEntity<SpotifyFirstQueryObject> queryResponse = restTemplate.exchange(
                queryUrl,
                HttpMethod.GET,
                new HttpEntity<>(createAuthBearerHeader(accessToken)),
                SpotifyFirstQueryObject.class);

        System.out.println(queryResponse.getBody().getAlbums().getAlbumQueryList().stream().toList());

//        System.out.println(queryResponse.getBody().getAlbumQueryList().get(0).getName());


        Object[] spotifyStatus = new Object[2];
        return null;
    }

    private HttpHeaders createAuthBearerHeader(String token) {
        String authValue = "Bearer " + token;
        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", authValue);
        return header;
    }
}
