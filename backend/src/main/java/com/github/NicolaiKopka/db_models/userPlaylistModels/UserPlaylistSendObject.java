package com.github.NicolaiKopka.db_models.userPlaylistModels;

import lombok.Data;

@Data
public class UserPlaylistSendObject {

    private String playlistName;
    private String spotifyTrackId;
    private String deezerTrackId;

}
