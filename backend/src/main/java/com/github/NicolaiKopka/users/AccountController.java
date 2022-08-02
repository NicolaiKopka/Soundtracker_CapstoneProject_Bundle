package com.github.NicolaiKopka.users;

import com.github.NicolaiKopka.dto.LoginResponseDTO;
import com.github.NicolaiKopka.dto.RegisterUserDTO;
import com.github.NicolaiKopka.security.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/soundtracker/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final MyUserRepo myUserRepo;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody RegisterData registerData) {
        try {
            MyUser newlySavedUser = accountService.registerUser(registerData);
            RegisterUserDTO registerUserDTO = RegisterUserDTO.builder().username(newlySavedUser.getUsername()).build();
            return ResponseEntity.status(HttpStatus.CREATED).body(registerUserDTO);
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginUser(@RequestBody LoginData loginData) {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginData.getUsername(), loginData.getPassword()));

            MyUser user = myUserRepo.findByUsername(loginData.getUsername()).orElseThrow();
            Map<String, Object> claims = new HashMap<>();
            claims.put("roles", user.getRoles());

            //following is after deploy to ensure every user's DB entries are modified according to possible code changes
            accountService.modifyDBOnLogin(user);

            return ResponseEntity.ok(new LoginResponseDTO(jwtService.createToken(claims, loginData.getUsername())));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
