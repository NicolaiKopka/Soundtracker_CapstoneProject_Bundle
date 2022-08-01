package com.github.NicolaiKopka.db_models.spotifyModels.spotifyPlaylistModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SpotifyPlaylist {

    private String description;
    private String name;
    private String href;
    private boolean collaborative;
    @JsonProperty("public")
    private boolean isPublic;
    private String id;

}
