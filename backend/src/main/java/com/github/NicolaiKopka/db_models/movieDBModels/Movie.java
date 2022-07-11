package com.github.NicolaiKopka.db_models.movieDBModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.bson.codecs.pojo.annotations.BsonId;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Movie {

    @JsonProperty("backdrop_path")
    private String backdropPath;

    @JsonProperty("poster_path")
    private String posterPath;

    private String title;

    @JsonProperty("release_date")
    private String releaseDate;

    private int id;
}
