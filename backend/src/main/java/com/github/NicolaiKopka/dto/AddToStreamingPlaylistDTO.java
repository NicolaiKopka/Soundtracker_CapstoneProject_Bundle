package com.github.NicolaiKopka.dto;

import lombok.Data;

@Data
public class AddToStreamingPlaylistDTO {

    private String spotifyToken;
    private String spotifyPlaylistId;
    private String userPlaylistName;

}
