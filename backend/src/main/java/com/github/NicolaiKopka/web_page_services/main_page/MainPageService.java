package com.github.NicolaiKopka.web_page_services.main_page;

import com.github.NicolaiKopka.api_services.MovieDBApiConnect;
import com.github.NicolaiKopka.api_services.SpotifyApiConnect;
import com.github.NicolaiKopka.db_models.movieDBModels.Movie;
import com.github.NicolaiKopka.db_models.spotifyModels.SpotifyAlbum;
import com.github.NicolaiKopka.dto.StreamingStatusDTO;
import lombok.RequiredArgsConstructor;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.springframework.stereotype.Service;


import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MainPageService {

    private final MovieDBApiConnect movieDBApiConnect;
    private final SpotifyApiConnect spotifyApiConnect;

    public List<Movie> getPopularMovies() {
        return movieDBApiConnect.getPopularMovies();
    }

    public StreamingStatusDTO getSoundtrackOnSpotify(String movieName) throws OAuthProblemException, OAuthSystemException {
        StreamingStatusDTO streamingStatusDTO = new StreamingStatusDTO();
        streamingStatusDTO.setMovieName(movieName);

        List<SpotifyAlbum> albums = spotifyApiConnect.getSpotifyListOfMovieAlbums(movieName);

        checkForExactSoundtrack(albums, movieName).ifPresentOrElse(
                album -> {
                    String link = album.getExternalURLs().getAlbumUrl();
                    streamingStatusDTO.getStreamingServiceStatus().put("spotify", true);
                    streamingStatusDTO.getAlbumLinks().put("spotify", link);
                },
                () -> streamingStatusDTO.getStreamingServiceStatus().put("spotify", false)
        );

        return streamingStatusDTO;
    }

    private Optional<SpotifyAlbum> checkForExactSoundtrack(List<SpotifyAlbum> albumList, String movieName) {
        return albumList.stream()
                .filter(album -> checkForExactTitle(album.getName(), movieName) && checkForKeywords(album))
                .findFirst();
    }
    private boolean checkForKeywords(SpotifyAlbum album) {
        return album.getName().toLowerCase().contains("official") || album.getName().toLowerCase().contains("motion picture")
                || album.getName().toLowerCase().contains("soundtrack") || album.getName().toLowerCase().contains("netflix");
    }
    private boolean checkForExactTitle(String albumTitle, String movieName){
        String albumTitleLower = albumTitle.split("\\(")[0].toLowerCase();
        if (albumTitleLower.charAt(albumTitleLower.length() - 1) == ' ') {
            StringBuilder builder = new StringBuilder(albumTitleLower);
            albumTitleLower = builder.deleteCharAt(albumTitleLower.length() - 1)
                    .toString();
        }

        String movieNameLower = movieName.toLowerCase();

        return albumTitleLower.equals(movieNameLower);
    }

    public Collection<Movie> getMoviesByQuery(String query) {
        Collection<Movie> movies = movieDBApiConnect.getMoviesByQuery(query);
        if(movies.isEmpty()) {
            throw new RuntimeException("No movies found");
        }
        return movies;
    }
}
