package com.github.NicolaiKopka.users;

import com.github.NicolaiKopka.dto.LoginResponseDTO;
import com.github.NicolaiKopka.dto.RegisterUserDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerIT {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    MyUserRepo userRepo;

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


}