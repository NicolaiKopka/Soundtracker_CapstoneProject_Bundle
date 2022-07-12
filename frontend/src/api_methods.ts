import axios, {AxiosResponse} from "axios";
import {LoginResponseDTO, MovieItem, RegisterUserDTO, StreamingStatusDTO} from "./models";



export function getStarterPageMovies() {
    return axios.get("api/soundtracker")
        .then((response: AxiosResponse<MovieItem[]>) => response.data)
}

export function getStreamingDetails(movieName: string) {
    return axios.get("api/soundtracker/streaming/" + movieName)
        .then((response: AxiosResponse<StreamingStatusDTO>) => response.data)
}

export function searchForMovie(query: string) {
    return axios.get("api/soundtracker/search/" + query)
        .then((response: AxiosResponse<MovieItem[]>) => response.data)
}

export function loginUser(username: string, password: string) {
    return axios.post("api/soundtracker/accounts/login", {
        username,
        password
    }).then((response: AxiosResponse<LoginResponseDTO>) => response.data)

}

export function registerUser(username: string, password: string, checkPassword: string) {
    return axios.post("api/soundtracker/accounts/register", {
        username,
        password,
        checkPassword
    }).then((response: AxiosResponse<RegisterUserDTO>) => response.data)
}