package com.github.NicolaiKopka.web_page_services.main_page;


import com.github.NicolaiKopka.db_models.movieDBModels.Movie;
import com.github.NicolaiKopka.dto.StreamingStatusDTO;
import lombok.RequiredArgsConstructor;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Stream;

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

//    @GetMapping("/{id}")
//    public Movie getMovieById(@PathVariable String id) {
//        return mainPageService.getMovieById(id);
//    }

    // TODO add bucket4j to limit request count
    @GetMapping("/streaming/{movieName}")
    public StreamingStatusDTO getMovieSoundtrackOnSpotify(@PathVariable String movieName) throws OAuthProblemException, OAuthSystemException {
        return mainPageService.getSoundtrackOnSpotify(movieName);
    }
}
