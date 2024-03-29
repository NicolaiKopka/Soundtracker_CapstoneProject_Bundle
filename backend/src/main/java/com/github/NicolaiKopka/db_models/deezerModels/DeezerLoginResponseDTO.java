package com.github.NicolaiKopka.db_models.deezerModels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeezerLoginResponseDTO {

    private String jwtToken;
    private String deezerToken;

}
