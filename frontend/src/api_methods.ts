import axios, {AxiosResponse} from "axios";
import {
    LoginResponseDTO,
    MovieItem,
    RegisterUserDTO,
    SpotifyLoginResponseDTO, SpotifyTrackDTO,
    SpotifyUserPlaylists,
    StreamingStatusDTO
} from "./models";



export function getStarterPageMovies() {
    return axios.get("/api/soundtracker")
        .then((response: AxiosResponse<MovieItem[]>) => response.data)
}

export function getStreamingDetails(movieName: string) {
    return axios.get("/api/soundtracker/streaming/" + movieName)
        .then((response: AxiosResponse<StreamingStatusDTO>) => response.data)
}

export function searchForMovie(query: string) {
    return axios.get("/api/soundtracker/search/" + query)
        .then((response: AxiosResponse<MovieItem[]>) => response.data)
}

export function loginUser(username: string, password: string) {
    return axios.post("/api/soundtracker/accounts/login", {
        username,
        password
    }).then((response: AxiosResponse<LoginResponseDTO>) => response.data)

}

export function registerUser(username: string, password: string, checkPassword: string) {
    return axios.post("/api/soundtracker/accounts/register", {
        username,
        password,
        checkPassword
    }).then((response: AxiosResponse<RegisterUserDTO>) => response.data)
}

export function addMovieToFavorites(id: number) {
    return axios.put("/api/soundtracker/user-favorites/" + id, {}, {
        headers: {
            Authorization: "Bearer " + localStorage.getItem("jwt")
        }
    }).then(response => response.data)
}

export function getFavoriteUserMovies() {
    return axios.get("/api/soundtracker/user-favorites", {
        headers: {
            Authorization: "Bearer " + localStorage.getItem("jwt")
        }
    }).then((response: AxiosResponse<MovieItem[]>) => response.data)
}

export function deleteMoviesFromFavorites(id: number) {
    return axios.delete("/api/soundtracker/user-favorites/" + id, {
        headers: {
            Authorization: "Bearer " + localStorage.getItem("jwt")
        }
    })
}

export function authorizeWithSpotify() {
    return axios.get(`https://accounts.spotify.com/authorize
                        ?response_type=code&client_id=3ed8e5d98a3b469db405d1bb01652723
                        &scope=user-read-private user-read-email playlist-read-private playlist-read-collaborative playlist-modify-public playlist-modify-private
                        &redirect_uri=${process.env.SPOTIFY_CALLBACK_URI}`)
}

export function getSpotifyAccessTokenFromBackend(spotifyCode: string) {
    return axios.get("/api/spotify/callback" + spotifyCode)
        .then((response:AxiosResponse<SpotifyLoginResponseDTO>) => response.data)
}

export function getAllUserPlaylists() {
    return axios.get("/api/soundtracker/user-favorites/spotify-playlists/" + localStorage.getItem("spotify_jwt"), {
        headers: {
            Authorization: "Bearer " + localStorage.getItem("jwt")
        }
    }).then((response:AxiosResponse<SpotifyUserPlaylists>) => response.data)
}

export function addSpotifyPlaylist(name: string, description: string, isPublic: boolean, collaborative: boolean) {
    return axios.post("/api/soundtracker/user-favorites/spotify-playlists/add/" + localStorage.getItem("spotify_jwt"), {
        name,
        description,
        public: isPublic,
        collaborative
    }, {
        headers: {
            Authorization: "Bearer " + localStorage.getItem("jwt")
        }
    })
}

export function getSpotifyAlbumById(id: string) {
    return axios.get("/api/soundtracker/spotify/album/" + id + "/" + localStorage.getItem("spotify_jwt"), {
        headers: {
            Authorization: "Bearer " + localStorage.getItem("jwt")
        }
    }).then((response: AxiosResponse<Array<SpotifyTrackDTO>>) => response.data)
}
