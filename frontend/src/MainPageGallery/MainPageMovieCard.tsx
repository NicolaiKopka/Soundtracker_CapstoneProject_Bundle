import {MovieItem} from "../models";
import "./MainPageMovieCard.css"
import {addMovieToFavorites, deleteMoviesFromFavorites, getStreamingDetails} from "../api_methods";
import {useEffect, useState} from "react";

interface MainPageGalleryProps {
    movie: MovieItem
    favoriteMovieIds: Array<number>
    getUserMovies: Function
}

export default function MainPageMovieCard(props: MainPageGalleryProps) {

    const [spotifyStatus, setSpotifyStatus] = useState(false)
    const [spotifyLink, setSpotifyLink] = useState("")
    const [favoriteStatus, setFavoriteStatus] = useState(false)


    useEffect(() => {
        if(props.favoriteMovieIds.includes(props.movie.id)) {
            setFavoriteStatus(true)
        }
    }, [props.favoriteMovieIds, props.movie.id])

    function getMovieDetails() {
        getStreamingDetails(props.movie.title)
            .then(data => {
                setSpotifyStatus(data.streamingServiceStatus["spotify"])
                setSpotifyLink(data.albumLinks["spotify"])
            })
    }

    function addToFavorites() {
        addMovieToFavorites(props.movie.id)
            .then(() => setFavoriteStatus(true))
            .then(() => props.getUserMovies)
            .catch()

    }

    function deleteFromFavorites() {
        deleteMoviesFromFavorites(props.movie.id)
            .then(() => setFavoriteStatus(false))
            .then(() => props.getUserMovies)
            .catch()

    }

    return (
        <div>
            <div className={"movie-card"}>
                {/* check undefined on Error. Should be fallback image*/}
                <button onClick={getMovieDetails}><img src={"https://image.tmdb.org/t/p/original" + props.movie.poster_path} alt={"movies"} onError={undefined}/></button>
                <br/>
                {props.movie.title}<br/>
                {spotifyStatus? <div><a href={spotifyLink}>Link to Spotify</a></div> : <div>Spotify not available</div>}
                {favoriteStatus ? <button onClick={deleteFromFavorites}>Delete From Favorite</button> : <button onClick={addToFavorites}>To Favorites</button>}
            </div>
        </div>
    )
}