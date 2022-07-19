package com.github.NicolaiKopka.db_models.spotifyModels.authenticationModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SpotifyUserData {
    @JsonProperty("display_name")
    private String username;
    private String email;
}
