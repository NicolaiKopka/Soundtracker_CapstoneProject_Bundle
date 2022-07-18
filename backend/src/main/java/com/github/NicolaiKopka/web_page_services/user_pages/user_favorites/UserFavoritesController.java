package com.github.NicolaiKopka.web_page_services.user_pages.user_favorites;


import com.github.NicolaiKopka.db_models.movieDBModels.Movie;
import com.github.NicolaiKopka.dto.UserFavoritesDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PutMapping("/{movieId}")
    public ResponseEntity<UserFavoritesDTO> addMovieToFavorites(@PathVariable int movieId, Principal principal) {
        try {
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
//
//    @PutMapping("/{id}")
//    public void changeNotificationStatusForMovie(@PathVariable String id) {
//        userPageService.changeNotificationStatus(id);
//    }

}