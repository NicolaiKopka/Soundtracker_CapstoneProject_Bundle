package com.github.NicolaiKopka.users;

import com.github.NicolaiKopka.api_services.MovieDBApiConnect;
import com.github.NicolaiKopka.db_models.movieDBModels.Movie;
import com.github.NicolaiKopka.dto.LoginResponseDTO;
import com.github.NicolaiKopka.dto.RegisterUserDTO;
import com.github.NicolaiKopka.dto.UserFavoritesDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerIT {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    MyUserRepo userRepo;

    @MockBean
    MovieDBApiConnect movieDBApiConnect;

    @Test
    void userRegistersAndLogsInWithWrongAndCorrectCredentials() {
        // register User
        RegisterData newUser = RegisterData.builder()
                .username("user1")
                .password("password")
                .checkPassword("password")
                .build();

        ResponseEntity<RegisterUserDTO> registerUserResponse = restTemplate.postForEntity("/api/soundtracker/accounts/register",
                newUser,
                RegisterUserDTO.class);
        Assertions.assertThat(registerUserResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(registerUserResponse.getBody().getUsername()).isEqualTo("user1");

        // login with wrong credentials
        LoginData wrongUser1Login = LoginData.builder().username("user1").password("wrongPassword").build();
        ResponseEntity<LoginResponseDTO> wrongLoginUserResponse = restTemplate.postForEntity("/api/soundtracker/accounts/login",
                wrongUser1Login,
                LoginResponseDTO.class);
        Assertions.assertThat(wrongLoginUserResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        // login with correct credentials
        LoginData user1Login = LoginData.builder().username("user1").password("password").build();
        ResponseEntity<LoginResponseDTO> loginUserResponse = restTemplate.postForEntity("/api/soundtracker/accounts/login",
                user1Login,
                LoginResponseDTO.class);
        Assertions.assertThat(loginUserResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(loginUserResponse.getBody().getToken()).isNotBlank();
    }

    @Test
    void usersAddFavoriteMoviesAndWillOnlySeeRespectiveMoviesOnFavoritesPage() {
        // register User 1
        RegisterData newUser1 = RegisterData.builder()
                .username("user1")
                .password("password")
                .checkPassword("password")
                .build();

        restTemplate.postForEntity("/api/soundtracker/accounts/register",
                newUser1,
                RegisterUserDTO.class);

        // login user 1
        LoginData user1Login = LoginData.builder().username("user1").password("password").build();
        ResponseEntity<LoginResponseDTO> loginUser1Response = restTemplate.postForEntity("/api/soundtracker/accounts/login",
                user1Login,
                LoginResponseDTO.class);

        // register User 2
        RegisterData newUser2 = RegisterData.builder()
                .username("user2")
                .password("password")
                .checkPassword("password")
                .build();

        restTemplate.postForEntity("/api/soundtracker/accounts/register",
                newUser2,
                RegisterUserDTO.class);

        // login user 2
        LoginData user2Login = LoginData.builder().username("user2").password("password").build();
        ResponseEntity<LoginResponseDTO> loginUser2Response = restTemplate.postForEntity("/api/soundtracker/accounts/login",
                user2Login,
                LoginResponseDTO.class);

        // fetch user token
        String user1Token = loginUser1Response.getBody().getToken();
        String user2Token = loginUser2Response.getBody().getToken();

        // users add favorite movies
        ResponseEntity<UserFavoritesDTO> user1addId1Response = restTemplate.exchange("/api/soundtracker/user-favorites/1",
                HttpMethod.PUT,
                new HttpEntity<>(createHeader(user1Token)),
                UserFavoritesDTO.class);
        Assertions.assertThat(user1addId1Response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(user1addId1Response.getBody().getMovieIds()).contains(1);

        ResponseEntity<UserFavoritesDTO> user1addId2Response = restTemplate.exchange("/api/soundtracker/user-favorites/2",
                HttpMethod.PUT,
                new HttpEntity<>(createHeader(user1Token)),
                UserFavoritesDTO.class);
        Assertions.assertThat(user1addId2Response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(user1addId2Response.getBody().getMovieIds()).contains(1, 2);

        ResponseEntity<UserFavoritesDTO> user2addId1Response = restTemplate.exchange("/api/soundtracker/user-favorites/123",
                HttpMethod.PUT,
                new HttpEntity<>(createHeader(user2Token)),
                UserFavoritesDTO.class);
        Assertions.assertThat(user2addId1Response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(user2addId1Response.getBody().getMovieIds()).contains(123);

        // users get respective favorite movies
        Movie movie1 = Movie.builder().title("movie1").id(1).build();
        Movie movie2 = Movie.builder().title("movie2").id(2).build();
        Movie movie3 = Movie.builder().title("movie3").id(123).build();
        Mockito.when(movieDBApiConnect.getMovieById(1)).thenReturn(movie1);
        Mockito.when(movieDBApiConnect.getMovieById(2)).thenReturn(movie2);
        Mockito.when(movieDBApiConnect.getMovieById(123)).thenReturn(movie3);

        ResponseEntity<Movie[]> user1GetMoviesResponse = restTemplate.exchange("/api/soundtracker/user-favorites",
                HttpMethod.GET,
                new HttpEntity<>(createHeader(user1Token)),
                Movie[].class);
        Assertions.assertThat(user1GetMoviesResponse.getBody().length).isEqualTo(2);
        Assertions.assertThat(user1GetMoviesResponse.getBody()).contains(movie1, movie2);

        ResponseEntity<Movie[]> user2GetMoviesResponse = restTemplate.exchange("/api/soundtracker/user-favorites",
                HttpMethod.GET,
                new HttpEntity<>(createHeader(user2Token)),
                Movie[].class);
        Assertions.assertThat(user2GetMoviesResponse.getBody().length).isEqualTo(1);
        Assertions.assertThat(user2GetMoviesResponse.getBody()).contains(movie3);


    }

    public HttpHeaders createHeader(String token) {
        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", "Bearer " + token);
        return header;
    }


}