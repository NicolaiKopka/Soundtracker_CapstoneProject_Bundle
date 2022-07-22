package com.github.NicolaiKopka.db_models.deezerModels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeezerSearchData {

    List<DeezerSearchObject> data;

}
