package com.github.NicolaiKopka.db_models.spotifyModels.authenticationModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SpotifyOAuthResponse {

    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("token_type")
    private String tokenType;
    private String scope;
    @JsonProperty("expires_in")
    private int expirationTime;
    @JsonProperty("refresh_token")
    private String refreshToken;

}
