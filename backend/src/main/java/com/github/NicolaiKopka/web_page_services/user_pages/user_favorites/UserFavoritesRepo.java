package com.github.NicolaiKopka.web_page_services.user_pages.user_favorites;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface UserFavoritesRepo extends MongoRepository<UserFavoritesSaveObject, String> {

    Optional<UserFavoritesSaveObject> findByUserId(String userId);

}
