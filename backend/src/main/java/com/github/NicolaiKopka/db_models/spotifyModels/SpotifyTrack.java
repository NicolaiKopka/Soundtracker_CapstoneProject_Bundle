package com.github.NicolaiKopka.db_models.spotifyModels;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpotifyTrack {

    private String name;
    private String id;
    @JsonProperty("external_urls")
    private SpotifyAlbumExternalURLs externalURLs;

}
