package com.github.NicolaiKopka.db_models.spotifyModels.spotifyPlaylistModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AddPlaylistTransferData {

    private String name;
    @JsonProperty("public")
    private boolean isPublic;
    private boolean collaborative;
    private String description;
}
