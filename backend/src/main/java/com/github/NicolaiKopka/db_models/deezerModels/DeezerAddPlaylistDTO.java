package com.github.NicolaiKopka.db_models.deezerModels;

import lombok.Data;

@Data
public class DeezerAddPlaylistDTO {

    private String deezerToken;
    private String playlistName;
    private String id;

}
