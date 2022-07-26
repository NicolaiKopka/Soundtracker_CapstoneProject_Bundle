package com.github.NicolaiKopka.db_models.spotifyModels;

import lombok.Data;

import java.util.List;

@Data
public class SpotifyAlbumDTO {

    private List<SpotifyTrack> albumTracks;
}
