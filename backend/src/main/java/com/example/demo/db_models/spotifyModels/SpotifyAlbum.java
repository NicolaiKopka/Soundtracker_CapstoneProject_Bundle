package com.example.demo.db_models.spotifyModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SpotifyAlbum {

    @JsonProperty("external_urls")
    private SpotifyAlbumExternalURLs externalURLs;
    private String name;
    @JsonProperty("release_date")
    private String releaseDate;

}
