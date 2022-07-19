package com.github.NicolaiKopka.users;

import com.github.NicolaiKopka.db_models.spotifyModels.authenticationModels.SpotifyOAuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/spotify/callback")
public class SpotifyLoginController {

    private final RestTemplate restTemplate;
    private final String SPOTIFY_ID;
    private final String SPOTIFY_SECRET;
    private final String spotifyCallbackURL;

    public SpotifyLoginController(RestTemplate restTemplate, @Value("${spotify.api.id}") String SPOTIFY_ID, @Value("${spotify.api.secret}") String SPOTIFY_SECRET, @Value("${spotify.callback.url}") String spotifyCallbackURL) {
        this.restTemplate = restTemplate;
        this.SPOTIFY_ID = SPOTIFY_ID;
        this.SPOTIFY_SECRET = SPOTIFY_SECRET;
        this.spotifyCallbackURL = spotifyCallbackURL;
    }

    @GetMapping
    public void callbackUrl(@RequestParam String code) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "authorization_code");
        map.add("code", code);
        map.add("redirect_uri", spotifyCallbackURL);
        HttpHeaders headers = createGetTokenHeaders();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<SpotifyOAuthResponse> accessTokenResponse = restTemplate.exchange(
                "https://accounts.spotify.com/api/token",
                HttpMethod.POST,
                request,
                SpotifyOAuthResponse.class
        );
        System.out.println(accessTokenResponse.getBody().toString());
    }
    HttpHeaders createGetTokenHeaders(){
        HttpHeaders header = new HttpHeaders();
        header.setBasicAuth(SPOTIFY_ID ,SPOTIFY_SECRET);
        return header;
    }
}


