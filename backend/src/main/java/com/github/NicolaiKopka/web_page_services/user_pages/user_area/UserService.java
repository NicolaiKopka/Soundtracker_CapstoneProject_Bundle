package com.github.NicolaiKopka.web_page_services.user_pages.user_area;

import com.github.NicolaiKopka.dto.UserInfoDTO;
import com.github.NicolaiKopka.users.MyUser;
import com.github.NicolaiKopka.users.MyUserRepo;
import com.github.NicolaiKopka.web_page_services.user_pages.user_favorites.UserFavoritesRepo;
import com.github.NicolaiKopka.web_page_services.user_pages.user_favorites.UserFavoritesSaveObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final MyUserRepo myUserRepo;
    private final UserFavoritesRepo userFavoritesRepo;

    public UserInfoDTO getUserInfo(String username) {

        MyUser user = myUserRepo.findByUsername(username).orElseThrow();
        UserFavoritesSaveObject favoritesObject = userFavoritesRepo.findByUserId(user.getId()).orElseThrow();
        return null;
    }
}
