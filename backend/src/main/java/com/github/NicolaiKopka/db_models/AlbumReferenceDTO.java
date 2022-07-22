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

    // Spotify Reference
    private String spotifyReleaseDate;
    private String spotifyAlbumUrl;

    // Deezer Reference
    private long deezerAlbumId;
    private String deezerAlbumUrl;

}
