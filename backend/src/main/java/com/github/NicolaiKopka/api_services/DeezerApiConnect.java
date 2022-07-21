package com.github.NicolaiKopka.api_services;

import com.github.NicolaiKopka.db_models.deezerModels.DeezerAlbum;
import com.github.NicolaiKopka.db_models.deezerModels.DeezerSearchData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class DeezerApiConnect {

    private final RestTemplate restTemplate;

    public DeezerSearchData getFullSearchData(String movieName) {

        String queryUrl = "https://api.deezer.com/search?q=" + movieName;
        return restTemplate.getForObject(queryUrl, DeezerSearchData.class);
    }

    public DeezerAlbum getAlbumById(long id) {

        String queryUrl = "https://api.deezer.com/album/" + id;
        return restTemplate.getForObject(queryUrl, DeezerAlbum.class);

    }

}
