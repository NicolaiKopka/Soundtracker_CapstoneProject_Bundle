package com.github.NicolaiKopka.db_models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlbumReferenceDTO {

    private String movieTitle;
    private String albumUrl;

    // Spotify Reference
    private String spotifyReleaseDate;
    private String spotifyAlbumId;
    private List<String> spotifyAlbumTrackUrls;
    // Deezer Reference
    private long deezerAlbumId;

}
