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
            throw new IllegalArgumentException("This movie already exists");
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
        if(playlistName.isBlank() || playlistName.equalsIgnoreCase("new playlist")) {
            throw new IllegalArgumentException("The chosen playlist name is not compatible");
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

        if(currentPlaylist.getSpotifyTrackIds().contains(sendObject.getSpotifyTrackId()) && !sendObject.getSpotifyTrackId().equals("0") ||
                (currentPlaylist.getDeezerTrackIds().contains(sendObject.getDeezerTrackId()) && !sendObject.getDeezerTrackId().equals("0"))) {
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

        if(sendObject.getDeezerTrackId().equals("0")) {
            int spotifyIndex = currentPlaylist.getSpotifyTrackIds().indexOf(sendObject.getSpotifyTrackId());
            currentPlaylist.getSpotifyTrackIds().remove(sendObject.getSpotifyTrackId());
            currentPlaylist.getDeezerTrackIds().remove(spotifyIndex);
        }
        else if(sendObject.getSpotifyTrackId().equals("0")) {
            int deezerIndex = currentPlaylist.getDeezerTrackIds().indexOf(sendObject.getDeezerTrackId());
            currentPlaylist.getSpotifyTrackIds().remove(deezerIndex);
            currentPlaylist.getDeezerTrackIds().remove(sendObject.getDeezerTrackId());
        } else {
            currentPlaylist.getSpotifyTrackIds().remove(sendObject.getSpotifyTrackId());
            currentPlaylist.getDeezerTrackIds().remove(sendObject.getDeezerTrackId());
        }


    }

    public void removeUserPlaylist(String playlistName) {
        if(userPlaylists.containsKey(playlistName)) {
            userPlaylists.remove(playlistName);
        } else {
            throw new IllegalArgumentException("No Playlist found with this name");
        }
    }
}
