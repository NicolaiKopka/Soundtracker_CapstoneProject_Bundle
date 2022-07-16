package com.github.NicolaiKopka.web_page_services.user_pages.user_favorites;

import com.github.NicolaiKopka.api_services.MovieDBApiConnect;
import com.github.NicolaiKopka.db_models.movieDBModels.Movie;
import com.github.NicolaiKopka.users.MyUser;
import com.github.NicolaiKopka.users.MyUserRepo;
import org.apache.tomcat.util.security.Escape;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.xml.crypto.dsig.spec.ExcC14NParameterSpec;
import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserFavoritesServiceTest {

    @Test
    void shouldReturnListOfFavoriteMovies() {
        MyUser user = MyUser.builder().username("user").id("1234").build();
        UserFavoritesSaveObject saveObject = UserFavoritesSaveObject.builder().movieIds(List.of(1, 2)).build();
        Movie movie1 = Movie.builder().title("movie1").build();
        Movie movie2 = Movie.builder().title("movie2").build();

        MyUserRepo myUserRepo = Mockito.mock(MyUserRepo.class);
        Mockito.when(myUserRepo.findByUsername("user")).thenReturn(Optional.of(user));

        UserFavoritesRepo userFavoritesRepo = Mockito.mock(UserFavoritesRepo.class);
        Mockito.when(userFavoritesRepo.findByUserId("1234")).thenReturn(Optional.of(saveObject));

        MovieDBApiConnect movieDBApiConnect = Mockito.mock(MovieDBApiConnect.class);
        Mockito.when(movieDBApiConnect.getMovieById(1)).thenReturn(movie1);
        Mockito.when(movieDBApiConnect.getMovieById(2)).thenReturn(movie2);

        UserFavoritesService userFavoritesService = new UserFavoritesService(myUserRepo, userFavoritesRepo, movieDBApiConnect);
        Collection<Movie> actual = userFavoritesService.getAllFavoriteMoviesFromDbByUser("user");

        Assertions.assertThat(actual).contains(movie1, movie2);
    }

    @Test
    void shouldThrowIfUserNotFoundOnGetFavorites() {
        MyUser user = MyUser.builder().username("noUser").id("1234").build();

        MyUserRepo myUserRepo = Mockito.mock(MyUserRepo.class);
        Mockito.when(myUserRepo.findByUsername("noUser")).thenReturn(Optional.empty());

        UserFavoritesRepo userFavoritesRepo = Mockito.mock(UserFavoritesRepo.class);

        MovieDBApiConnect movieDBApiConnect = Mockito.mock(MovieDBApiConnect.class);

        UserFavoritesService userFavoritesService = new UserFavoritesService(myUserRepo, userFavoritesRepo, movieDBApiConnect);

        try{
            userFavoritesService.getAllFavoriteMoviesFromDbByUser(user.getUsername());
            fail();
        } catch (NoSuchElementException e) {}
    }

    @Test
    void shouldThrowIfFavoritesNotFoundOnGetFavorites() {
        MyUser user = MyUser.builder().username("user").id("1234").build();

        MyUserRepo myUserRepo = Mockito.mock(MyUserRepo.class);
        Mockito.when(myUserRepo.findByUsername("user")).thenReturn(Optional.of(user));

        UserFavoritesRepo userFavoritesRepo = Mockito.mock(UserFavoritesRepo.class);
        Mockito.when(userFavoritesRepo.findByUserId("1234")).thenReturn(Optional.empty());

        MovieDBApiConnect movieDBApiConnect = Mockito.mock(MovieDBApiConnect.class);

        UserFavoritesService userFavoritesService = new UserFavoritesService(myUserRepo, userFavoritesRepo, movieDBApiConnect);

        try{
            userFavoritesService.getAllFavoriteMoviesFromDbByUser(user.getUsername());
            fail();
        } catch (NoSuchElementException e) {}
    }

    @Test
    void shouldAddMovieIdToFavoriteObject() {
        MyUser user = MyUser.builder().username("user").id("1234").build();

        UserFavoritesSaveObject existingFavorites = new UserFavoritesSaveObject();
        existingFavorites.setMovieIds(List.of(1));
        existingFavorites.setUserId("1234");

        MyUserRepo myUserRepo = Mockito.mock(MyUserRepo.class);
        Mockito.when(myUserRepo.findByUsername("user")).thenReturn(Optional.of(user));

        UserFavoritesRepo userFavoritesRepo = Mockito.mock(UserFavoritesRepo.class);
        Mockito.when(userFavoritesRepo.findByUserId("1234")).thenReturn(Optional.of(existingFavorites));

        MovieDBApiConnect movieDBApiConnect = Mockito.mock(MovieDBApiConnect.class);

        UserFavoritesSaveObject expectedFavorites = UserFavoritesSaveObject.builder()
                .userId("1234")
                .movieIds(List.of(1, 2))
                .build();

        UserFavoritesService userFavoritesService = new UserFavoritesService(myUserRepo, userFavoritesRepo, movieDBApiConnect);
        userFavoritesService.addMovieToFavorites(2, "user");

        Mockito.verify(userFavoritesRepo).save(expectedFavorites);
    }

    @Test
    void shouldCreateFavoriteObjectAndAddMovieId() {
        MyUser user = MyUser.builder().username("user").id("1234").build();

        MyUserRepo myUserRepo = Mockito.mock(MyUserRepo.class);
        Mockito.when(myUserRepo.findByUsername("user")).thenReturn(Optional.of(user));

        UserFavoritesRepo userFavoritesRepo = Mockito.mock(UserFavoritesRepo.class);
        Mockito.when(userFavoritesRepo.findByUserId("1234")).thenReturn(Optional.empty());

        MovieDBApiConnect movieDBApiConnect = Mockito.mock(MovieDBApiConnect.class);

        UserFavoritesSaveObject expectedFavorites = UserFavoritesSaveObject.builder()
                .userId("1234")
                .movieIds(List.of(1))
                .build();

        UserFavoritesService userFavoritesService = new UserFavoritesService(myUserRepo, userFavoritesRepo, movieDBApiConnect);
        userFavoritesService.addMovieToFavorites(1, "user");

        Mockito.verify(userFavoritesRepo).save(expectedFavorites);
    }

    @Test
    void shouldThrowIfUserNotFoundOnAddMovieID() {
        MyUser user = MyUser.builder().username("noUser").id("1234").build();

        MyUserRepo myUserRepo = Mockito.mock(MyUserRepo.class);
        Mockito.when(myUserRepo.findByUsername("noUser")).thenReturn(Optional.empty());

        UserFavoritesRepo userFavoritesRepo = Mockito.mock(UserFavoritesRepo.class);

        MovieDBApiConnect movieDBApiConnect = Mockito.mock(MovieDBApiConnect.class);

        UserFavoritesService userFavoritesService = new UserFavoritesService(myUserRepo, userFavoritesRepo, movieDBApiConnect);

        try{
            userFavoritesService.addMovieToFavorites(1, user.getUsername());
            fail();
        } catch (NoSuchElementException e) {}
    }

    @Test
    void shouldDeleteMovieIdFromFavoritesList() {
        MyUser user = MyUser.builder().username("testUser").id("1234").build();
        UserFavoritesSaveObject favoritesObject = UserFavoritesSaveObject.builder().movieIds(List.of(12, 13, 14)).build();
        UserFavoritesSaveObject expectedFavorites = UserFavoritesSaveObject.builder().movieIds(List.of(12, 14)).build();

        MyUserRepo myUserRepo = Mockito.mock(MyUserRepo.class);
        Mockito.when(myUserRepo.findByUsername("testUser")).thenReturn(Optional.of(user));

        UserFavoritesRepo userFavoritesRepo = Mockito.mock(UserFavoritesRepo.class);
        Mockito.when(userFavoritesRepo.findByUserId("1234")).thenReturn(Optional.of(favoritesObject));

        MovieDBApiConnect movieDBApiConnect = Mockito.mock(MovieDBApiConnect.class);

        UserFavoritesService userFavoritesService = new UserFavoritesService(myUserRepo, userFavoritesRepo, movieDBApiConnect);
        userFavoritesService.deleteMovieFromFavorites(13, "testUser");

        Mockito.verify(userFavoritesRepo).save(expectedFavorites);
    }




}