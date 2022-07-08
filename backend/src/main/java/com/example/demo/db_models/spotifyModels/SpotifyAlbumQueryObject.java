package com.example.demo.db_models.spotifyModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class SpotifyAlbumQueryObject {

    @JsonProperty("items")
    private List<SpotifyAlbum> albumQueryList;

}
