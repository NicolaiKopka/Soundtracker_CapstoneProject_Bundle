package com.github.NicolaiKopka.db_models.deezerModels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeezerAlbum {

    private long id;
    private String title;
    private String link;

}
