package com.github.NicolaiKopka.users;

import com.github.NicolaiKopka.web_page_services.user_pages.user_favorites.UserFavoritesRepo;
import com.github.NicolaiKopka.web_page_services.user_pages.user_favorites.UserFavoritesSaveObject;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final UserFavoritesRepo userFavoritesRepo;
    private final MyUserRepo myUserRepo;
    private final PasswordEncoder encoder;
    public MyUser registerUser(RegisterData registerData) {

        if(registerData.getUsername().contains("(") || registerData.getUsername().contains(")")){
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

        MyUser setupUser = myUserRepo.save(newUser);

        UserFavoritesSaveObject saveObject = new UserFavoritesSaveObject();
        saveObject.setUserId(setupUser.getId());
        userFavoritesRepo.save(saveObject);

        return setupUser;
    }

    //after deploy section to modify DB on next login with possible changes in backend handling
    public void modifyDBOnLogin(MyUser user) {

        //ensures every user has a related UserFavoriteSaveObject or has one created on next login
        if(userFavoritesRepo.findByUserId(user.getId()).isEmpty()) {
            UserFavoritesSaveObject saveObject = new UserFavoritesSaveObject();
            saveObject.setUserId(user.getId());
            userFavoritesRepo.save(saveObject);
        }
    }
}
