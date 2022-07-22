package com.github.NicolaiKopka.web_page_services.user_pages.user_favorites;

import com.github.NicolaiKopka.api_services.MovieDBApiConnect;
import com.github.NicolaiKopka.api_services.SpotifyApiConnect;
import com.github.NicolaiKopka.db_models.movieDBModels.Movie;
import com.github.NicolaiKopka.db_models.spotifyModels.spotifyPlaylistModels.AddPlaylistTransferData;
import com.github.NicolaiKopka.db_models.spotifyModels.spotifyPlaylistModels.SpotifyPlaylist;
import com.github.NicolaiKopka.db_models.spotifyModels.spotifyPlaylistModels.SpotifyUserPlaylists;
import com.github.NicolaiKopka.users.MyUser;
import com.github.NicolaiKopka.users.MyUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserFavoritesService {

    private final MyUserRepo myUserRepo;
    private final UserFavoritesRepo userFavoritesRepo;
    private final MovieDBApiConnect movieDBApiConnect;
    private final SpotifyApiConnect spotifyApiConnect;

    public Collection<Movie> getAllFavoriteMoviesFromDbByUser(String username) {

        MyUser user = myUserRepo.findByUsername(username).orElseThrow();

        UserFavoritesSaveObject userFavorites = userFavoritesRepo.findByUserId(user.getId()).orElseThrow();

        return userFavorites.getMovieIds().stream().map(movieDBApiConnect::getMovieById).toList();
    }
    public UserFavoritesSaveObject addMovieToFavorites(Integer movieId, String username) {

        MyUser user = myUserRepo.findByUsername(username).orElseThrow();

        userFavoritesRepo.findByUserId(user.getId()).ifPresentOrElse(
                userFavorites -> {
                    userFavorites.addMovieId(movieId);
                    userFavoritesRepo.save(userFavorites);
                },
                () -> {
                    UserFavoritesSaveObject userFavorites = new UserFavoritesSaveObject();
                    userFavorites.setUserId(user.getId());
                    userFavorites.addMovieId(movieId);
                    userFavoritesRepo.save(userFavorites);
                }
        );

        return userFavoritesRepo.findByUserId(user.getId()).orElseThrow();
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
        return spotifyApiConnect.addSpotifyPlaylist(spotifyToken, user.getSpotifyId(), data);
    }
}
