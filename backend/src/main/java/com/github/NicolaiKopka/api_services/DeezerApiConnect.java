package com.github.NicolaiKopka.api_services;

import com.github.NicolaiKopka.db_models.AlbumReferenceDTO;
import com.github.NicolaiKopka.db_models.deezerModels.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
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

    public List<DeezerTrack> getAllTracksByAlbumId(long id) {
        String queryUrl = "https://api.deezer.com/album/" + id + "/tracks";
        DeezerMultiTracks deezerAllTracks = restTemplate.getForObject(queryUrl, DeezerMultiTracks.class);

        if(deezerAllTracks == null) {
            return new ArrayList<>();
        }

        return deezerAllTracks.getData();

    }
    public DeezerAddPlaylistDTO createNewAlbum(DeezerAddPlaylistDTO playlistDTO) {
        String queryUrl = "https://api.deezer.com/user/me/playlists?title=" +
                playlistDTO.getPlaylistName() +
                "&access_token=" + playlistDTO.getDeezerToken();

        DeezerAddPlaylistDTO returnPlaylist = restTemplate.postForObject(queryUrl, playlistDTO, DeezerAddPlaylistDTO.class);
        returnPlaylist.setPlaylistName(playlistDTO.getPlaylistName());
        return returnPlaylist;
    }

    public void addTracksInUserPlaylistToNewDeezerPlaylist(List<String> deezerTrackIds, String playlistId, String deezerToken) {
        StringBuilder builder = new StringBuilder();
        deezerTrackIds.forEach(id -> builder.append(id).append(","));
        builder.deleteCharAt(builder.length() - 1);
        String allTrackIds = builder.toString();

        String queryUrl = "https://api.deezer.com/playlist/" + playlistId + "/tracks?songs=" +
                allTrackIds + "&access_token=" +
                deezerToken;

        DeezerAddPlaylistDTO empty = new DeezerAddPlaylistDTO();

        restTemplate.postForObject(queryUrl, empty, Void.class);
    }
}
