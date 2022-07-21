package com.github.NicolaiKopka.api_services;

import com.github.NicolaiKopka.db_models.deezerModels.DeezerAlbum;
import com.github.NicolaiKopka.db_models.deezerModels.DeezerSearchData;
import com.github.NicolaiKopka.db_models.deezerModels.DeezerSearchObject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeezerApiConnectTest {

    @Test
    void shouldReturnDeezerSearchDataWithSoundtrackList() {
        DeezerAlbum deezerAlbum = DeezerAlbum.builder().title("testMovie").build();

        DeezerSearchData deezerSearchData = new DeezerSearchData(List.of(new DeezerSearchObject(deezerAlbum)));

        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
        Mockito.when(restTemplate.getForObject(
                "https://api.deezer.com/search?q=testMovie"
                , DeezerSearchData.class)).thenReturn(deezerSearchData);

        DeezerApiConnect deezerApiConnect = new DeezerApiConnect(restTemplate);

        Assertions.assertThat(deezerApiConnect.getFullSearchData("testMovie")).isEqualTo(deezerSearchData);
    }

    @Test
    void shouldReturnDeezerAlbumById() {
        DeezerAlbum deezerAlbum = DeezerAlbum.builder().title("testMovie").id(1234).build();

        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
        Mockito.when(restTemplate.getForObject(
                "https://api.deezer.com/album/1234",
                DeezerAlbum.class)).thenReturn(deezerAlbum);

        DeezerApiConnect deezerApiConnect = new DeezerApiConnect(restTemplate);

        Assertions.assertThat(deezerApiConnect.getAlbumById(1234)).isEqualTo(deezerAlbum);
    }
}