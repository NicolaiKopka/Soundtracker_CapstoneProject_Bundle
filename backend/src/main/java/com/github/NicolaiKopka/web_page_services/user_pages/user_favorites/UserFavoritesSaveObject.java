package com.github.NicolaiKopka.web_page_services.user_pages.user_favorites;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "userFavorites")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFavoritesSaveObject {

    @Id
    private String id;
    private String userId;
    private List<Integer> movieIds;

}
