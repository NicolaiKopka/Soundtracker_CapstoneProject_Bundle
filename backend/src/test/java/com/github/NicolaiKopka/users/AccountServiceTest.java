package com.github.NicolaiKopka.users;

import com.github.NicolaiKopka.web_page_services.user_pages.user_favorites.UserFavoritesRepo;
import com.github.NicolaiKopka.web_page_services.user_pages.user_favorites.UserFavoritesSaveObject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceTest {

    @Test
    void shouldSaveNewUser() {
        MyUser userBeforeSave = MyUser.builder().username("user").password("hashedPassword").roles(List.of("user")).build();
        MyUser expectedUser = MyUser.builder().id("1234").username("user").password("hashedPassword").roles(List.of("user")).build();
        UserFavoritesSaveObject saveObject = UserFavoritesSaveObject.builder()
                .userId("1234")
                .userPlaylists(new HashMap<>())
                .movieIds(new ArrayList<>())
                .build();

        RegisterData newUser = RegisterData.builder().username("user")
                .password("password")
                .checkPassword("password")
                .build();

        PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);
        Mockito.when(encoder.encode("password")).thenReturn("hashedPassword");

        MyUserRepo myUserRepo = Mockito.mock(MyUserRepo.class);
        Mockito.when(myUserRepo.save(userBeforeSave)).thenReturn(expectedUser);

        UserFavoritesRepo userFavoritesRepo = Mockito.mock(UserFavoritesRepo.class);

        AccountService accountService = new AccountService(userFavoritesRepo, myUserRepo, encoder);
        MyUser actual = accountService.registerUser(newUser);

        Assertions.assertThat(actual).isEqualTo(expectedUser);
        Mockito.verify(userFavoritesRepo).save(saveObject);
    }

    @Test
    void shouldThrowIfEmptyRegisterDataFieldsDetected() {

        RegisterData newUser = RegisterData.builder().username("")
                .password("password")
                .checkPassword("password")
                .build();

        MyUserRepo myUserRepo = Mockito.mock(MyUserRepo.class);

        PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);

        UserFavoritesRepo userFavoritesRepo = Mockito.mock(UserFavoritesRepo.class);

        AccountService accountService = new AccountService(userFavoritesRepo, myUserRepo, encoder);
        try{
            accountService.registerUser(newUser);
            fail();
        } catch (IllegalArgumentException e) {
            Assertions.assertThat(e.getMessage()).isEqualTo("No empty fields allowed");
        }
    }

    @Test
    void shouldThrowIfUserAlreadyExists() {

        RegisterData newUser = RegisterData.builder().username("user")
                .password("password")
                .checkPassword("password")
                .build();

        MyUser existingUser = MyUser.builder().username("user").password("1234").build();

        MyUserRepo myUserRepo = Mockito.mock(MyUserRepo.class);
        Mockito.when(myUserRepo.findByUsername("user")).thenReturn(Optional.of(existingUser));

        PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);

        UserFavoritesRepo userFavoritesRepo = Mockito.mock(UserFavoritesRepo.class);

        AccountService accountService = new AccountService(userFavoritesRepo, myUserRepo, encoder);
        try{
            accountService.registerUser(newUser);
            fail();
        } catch (IllegalStateException e) {
            Assertions.assertThat(e.getMessage()).isEqualTo("User already exists");
        }
    }

    @Test
    void shouldThrowIfPasswordsAreNotMatching() {

        RegisterData newUser = RegisterData.builder().username("user")
                .password("password")
                .checkPassword("wrongPassword")
                .build();

        MyUserRepo myUserRepo = Mockito.mock(MyUserRepo.class);

        PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);

        UserFavoritesRepo userFavoritesRepo = Mockito.mock(UserFavoritesRepo.class);

        AccountService accountService = new AccountService(userFavoritesRepo, myUserRepo, encoder);
        try{
            accountService.registerUser(newUser);
            fail();
        } catch (IllegalStateException e) {
            Assertions.assertThat(e.getMessage()).isEqualTo("Passwords are not matching");
        }
    }

    @Test
    void shouldThrowIfUsernameContainsIllegalCombinations() {
        RegisterData newUser = RegisterData.builder().username("user(spotifyUser)")
                .password("password")
                .checkPassword("password")
                .build();

        MyUserRepo myUserRepo = Mockito.mock(MyUserRepo.class);

        PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);

        UserFavoritesRepo userFavoritesRepo = Mockito.mock(UserFavoritesRepo.class);

        AccountService accountService = new AccountService(userFavoritesRepo, myUserRepo, encoder);
        try{
            accountService.registerUser(newUser);
            fail();
        } catch (IllegalStateException e) {
            Assertions.assertThat(e.getMessage()).isEqualTo("Illegal username combination");
        }
    }

    //test section for after deploy on login DB modification
    @Test
    void shouldCreateRespectiveUserFavoritesSaveObjectIfNonPresentOnLogin() {
        MyUser registeredUser = MyUser.builder().username("user")
                .password("hashedPW").roles(List.of("user")).id("1234").build();

        UserFavoritesSaveObject expectedSaveObject = UserFavoritesSaveObject.builder().userId("1234")
                .userPlaylists(new HashMap<>()).movieIds(new ArrayList<>()).build();

        UserFavoritesRepo userFavoritesRepo = Mockito.mock(UserFavoritesRepo.class);
        Mockito.when(userFavoritesRepo.findByUserId("1234")).thenReturn(Optional.empty());

        MyUserRepo myUserRepo = Mockito.mock(MyUserRepo.class);

        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);

        AccountService accountService = new AccountService(userFavoritesRepo, myUserRepo, passwordEncoder);
        accountService.modifyDBOnLogin(registeredUser);

        Mockito.verify(userFavoritesRepo).save(expectedSaveObject);

    }

}