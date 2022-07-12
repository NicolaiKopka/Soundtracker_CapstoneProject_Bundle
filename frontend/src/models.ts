

export interface MovieItem {
    title: string;
    id: number;
    backdrop_path: string;
    poster_path: string;
    release_date: string;
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

// const o = {}
// let keys = Object.keys(o)
// keys.forEach(key => o[key])
