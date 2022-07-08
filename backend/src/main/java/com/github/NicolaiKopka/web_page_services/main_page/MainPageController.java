package com.github.NicolaiKopka.web_page_services.main_page;


import com.github.NicolaiKopka.db_models.movieDBModels.Movie;
import lombok.RequiredArgsConstructor;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/soundtracker")
@RequiredArgsConstructor
public class MainPageController {

    private final MainPageService mainPageService;

    @GetMapping
    public List<Movie> getAllMovies() {
        return mainPageService.getPopularMovies();
    }

//    @GetMapping("/{id}")
//    public Movie getMovieById(@PathVariable String id) {
//        return mainPageService.getMovieById(id);
//    }

    @GetMapping("/spotify")
    public void getMovieOnSpotify() throws OAuthProblemException, OAuthSystemException {
        mainPageService.getSoundtrackOnSpotify();
    }
}
