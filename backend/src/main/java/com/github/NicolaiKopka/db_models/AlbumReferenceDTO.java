package com.github.NicolaiKopka.db_models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlbumReferenceDTO {

    private String movieTitle;
    private String albumUrl;

    // Spotify Reference
    private String spotifyReleaseDate;
    // Deezer Reference
    private long deezerAlbumId;

}
