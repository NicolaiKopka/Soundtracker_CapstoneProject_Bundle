import {MovieItem} from "../models";
import "./MainPageMovieCard.css"
import {addMovieToFavorites, deleteMoviesFromFavorites, getStreamingDetails} from "../api_methods";
import {useEffect, useState} from "react";
import 'react-slideshow-image/dist/styles.css';


interface MainPageGalleryProps {
    movie: MovieItem
    favoriteMovieIds: Array<number>
    getUserMovies: Function
}

export default function MainPageMovieCard(props: MainPageGalleryProps) {

    const [spotifyStatus, setSpotifyStatus] = useState(false)
    const [spotifyLink, setSpotifyLink] = useState("")
    const [deezerStatus, setDeezerStatus] = useState(false)
    const [deezerLink, setDeezerLink] = useState("")
    const [favoriteStatus, setFavoriteStatus] = useState(false)


    useEffect(() => {
        if (props.favoriteMovieIds.includes(props.movie.id)) {
            setFavoriteStatus(true)
        }
    }, [props.favoriteMovieIds, props.movie.id])

    function getMovieDetails() {
        getStreamingDetails(props.movie.title)
            .then(data => {
                setSpotifyStatus(data.streamingServiceStatus["spotify"])
                setSpotifyLink(data.albumLinks["spotify"])
                setDeezerStatus(data.streamingServiceStatus["deezer"])
                setDeezerLink(data.albumLinks["deezer"])
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
            .then(() => props.getUserMovies())
            .catch()

    }

    return (
        <div className={"card-wrapper"}>
            <div className={"movie-card"}>
                {/* check undefined on Error. Should be fallback image*/}
                <button className={"card-image"} onClick={getMovieDetails}><img
                    src={"https://image.tmdb.org/t/p/original" + props.movie.poster_path} alt={"movies"}
                    onError={undefined}/></button>
                <div className={"card-title"}>
                    {props.movie.title}
                </div>
                {spotifyStatus ? <div className={"card-status"}><a href={spotifyLink}>Link to Spotify</a></div> :
                    <div className={"card-status"}>Spotify not available</div>}
                {deezerStatus ? <div className={"card-status"}><a href={deezerLink}>Link to Deezer</a></div> :
                    <div className={"card-status"}>Deezer not available</div>}
                {favoriteStatus ?
                    <button className={"favorites-button"} onClick={deleteFromFavorites}>Delete From Favorite</button> :
                    <button className={"favorites-button"} onClick={addToFavorites}>To Favorites</button>}
            </div>
        </div>


    )
}