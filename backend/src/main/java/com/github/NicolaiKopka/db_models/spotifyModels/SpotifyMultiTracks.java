package com.github.NicolaiKopka.db_models.spotifyModels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpotifyMultiTracks {

    private List<SpotifyTrack> items;

}
