package com.github.NicolaiKopka.db_models;

import com.github.NicolaiKopka.db_models.deezerModels.DeezerTrack;
import com.github.NicolaiKopka.db_models.spotifyModels.SpotifyTrackDTO;
import lombok.Data;

import java.util.List;

@Data
public class StreamingTracks {

    private List<SpotifyTrackDTO> spotifyTracks;
    private List<DeezerTrack> deezerTracks;

}
