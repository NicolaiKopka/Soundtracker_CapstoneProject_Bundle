package com.github.NicolaiKopka.web_page_services.user_pages.user_favorites;

import com.github.NicolaiKopka.api_services.DeezerApiConnect;
import com.github.NicolaiKopka.api_services.MovieDBApiConnect;
import com.github.NicolaiKopka.api_services.SpotifyApiConnect;
import com.github.NicolaiKopka.db_models.deezerModels.DeezerAddPlaylistDTO;
import com.github.NicolaiKopka.db_models.movieDBModels.Movie;
import com.github.NicolaiKopka.db_models.spotifyModels.SpotifyTrack;
import com.github.NicolaiKopka.db_models.spotifyModels.spotifyPlaylistModels.AddPlaylistTransferData;
import com.github.NicolaiKopka.db_models.spotifyModels.spotifyPlaylistModels.SpotifyPlaylist;
import com.github.NicolaiKopka.db_models.spotifyModels.spotifyPlaylistModels.SpotifyUserPlaylists;
import com.github.NicolaiKopka.db_models.userPlaylistModels.UserPlaylistSendObject;
import com.github.NicolaiKopka.dto.AddToStreamingPlaylistDTO;
import com.github.NicolaiKopka.users.MyUser;
import com.github.NicolaiKopka.users.MyUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserFavoritesService {

    private final MyUserRepo myUserRepo;
    private final UserFavoritesRepo userFavoritesRepo;
    private final MovieDBApiConnect movieDBApiConnect;
    private final SpotifyApiConnect spotifyApiConnect;

    private final DeezerApiConnect deezerApiConnect;

    public Collection<Movie> getAllFavoriteMoviesFromDbByUser(String username) {

        MyUser user = myUserRepo.findByUsername(username).orElseThrow();

        UserFavoritesSaveObject userFavorites = userFavoritesRepo.findByUserId(user.getId()).orElseThrow();

        return userFavorites.getMovieIds().stream().map(movieDBApiConnect::getMovieById).toList();
    }
    public UserFavoritesSaveObject addMovieToFavorites(Integer movieId, String username) {

        MyUser user = myUserRepo.findByUsername(username).orElseThrow();

        UserFavoritesSaveObject favoritesSaveObject = userFavoritesRepo.findByUserId(user.getId()).orElseThrow();

        favoritesSaveObject.addMovieId(movieId);
        return userFavoritesRepo.save(favoritesSaveObject);

    }
    public Optional<UserFavoritesSaveObject> deleteMovieFromFavorites(int movieId, String username) {
        return myUserRepo.findByUsername(username)
                .flatMap(user -> userFavoritesRepo.findByUserId(user.getId()))
                .map(favoritesObject -> favoritesObject.deleteMovieId(movieId))
                .map(userFavoritesRepo::save);
    }
    public SpotifyUserPlaylists getAllSpotifyPlaylistsFromUser(String username, String spotifyToken) {

        MyUser user = myUserRepo.findByUsername(username).orElseThrow();
        return spotifyApiConnect.getAllUserPlaylists(spotifyToken, user.getSpotifyId());
    }
    public SpotifyPlaylist addSpotifyPlaylist(String spotifyToken, String username, AddPlaylistTransferData data) {

        MyUser user = myUserRepo.findByUsername(username).orElseThrow();
        SpotifyUserPlaylists allSpotifyPlaylistsFromUser = getAllSpotifyPlaylistsFromUser(username, spotifyToken);
        if(allSpotifyPlaylistsFromUser.getItems().stream()
                .anyMatch(playlist -> playlist.getName().equals(data.getName()))) {
            throw new IllegalArgumentException("Playlist already exists");
        }

        return spotifyApiConnect.addSpotifyPlaylist(spotifyToken, user.getSpotifyId(), data);
    }

    public DeezerAddPlaylistDTO addDeezerPlaylist(DeezerAddPlaylistDTO playlistDTO) {
        return deezerApiConnect.createNewAlbum(playlistDTO);
    }
    public UserFavoritesSaveObject createNewUserPlaylist(String username, String playlistName) {
        MyUser user = myUserRepo.findByUsername(username).orElseThrow();

        UserFavoritesSaveObject favoritesSaveObject = userFavoritesRepo.findByUserId(user.getId()).orElseThrow();

        favoritesSaveObject.createNewUserPlaylist(playlistName);
        return userFavoritesRepo.save(favoritesSaveObject);
    }
    public UserFavoritesSaveObject addTrackToUserPlaylist(String username, UserPlaylistSendObject sendObject) {
        MyUser user = myUserRepo.findByUsername(username).orElseThrow();

        UserFavoritesSaveObject favoritesObject = userFavoritesRepo.findByUserId(user.getId()).orElseThrow();

        favoritesObject.addTrackToPlaylist(sendObject);
        return userFavoritesRepo.save(favoritesObject);
    }
    public UserFavoritesSaveObject removeTrackFromPlaylist(String username, UserPlaylistSendObject sendObject) {
        MyUser user = myUserRepo.findByUsername(username).orElseThrow();

        UserFavoritesSaveObject favoritesObject = userFavoritesRepo.findByUserId(user.getId()).orElseThrow();

        favoritesObject.removeTrackFromPlaylist(sendObject);
        return userFavoritesRepo.save(favoritesObject);
    }
    public UserFavoritesSaveObject removeUserPlaylist(String username, String playlistName) {
        MyUser user = myUserRepo.findByUsername(username).orElseThrow();

        UserFavoritesSaveObject favoritesObject = userFavoritesRepo.findByUserId(user.getId()).orElseThrow();

        favoritesObject.removeUserPlaylist(playlistName);
        return userFavoritesRepo.save(favoritesObject);
    }
    public UserFavoritesSaveObject getAllUserPlaylists(String username) {
        MyUser user = myUserRepo.findByUsername(username).orElseThrow();

        return userFavoritesRepo.findByUserId(user.getId()).orElseThrow();
    }
    public Collection<SpotifyTrack> getTracksOfUserPlaylist(String username, String playlistName) {
        MyUser user = myUserRepo.findByUsername(username).orElseThrow();

        UserFavoritesSaveObject saveObject = userFavoritesRepo.findByUserId(user.getId()).orElseThrow();
        List<String> spotifyTrackIds = saveObject.getUserPlaylists().get(playlistName).getSpotifyTrackIds();

        return spotifyApiConnect.getMultipleSpotifyTracksById(spotifyTrackIds);
    }
    public void addTracksToSpotifyPlaylist(String username, AddToStreamingPlaylistDTO transferData) {
        MyUser user = myUserRepo.findByUsername(username).orElseThrow();

        UserFavoritesSaveObject saveObject = userFavoritesRepo.findByUserId(user.getId()).orElseThrow();
        List<String> spotifyTrackIds = saveObject.getUserPlaylists().get(transferData.getUserPlaylistName()).getSpotifyTrackIds();

        spotifyApiConnect.addTracksInUserPlaylistToNewSpotifyPlaylist(spotifyTrackIds, transferData.getSpotifyPlaylistId(), transferData.getSpotifyToken());
    }

    public void addTracksToDeezerPlaylist(String username, DeezerAddPlaylistDTO playlistDTO) {
        MyUser user = myUserRepo.findByUsername(username).orElseThrow();

        UserFavoritesSaveObject saveObject = userFavoritesRepo.findByUserId(user.getId()).orElseThrow();
        List<String> deezerTrackIds = saveObject.getUserPlaylists().get(playlistDTO.getPlaylistName()).getDeezerTrackIds();

        deezerApiConnect.addTracksInUserPlaylistToNewDeezerPlaylist(deezerTrackIds, playlistDTO.getId(), playlistDTO.getDeezerToken());
    }

}
