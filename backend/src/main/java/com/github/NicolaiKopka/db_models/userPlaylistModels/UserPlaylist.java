package com.github.NicolaiKopka.db_models.userPlaylistModels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPlaylist {

    private String playlistName;
    private List<String> spotifyTrackIds = new ArrayList<>();
    private List<String> deezerTrackIds = new ArrayList<>();
}
