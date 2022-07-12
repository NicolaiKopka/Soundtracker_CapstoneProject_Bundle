package com.github.NicolaiKopka.users;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceTest {

    @Test
    void shouldSaveNewUser() {

        RegisterData newUser = RegisterData.builder().username("user")
                .password("password")
                .checkPassword("password")
                .build();

        MyUserRepo myUserRepo = Mockito.mock(MyUserRepo.class);

        PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);
        Mockito.when(encoder.encode("password")).thenReturn("hashedPassword");

        MyUser expectedUser = MyUser.builder().username("user").password("hashedPassword").build();

        AccountService accountService = new AccountService(myUserRepo, encoder);
        accountService.registerUser(newUser);

        Mockito.verify(myUserRepo).save(expectedUser);
    }

    @Test
    void shouldThrowIfEmptyRegisterDataFieldsDetected() {

        RegisterData newUser = RegisterData.builder().username("")
                .password("password")
                .checkPassword("password")
                .build();

        MyUserRepo myUserRepo = Mockito.mock(MyUserRepo.class);

        PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);

        AccountService accountService = new AccountService(myUserRepo, encoder);
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

        AccountService accountService = new AccountService(myUserRepo, encoder);
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

        AccountService accountService = new AccountService(myUserRepo, encoder);
        try{
            accountService.registerUser(newUser);
            fail();
        } catch (IllegalStateException e) {
            Assertions.assertThat(e.getMessage()).isEqualTo("Passwords are not matching");
        }
    }

}