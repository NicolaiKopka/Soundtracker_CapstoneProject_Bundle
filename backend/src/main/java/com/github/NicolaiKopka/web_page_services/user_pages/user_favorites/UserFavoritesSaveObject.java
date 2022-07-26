package com.github.NicolaiKopka.web_page_services.user_pages.user_favorites;

import com.github.NicolaiKopka.db_models.userPlaylistModels.UserPlaylistSendObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

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

    private Map<String, Map<String, List<String>>> userPlaylists = new HashMap<>();
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
    public void createNewUserPlaylist(String playlistName) {
        userPlaylists.putIfAbsent(playlistName, new HashMap<>());
    }

    public void addTrackToPlaylist(UserPlaylistSendObject sendObject) {
        Map<String, List<String>> currentPlaylist = userPlaylists.get(sendObject.getPlaylistName());
        currentPlaylist.putIfAbsent()
    }
}
