package com.github.NicolaiKopka.db_models.spotifyModels;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SpotifyTrack {

    private String name;
    private String id;
    @JsonProperty("external_urls")
    private SpotifyAlbumExternalURLs externalURLs;

}
