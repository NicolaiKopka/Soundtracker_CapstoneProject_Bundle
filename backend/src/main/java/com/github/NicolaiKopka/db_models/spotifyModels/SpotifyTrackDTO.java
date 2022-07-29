package com.github.NicolaiKopka.db_models.spotifyModels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpotifyTrackDTO {

    private String name;
    private String url;
    private String id;

}
