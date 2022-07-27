package com.github.NicolaiKopka.db_models.userPlaylistModels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPlaylistSendObject {

    private String playlistName;
    private String spotifyTrackId;
    private String deezerTrackId;

}
