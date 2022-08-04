import axios, {AxiosResponse} from "axios";
import {
    DeezerLoginResponseDTO,
    LoginResponseDTO,
    MovieItem,
    RegisterUserDTO,
    SpotifyLoginResponseDTO, SpotifyPlaylist, SpotifyTrackDTO,
    SpotifyUserPlaylists,
    StreamingStatusDTO, UserFavoritesDTO
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

export function getDeezerAccessTokenFromBackend(deezerCode: string) {
    return axios.get("/api/deezer/callback" + deezerCode)
        .then((response:AxiosResponse<DeezerLoginResponseDTO>) => response.data)
}

export function getAllUserSpotifyPlaylists() {
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
    }).then((response: AxiosResponse<SpotifyPlaylist>) => response.data)
}

export function getSpotifyAlbumById(id: string) {
    return axios.get("/api/soundtracker/spotify/album/" + id, {
        headers: {
            Authorization: "Bearer " + localStorage.getItem("jwt")
        }
    }).then((response: AxiosResponse<Array<SpotifyTrackDTO>>) => response.data)
}

export function getAllUserPlaylists() {
    return axios.get("/api/soundtracker/user-favorites/all-user-playlists", {
        headers: {
            Authorization: "Bearer " + localStorage.getItem("jwt")
        }
    }).then((response: AxiosResponse<UserFavoritesDTO>) => response.data)
}

export function addTrackToUserPlaylist(playlistName:string, spotifyTrackId:string, deezerTrackId:string) {
    return axios.post("/api/soundtracker/user-favorites/user-playlist/add-track", {
        playlistName,
        spotifyTrackId,
        deezerTrackId
    }, {
        headers: {
            Authorization: "Bearer " + localStorage.getItem("jwt")
        }
    }).then(response => response.data)
}

export function deleteTrackFromUserPlaylist(playlistName:string, spotifyTrackId:string, deezerTrackId:string) {
    return axios.delete("/api/soundtracker/user-favorites/user-playlist/delete-track", {
        headers: {
            Authorization: "Bearer " + localStorage.getItem("jwt")
        }, data: {
            playlistName,
            spotifyTrackId,
            deezerTrackId
        }
    }).then(response => response.data)
}

export function createNewUserPlaylist(playlistName: string) {
   return axios.post(`/api/soundtracker/user-favorites/create-user-playlist/${playlistName}`, {}, {
       headers: {
           Authorization: "Bearer " + localStorage.getItem("jwt")
       }
   }).then(response => response.data)
}

export function deleteUserPlaylist(playlistName: string) {
    return axios.delete(`/api/soundtracker/user-favorites/user-playlist/${playlistName}`, {
        headers: {
            Authorization: "Bearer " + localStorage.getItem("jwt")
        }
    }).then(response => response.data)
}

export function getAllSpotifyTracksInPlaylist(playlistName: string) {
    return axios.get(`/api/soundtracker/user-favorites/tracks/user-playlist/${playlistName}`, {
        headers: {
            Authorization: "Bearer " + localStorage.getItem("jwt")
        }
    }).then((response: AxiosResponse<Array<SpotifyTrackDTO>>) => response.data)
}

export function addTracksToSpotifyPlaylist(playlistId: string, playlistName: string) {
    return axios.post(`/api/soundtracker/user-favorites/user-playlist/to-spotify`, {
        spotifyToken: localStorage.getItem("spotify_jwt"),
        spotifyPlaylistId: playlistId,
        userPlaylistName: playlistName
    }, {
        headers: {
            Authorization: "Bearer " + localStorage.getItem("jwt")
        }
    }).then(response => response.data)
}

