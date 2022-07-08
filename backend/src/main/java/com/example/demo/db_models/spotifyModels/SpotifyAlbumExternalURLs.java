package com.example.demo.db_models.spotifyModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SpotifyAlbumExternalURLs {

    @JsonProperty("spotify")
    private String albumUrl;

}
