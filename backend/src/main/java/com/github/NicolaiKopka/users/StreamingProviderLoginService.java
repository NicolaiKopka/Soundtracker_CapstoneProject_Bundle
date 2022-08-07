package com.github.NicolaiKopka.users;

import com.github.NicolaiKopka.db_models.deezerModels.DeezerUserData;
import com.github.NicolaiKopka.db_models.spotifyModels.authenticationModels.SpotifyUserData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StreamingProviderLoginService {

    private final MyUserRepo myUserRepo;

    public MyUser findOrCreateSpotifyUser(SpotifyUserData userData) {
        return myUserRepo.findByUsername(userData.getUsername() + "(spotifyUser)").orElseGet(() -> myUserRepo.save(MyUser.builder()
                .username(userData.getUsername() + "(spotifyUser)")
                .email(userData.getEmail())
                .spotifyId(userData.getSpotifyUserId())
                .roles(List.of("user"))
                .build()));
    }

    public MyUser findOrCreateDeezerUser(DeezerUserData deezerUserData) {
        return myUserRepo.findByUsername(deezerUserData.getName() + "(deezerUser)").orElseGet(() -> myUserRepo.save(MyUser.builder()
                .username(deezerUserData.getName() + "(deezerUser)")
                .email(deezerUserData.getEmail())
                .spotifyId(deezerUserData.getEmail())
                .roles(List.of("user"))
                .build()));
    }

    public void addSpotifyIdForNonSpotifyUser(String username, String spotifyId) {
        MyUser user = myUserRepo.findByUsername(username).orElseThrow();
        if(user.getSpotifyId() == null) {
            user.setSpotifyId(spotifyId);
            myUserRepo.save(user);
        }
    }
}
