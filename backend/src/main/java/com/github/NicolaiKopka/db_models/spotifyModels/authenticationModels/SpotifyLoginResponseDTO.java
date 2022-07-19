package com.github.NicolaiKopka.db_models.spotifyModels.authenticationModels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpotifyLoginResponseDTO {

    private String jwtToken;
    private String spotifyToken;
}
