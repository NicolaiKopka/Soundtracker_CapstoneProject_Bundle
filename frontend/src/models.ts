

export interface MovieItem {
    title: string;
    id: number;
    backdrop_path: string;
    poster_path: string;
    release_date: string;
}

export interface UserInfoDTO {
    username: string
    movieIds: Array<number>
}

export interface StreamingStatusDTO {
    movieName: string
    streamingServiceStatus: {
        [key: string]: boolean
    }
    albumLinks: {
        [key: string]: string
    }
    albumIds: {
        [key: string]: string
    }
}

export interface RegisterUserDTO {
    username: string
}

export interface LoginResponseDTO {
    token: string
}

export interface SpotifyLoginResponseDTO {
    jwtToken: string
    spotifyToken: string
}

export interface DeezerLoginResponseDTO {
    jwtToken: string
    deezerToken: string
}

export interface SpotifyUserPlaylists {
    items: Array<SpotifyPlaylist>
}

export interface SpotifyPlaylist {
    description: string
    name: string
    href: string
    id: string
}

export interface SpotifyTrackDTO {
    name: string
    url: string
    id: string
}

export interface DeezerTrack {
    id: string
    title: string
    link: string
}

export interface UserFavoritesDTO {
    movieIds: Array<number>
    userPlaylists: UserPlaylistMap
}

export interface UserPlaylistMap {
    [key: string]: UserPlaylist
}

export interface UserPlaylist {
    playlistName: string
    spotifyTrackIds: Array<string>
    deezerTrackIds: Array<string>
}

export interface DeezerAddPlaylistDTO {
    deezerToken: string
    playlistName: string
    id: string
}

export interface StreamingTracks {
    spotifyTracks: Array<SpotifyTrackDTO>
    deezerTracks: Array<DeezerTrack>
}




// const o = {}
// let keys = Object.keys(o)
// keys.forEach(key => o[key])
