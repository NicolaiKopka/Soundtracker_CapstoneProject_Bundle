package com.github.NicolaiKopka.db_models.deezerModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class DeezerUserData {

    @JsonProperty("id")
    private long userId;
    private String name;
    private String email;
}
