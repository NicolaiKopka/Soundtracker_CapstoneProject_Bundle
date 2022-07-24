package com.github.NicolaiKopka.users;

import com.github.NicolaiKopka.dto.RegisterUserDTO;
import com.github.NicolaiKopka.users.RegisterData;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final MyUserRepo myUserRepo;
    private final PasswordEncoder encoder;
    public MyUser registerUser(RegisterData registerData) {

        if(registerData.getUsername().contains("(spotifyUser)")){
            throw new IllegalStateException("Illegal username combination");
        }
        if(registerData.getUsername().isBlank() || registerData.getPassword().isBlank() || registerData.getCheckPassword().isBlank()  ||
                registerData.getUsername() == null || registerData.getPassword() == null || registerData.getCheckPassword() == null) {
            throw new IllegalArgumentException("No empty fields allowed");
        }
        if(myUserRepo.findByUsername(registerData.getUsername()).isPresent()) {
            throw new IllegalStateException("User already exists");
        }
        if(!registerData.getPassword().equals(registerData.getCheckPassword())) {
            throw new IllegalStateException("Passwords are not matching");
        }

        MyUser newUser = MyUser.builder().username(registerData.getUsername())
                            .password(encoder.encode(registerData.getPassword()))
                            .roles(List.of("user"))
                            .build();

        return myUserRepo.save(newUser);
    }
}
