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
public class SpotifyAlbum {

    @JsonProperty("external_urls")
    private SpotifyAlbumExternalURLs externalURLs;
    private String name;
    @JsonProperty("release_date")
    private String releaseDate;
    private String id;
    private SpotifyMultiTracks tracks;

}
