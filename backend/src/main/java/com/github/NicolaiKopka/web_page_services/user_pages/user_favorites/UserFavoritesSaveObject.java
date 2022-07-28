package com.github.NicolaiKopka.web_page_services.user_pages.user_favorites;

import com.github.NicolaiKopka.db_models.userPlaylistModels.UserPlaylistSendObject;
import com.github.NicolaiKopka.db_models.userPlaylistModels.UserPlaylist;
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
    private Map<String, UserPlaylist> userPlaylists = new HashMap<>();
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
        if(userPlaylists.containsKey(playlistName)){
            throw new IllegalArgumentException("Playlist already exists");
        }
        UserPlaylist playlist = new UserPlaylist();
        playlist.setPlaylistName(playlistName);
        userPlaylists.put(playlistName, playlist);
    }

    public void addTrackToPlaylist(UserPlaylistSendObject sendObject) {
        UserPlaylist currentPlaylist = userPlaylists.get(sendObject.getPlaylistName());

        if(currentPlaylist == null) {
            createNewUserPlaylist(sendObject.getPlaylistName());
            currentPlaylist = userPlaylists.get(sendObject.getPlaylistName());
        }

        if(currentPlaylist.getSpotifyTrackIds().contains(sendObject.getSpotifyTrackId()) ||
                currentPlaylist.getDeezerTrackIds().contains(sendObject.getDeezerTrackId())) {
            throw new IllegalArgumentException("Track already in playlist");
        }

        currentPlaylist.getSpotifyTrackIds().add(sendObject.getSpotifyTrackId());
        currentPlaylist.getDeezerTrackIds().add(sendObject.getDeezerTrackId());
    }

    public void removeTrackFromPlaylist(UserPlaylistSendObject sendObject) {
        UserPlaylist currentPlaylist = userPlaylists.get(sendObject.getPlaylistName());

        if(currentPlaylist == null) {
            throw new IllegalArgumentException("Playlist not found");
        }

        currentPlaylist.getSpotifyTrackIds().remove(sendObject.getSpotifyTrackId());
        currentPlaylist.getDeezerTrackIds().remove(sendObject.getDeezerTrackId());
    }

    public void removeUserPlaylist(String playlistName) {
        if(userPlaylists.containsKey(playlistName)) {
            userPlaylists.remove(playlistName);
        } else {
            throw new IllegalArgumentException("No Playlist found with this name");
        }
    }
}
