package com.github.NicolaiKopka.users;

import com.github.NicolaiKopka.db_models.spotifyModels.authenticationModels.SpotifyUserData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpotifyLoginService {

    private final MyUserRepo myUserRepo;

    public MyUser findOrCreateUser(SpotifyUserData userData) {
        return myUserRepo.findByUsername(userData.getUsername()).orElseGet(() -> myUserRepo.save(MyUser.builder()
                .username(userData.getUsername())
                .email(userData.getEmail())
                .roles(List.of("user"))
                .build()));
    }

}
