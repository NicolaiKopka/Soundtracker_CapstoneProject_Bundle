package com.github.NicolaiKopka.web_page_services.user_pages.user_favorites;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document(collection = "userFavorites")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFavoritesSaveObject {

    @Id
    private String id;
    private String userId;
    private List<Integer> movieIds = new ArrayList<>();


    public void addMovieId(Integer movieId) {
        if(movieIds.contains(movieId)) {
            throw new IllegalArgumentException("Movie already in list");
        }
        movieIds.add(movieId);
    }

    public UserFavoritesSaveObject deleteMovieId(Integer movieId) {
        movieIds.removeIf(id -> Objects.equals(id, movieId));
        return this;
    }
}
