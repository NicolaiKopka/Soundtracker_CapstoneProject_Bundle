package com.github.NicolaiKopka.db_models.spotifyModels.spotifyPlaylistModels;

import lombok.Data;

import java.util.List;

@Data
public class SpotifyUserPlaylists {

    private List<SpotifyPlaylist> items;

}
