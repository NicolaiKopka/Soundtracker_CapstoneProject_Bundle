import {MovieItem} from "../models";
import "./MainPageMovieCard.css"
import {getStreamingDetails} from "../api_methods";
import {useEffect, useState} from "react";
interface MainPageGalleryProps {
    movie: MovieItem;
}

export default function MainPageMovieCard(props: MainPageGalleryProps) {

    const [spotifyStatus, setSpotifyStatus] = useState(false);
    const [spotifyLink, setSpotifyLink] = useState("")

    useEffect(() => {

    }, [spotifyStatus])

    function getMovieDetails() {
        getStreamingDetails(props.movie.title)
            .then(data => {
                setSpotifyStatus(data.streamingServiceStatus["spotify"])
                setSpotifyLink(data.albumLinks["spotify"])
            })
    }

    return (
        <div>
            <div className={"movie-card"}>
                <button onClick={getMovieDetails}><img src={"https://image.tmdb.org/t/p/original" + props.movie.poster_path} alt={"movies"} onError={undefined}/></button>
                <br/>
                {props.movie.title}
                {spotifyStatus? <div><a href={spotifyLink}>Link to Spotify</a></div> : <div>Spotify not available</div>}
            </div>
        </div>
    )
}