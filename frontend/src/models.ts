

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

export interface SpotifyUserPlaylists {
    items: Array<SpotifyPlaylist>
}

export interface SpotifyPlaylist {
    description: string
    name: string
    href: string
}

// const o = {}
// let keys = Object.keys(o)
// keys.forEach(key => o[key])
