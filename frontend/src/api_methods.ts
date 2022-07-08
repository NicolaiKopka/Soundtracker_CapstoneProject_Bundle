import axios, {AxiosResponse} from "axios";



export function getStarterPageMovies() {
    return axios.get("api/soundtracker")
        .then((response) => response.data)
}