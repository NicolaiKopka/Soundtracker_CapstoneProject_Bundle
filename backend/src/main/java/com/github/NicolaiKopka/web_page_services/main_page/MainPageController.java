package com.github.NicolaiKopka.web_page_services.main_page;


import com.github.NicolaiKopka.db_models.movieDBModels.Movie;
import com.github.NicolaiKopka.db_models.spotifyModels.SpotifyTrack;
import com.github.NicolaiKopka.db_models.spotifyModels.SpotifyTrackDTO;
import com.github.NicolaiKopka.dto.StreamingStatusDTO;
import lombok.RequiredArgsConstructor;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

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
    public StreamingStatusDTO getMovieSoundtrackStreamingStatus(@PathVariable String movieName) throws OAuthProblemException, OAuthSystemException {
        return mainPageService.getSoundtrackOnSpotify(movieName);
    }
    @GetMapping("/spotify/album/{id}/{token}")
    public ResponseEntity<List<SpotifyTrackDTO>> getSpotifyAlbumById(@PathVariable String id, @PathVariable String token) {
        try {
            List<SpotifyTrack> spotifyAlbumTracksById = mainPageService.getSpotifyAlbumTracksById(token, id);
            List<SpotifyTrackDTO> spotifyTrackDTO = spotifyAlbumTracksById.stream().map(track -> {
                SpotifyTrackDTO trackDTO = new SpotifyTrackDTO();
                trackDTO.setName(track.getName());
                trackDTO.setUrl(track.getExternalURLs().getAlbumUrl());
                trackDTO.setId(track.getId());
                return trackDTO;
            }).toList();
            return ResponseEntity.ok(spotifyTrackDTO);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/search/{query}")
    public ResponseEntity getMoviesByQuery(@PathVariable String query) {
        try {
            return ResponseEntity.ok(mainPageService.getMoviesByQuery(query));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


}
