package com.github.NicolaiKopka.web_page_services.main_page;


import com.github.NicolaiKopka.db_models.StreamingTracks;
import com.github.NicolaiKopka.db_models.deezerModels.DeezerTrack;
import com.github.NicolaiKopka.db_models.movieDBModels.Movie;
import com.github.NicolaiKopka.db_models.spotifyModels.SpotifyTrack;
import com.github.NicolaiKopka.db_models.spotifyModels.SpotifyTrackDTO;
import com.github.NicolaiKopka.dto.StreamingStatusDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collection;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/soundtracker")
@RequiredArgsConstructor
public class MainPageController {

    private final MainPageService mainPageService;

    // TODO add bucket4j to limit request count
    @GetMapping
    public List<Movie> getPopularMovies() {
        return mainPageService.getPopularMovies();
    }

    // TODO add bucket4j to limit request count
    @GetMapping("/streaming/{movieName}")
    public StreamingStatusDTO getMovieSoundtrackStreamingStatus(@PathVariable String movieName){
        return mainPageService.getSoundtrackOnSpotify(movieName);
    }
    @GetMapping("/spotify/album/{spotifyId}/{deezerId}")
    public ResponseEntity<StreamingTracks> getSpotifyAlbumById(@PathVariable String spotifyId, @PathVariable String deezerId) {
        try {
            List<SpotifyTrack> spotifyAlbumTracksById = mainPageService.getSpotifyAlbumTracksById(spotifyId);
            List<DeezerTrack> deezerAlbumTracksById = mainPageService.getDeezerAlbumTracksById(deezerId);

            List<SpotifyTrackDTO> spotifyTrackDTOs = spotifyAlbumTracksById.stream().map(spotifyTrack -> {
                SpotifyTrackDTO spotifyTrackDTO = new SpotifyTrackDTO();
                spotifyTrackDTO.setId(spotifyTrack.getId());
                spotifyTrackDTO.setUrl(spotifyTrack.getExternalURLs().getAlbumUrl());
                spotifyTrackDTO.setName(spotifyTrack.getName());
                return spotifyTrackDTO;
            }).toList();

            StreamingTracks streamingTracks = new StreamingTracks();
            streamingTracks.setSpotifyTracks(spotifyTrackDTOs);
            streamingTracks.setDeezerTracks(deezerAlbumTracksById);
            return ResponseEntity.ok(streamingTracks);

        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @GetMapping("/deezer/album/{id}")
    public ResponseEntity<Collection<DeezerTrack>> getDeezerAlbumById(@PathVariable String id) {
        return ResponseEntity.ok(mainPageService.getDeezerAlbumTracksById(id));
    }
    @GetMapping("/search/{query}")
    public ResponseEntity<?> getMoviesByQuery(@PathVariable String query) {
        try {
            return ResponseEntity.ok(mainPageService.getMoviesByQuery(query));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


}
