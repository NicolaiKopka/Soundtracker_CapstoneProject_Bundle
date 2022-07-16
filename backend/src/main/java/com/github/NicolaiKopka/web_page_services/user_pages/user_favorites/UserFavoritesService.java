package com.github.NicolaiKopka.web_page_services.user_pages.user_favorites;

import com.github.NicolaiKopka.api_services.MovieDBApiConnect;
import com.github.NicolaiKopka.db_models.movieDBModels.Movie;
import com.github.NicolaiKopka.users.MyUser;
import com.github.NicolaiKopka.users.MyUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserFavoritesService {

    private final MyUserRepo myUserRepo;
    private final UserFavoritesRepo userFavoritesRepo;
    private final MovieDBApiConnect movieDBApiConnect;

    public Collection<Movie> getAllFavoriteMoviesFromDbByUser(String username) {

        MyUser user = myUserRepo.findByUsername(username).orElseThrow();

        UserFavoritesSaveObject userFavorites = userFavoritesRepo.findByUserId(user.getId()).orElseThrow();

        return userFavorites.getMovieIds().stream().map(movieDBApiConnect::getMovieById).toList();
    }

    // TODO Returns immutable collections. Check why and fix
    public UserFavoritesSaveObject addMovieToFavorites(Integer movieId, String username) {

        MyUser user = myUserRepo.findByUsername(username).orElseThrow();

        try{
            UserFavoritesSaveObject userFavorites = userFavoritesRepo.findByUserId(user.getId()).orElseThrow();
            List<Integer> movieIds = new ArrayList<>(List.copyOf(userFavorites.getMovieIds()));
            if(movieIds.contains(movieId)) {
                throw new IllegalArgumentException("Movie already in list");
            }
            movieIds.add(movieId);
            userFavorites.setMovieIds(movieIds);
            return userFavoritesRepo.save(userFavorites);
        } catch (NoSuchElementException e) {
            UserFavoritesSaveObject userFavorites = new UserFavoritesSaveObject();
            userFavorites.setUserId(user.getId());
            userFavorites.setMovieIds(List.of(movieId));
            return userFavoritesRepo.save(userFavorites);
        }
    }

    public UserFavoritesSaveObject deleteMovieFromFavorites(int movieId, String username) {

        MyUser user = myUserRepo.findByUsername(username).orElseThrow();
        UserFavoritesSaveObject favoritesObject = userFavoritesRepo.findByUserId(user.getId()).orElseThrow();
        List<Integer> idList = new ArrayList<>(List.copyOf(favoritesObject.getMovieIds()));
        idList.remove(Integer.valueOf(movieId));
        favoritesObject.setMovieIds(idList);
        return userFavoritesRepo.save(favoritesObject);
    }
}
