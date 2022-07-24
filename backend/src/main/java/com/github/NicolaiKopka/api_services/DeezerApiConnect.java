package com.github.NicolaiKopka.api_services;

import com.github.NicolaiKopka.db_models.AlbumReferenceDTO;
import com.github.NicolaiKopka.db_models.deezerModels.DeezerAlbum;
import com.github.NicolaiKopka.db_models.deezerModels.DeezerSearchData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeezerApiConnect {

    private final RestTemplate restTemplate;

    public List<AlbumReferenceDTO> getFullSearchData(String movieName) {

        String queryUrl = "https://api.deezer.com/search?q=" + movieName;
        DeezerSearchData deezerSearchData = restTemplate.getForObject(queryUrl, DeezerSearchData.class);

        if(deezerSearchData == null) {
            return List.of();
        }

        return deezerSearchData.getData().stream().map(searchObject -> {
            AlbumReferenceDTO albumReference = new AlbumReferenceDTO();
            albumReference.setMovieTitle(searchObject.getAlbum().getTitle());
            albumReference.setDeezerAlbumId(searchObject.getAlbum().getId());
            return albumReference;
        }).toList();
    }

    public AlbumReferenceDTO getAlbumById(long id, AlbumReferenceDTO currentAlbum) {

        String queryUrl = "https://api.deezer.com/album/" + id;
        DeezerAlbum deezerAlbum = restTemplate.getForObject(queryUrl, DeezerAlbum.class);

        if(deezerAlbum == null) {
            return null;
        }

        currentAlbum.setAlbumUrl(deezerAlbum.getLink());
        return currentAlbum;
    }

}
