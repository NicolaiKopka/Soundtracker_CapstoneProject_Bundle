package com.github.NicolaiKopka.users;

import com.github.NicolaiKopka.db_models.deezerModels.DeezerLoginResponseDTO;
import com.github.NicolaiKopka.db_models.deezerModels.DeezerUserData;
import com.github.NicolaiKopka.db_models.spotifyModels.authenticationModels.SpotifyLoginResponseDTO;
import com.github.NicolaiKopka.db_models.spotifyModels.authenticationModels.SpotifyOAuthResponse;
import com.github.NicolaiKopka.db_models.spotifyModels.authenticationModels.SpotifyUserData;
import com.github.NicolaiKopka.security.JWTService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class StreamingProviderLoginController {

    private final RestTemplate restTemplate;
    private final String SPOTIFY_ID;
    private final String SPOTIFY_SECRET;

    private final String DEEZER_ID;
    private final String DEEZER_SECRET;
    private final String spotifyCallbackURL;
    private final StreamingProviderLoginService streamingProviderLoginService;
    private final JWTService jwtService;
    private final AccountService accountService;
    private final AuthenticationManager authenticationManager;

    public StreamingProviderLoginController(RestTemplate restTemplate,
                                            @Value("${spotify.api.id}") String SPOTIFY_ID,
                                            @Value("${spotify.api.secret}") String SPOTIFY_SECRET,
                                            @Value("${spotify.callback.url}") String spotifyCallbackURL,
                                            @Value("${deezer.api.id}") String DEEZER_ID,
                                            @Value("${deezer.api.secret}") String DEEZER_SECRET,
                                            StreamingProviderLoginService streamingProviderLoginService,
                                            JWTService jwtService, AccountService accountService, AuthenticationManager authenticationManager) {
        this.restTemplate = restTemplate;
        this.SPOTIFY_ID = SPOTIFY_ID;
        this.SPOTIFY_SECRET = SPOTIFY_SECRET;
        this.spotifyCallbackURL = spotifyCallbackURL;
        this.jwtService = jwtService;
        this.accountService = accountService;
        this.authenticationManager = authenticationManager;
        this.streamingProviderLoginService = streamingProviderLoginService;
        this.DEEZER_ID = DEEZER_ID;
        this.DEEZER_SECRET = DEEZER_SECRET;
    }

    @GetMapping("/spotify/callback")
    public SpotifyLoginResponseDTO callbackUrl(@RequestParam String code) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "authorization_code");
        map.add("code", code);
        map.add("redirect_uri", spotifyCallbackURL);
        HttpHeaders headers = createTokenHeaders();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<SpotifyOAuthResponse> accessTokenResponse = restTemplate.exchange(
                "https://accounts.spotify.com/api/token",
                HttpMethod.POST,
                request,
                SpotifyOAuthResponse.class
        );
        SpotifyUserData userData = getSpotifyUserData(Objects.requireNonNull(accessTokenResponse.getBody()));

        MyUser user = streamingProviderLoginService.findOrCreateSpotifyUser(userData);

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", user.getRoles());

        //following is after deploy to ensure every user's DB entries are modified according to possible code changes
        accountService.modifyDBOnLogin(user);

        String jwtToken = jwtService.createToken(claims, user.getUsername());

        return new SpotifyLoginResponseDTO(jwtToken, accessTokenResponse.getBody().getAccessToken());
    }

    @GetMapping("/deezer/callback")
    public DeezerLoginResponseDTO deezerCallback(@RequestParam String code) {
        String queryUrl = "https://connect.deezer.com/oauth/access_token.php?" +
                "app_id=" + DEEZER_ID +
                "&secret=" + DEEZER_SECRET +
                "&code=" + code;

        String deezerResponse = restTemplate.getForObject(queryUrl, String.class);
        String accessTokenFull = deezerResponse.split("&")[0];
        String accessToken = deezerResponse.split("=")[1];

        DeezerUserData deezerUserData = getDeezerUserData(accessTokenFull);

        MyUser user = streamingProviderLoginService.findOrCreateDeezerUser(deezerUserData);

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", user.getRoles());

        accountService.modifyDBOnLogin(user);

        String jwtToken = jwtService.createToken(claims, user.getUsername());

        return new DeezerLoginResponseDTO(jwtToken, accessToken);

    }

    public SpotifyUserData getSpotifyUserData(SpotifyOAuthResponse accessTokenResponse) {
        ResponseEntity<SpotifyUserData> userResponse = restTemplate.exchange(
                "https://api.spotify.com/v1/me",
                HttpMethod.GET,
                new HttpEntity<>(createHeaders(accessTokenResponse.getAccessToken())),
                SpotifyUserData.class
        );
        return userResponse.getBody();
    }

    private DeezerUserData getDeezerUserData(String accessToken) {
        return restTemplate.getForObject("https://api.deezer.com/user/me?" + accessToken, DeezerUserData.class);

    }

    HttpHeaders createHeaders(String token) {
        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", "Bearer " + token);
        return header;
    }
    HttpHeaders createTokenHeaders(){
        HttpHeaders header = new HttpHeaders();
        header.setBasicAuth(SPOTIFY_ID ,SPOTIFY_SECRET);
        return header;
    }
}


