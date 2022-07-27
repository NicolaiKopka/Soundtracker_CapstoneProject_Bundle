package com.github.NicolaiKopka.dto;

import com.github.NicolaiKopka.db_models.userPlaylistModels.UserPlaylist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserFavoritesDTO {

    private List<Integer> movieIds;
    private Map<String, UserPlaylist> userPlaylists;

}
