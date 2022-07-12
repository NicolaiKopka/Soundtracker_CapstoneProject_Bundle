package com.github.NicolaiKopka.web_page_services.user_page;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/soundtracker/user")
@RequiredArgsConstructor
public class UserPageController {

    private final UserPageService userPageService;

//    @GetMapping
//    public Collection<Movie> getAllMoviesInDB() {
//        return userPageService.getAllMoviesInDB();
//    }
//
//    @PostMapping
//    public Movie addMovieToFavorites(@RequestBody Movie movie) {
//        return userPageService.addMovieToFavorites(movie);
//    }
//
//    @DeleteMapping("/{id}")
//    public void deleteMovieFromDB(@PathVariable String id) {
//        userPageService.deleteMovieFromDB;
//    }
//
//    @PutMapping("/{id}")
//    public void changeNotificationStatusForMovie(@PathVariable String id) {
//        userPageService.changeNotificationStatus(id);
//    }

}
