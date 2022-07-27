package com.github.NicolaiKopka.web_page_services.user_pages.user_favorites;


import com.github.NicolaiKopka.db_models.movieDBModels.Movie;
import com.github.NicolaiKopka.db_models.spotifyModels.spotifyPlaylistModels.AddPlaylistTransferData;
import com.github.NicolaiKopka.db_models.spotifyModels.spotifyPlaylistModels.SpotifyPlaylist;
import com.github.NicolaiKopka.db_models.spotifyModels.spotifyPlaylistModels.SpotifyUserPlaylists;
import com.github.NicolaiKopka.db_models.userPlaylistModels.UserPlaylistSendObject;
import com.github.NicolaiKopka.dto.UserFavoritesDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("api/soundtracker/user-favorites")
@RequiredArgsConstructor
public class UserFavoritesController {

    private final UserFavoritesService userFavoritesService;

    @GetMapping
    public ResponseEntity<Collection<Movie>> getAllFavoriteMoviesFromDbByUser(Principal principal) {
        try {
            return ResponseEntity.ok(userFavoritesService.getAllFavoriteMoviesFromDbByUser(principal.getName()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/spotify-playlists/{spotifyToken}")
    public ResponseEntity<SpotifyUserPlaylists> getAllSpotifyPlaylistsFromUser(Principal principal, @PathVariable String spotifyToken) {
        try {
            return ResponseEntity.ok(userFavoritesService.getAllSpotifyPlaylistsFromUser(principal.getName(), spotifyToken));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/spotify-playlists/add/{spotifyToken}")
    public ResponseEntity<SpotifyPlaylist> addSpotifyPlaylist(Principal principal, @PathVariable String spotifyToken, @RequestBody AddPlaylistTransferData data) {
        try {
            return ResponseEntity.ok(userFavoritesService.addSpotifyPlaylist(spotifyToken, principal.getName(), data));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/create-user-playlist/{playlistName}")
    public ResponseEntity<UserFavoritesDTO> createNewUserPlaylist(Principal principal, @PathVariable String playlistName) {
        try {
            UserFavoritesSaveObject userFavorites = userFavoritesService.createNewUserPlaylist(principal.getName(), playlistName);
            UserFavoritesDTO userFavoritesDTO = UserFavoritesDTO.builder()
                    .movieIds(userFavorites.getMovieIds())
                    .userPlaylists(userFavorites.getUserPlaylists()).build();
            return ResponseEntity.ok(userFavoritesDTO);
        } catch (NoSuchElementException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/user-playlist/add-track")
    public ResponseEntity<UserFavoritesDTO> addTrackToUserPlaylist(Principal principal, @RequestBody UserPlaylistSendObject sendObject) {
        try {
            UserFavoritesSaveObject userFavorites = userFavoritesService.addTrackToUserPlaylist(principal.getName(), sendObject);
            UserFavoritesDTO userFavoritesDTO = UserFavoritesDTO.builder()
                    .movieIds(userFavorites.getMovieIds())
                    .userPlaylists(userFavorites.getUserPlaylists()).build();
            return ResponseEntity.ok(userFavoritesDTO);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{movieId}")
    public ResponseEntity<UserFavoritesDTO> addMovieToFavorites(@PathVariable int movieId, Principal principal) {
        try {
//            userFavoritesService.addMovieToFavorites(movieId, principal.getName());
//            return ResponseEntity.status(HttpStatus.CREATED).build();
            UserFavoritesSaveObject userFavorites = userFavoritesService.addMovieToFavorites(movieId, principal.getName());
            UserFavoritesDTO userFavoritesDTO = UserFavoritesDTO.builder().movieIds(userFavorites.getMovieIds()).build();
            return ResponseEntity.ok(userFavoritesDTO);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

    @DeleteMapping("/{movieId}")
    public ResponseEntity<UserFavoritesDTO> deleteMovieFromFavorites(@PathVariable int movieId, Principal principal) {
        try {
            UserFavoritesSaveObject favoritesSaveObject = userFavoritesService.deleteMovieFromFavorites(movieId, principal.getName()).orElseThrow();
            return ResponseEntity.ok(UserFavoritesDTO.builder().movieIds(favoritesSaveObject.getMovieIds()).build());
        } catch (NoSuchElementException | NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
