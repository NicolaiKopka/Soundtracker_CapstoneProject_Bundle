import axios, {AxiosResponse} from "axios";
import {StreamingStatusDTO} from "./models";



export function getStarterPageMovies() {
    return axios.get("api/soundtracker")
        .then((response) => response.data)
}

export function getStreamingDetails(movieName: string) {
    return axios.get("api/soundtracker/streaming/" + movieName)
        .then((response: AxiosResponse<StreamingStatusDTO>) => response.data)
}