import {MovieItem} from "../models";
import "./MainPageMovieCard.css"
import {addMovieToFavorites, deleteMoviesFromFavorites, getStreamingDetails} from "../api_methods";
import {useEffect, useRef, useState} from "react";
import 'react-slideshow-image/dist/styles.css';
import altImage from "../images/page-not-found-ge8454dec1_1280.jpg"
import {NavLink} from "react-router-dom";


interface MainPageGalleryProps {
    movie: MovieItem
    favoriteMovieIds: Array<number>
    getUserMovies: Function
}

export default function MainPageMovieCard(props: MainPageGalleryProps) {

    const [spotifyStatus, setSpotifyStatus] = useState(false)
    const [spotifyAlbumId, setSpotifyAlbumId] = useState("")
    const [spotifyLink, setSpotifyLink] = useState("")
    const [deezerStatus, setDeezerStatus] = useState(false)
    const [deezerLink, setDeezerLink] = useState("")
    const [favoriteStatus, setFavoriteStatus] = useState(false)
    const [cardElement, setCardElement] = useState({} as HTMLDivElement)
    const ref = useRef({} as HTMLDivElement);

    useEffect(() => {
        setCardElement(ref.current)
        if (props.favoriteMovieIds.includes(props.movie.id)) {
            setFavoriteStatus(true)
        }
    }, [props.favoriteMovieIds, props.movie.id])

    function getMovieDetails() {
        cardElement.classList.toggle("is-flipped")
        getStreamingDetails(props.movie.title)
            .then(data => {
                setSpotifyStatus(data.streamingServiceStatus["spotify"])
                setSpotifyLink(data.albumLinks["spotify"])
                setSpotifyAlbumId(data.albumIds["spotify"])
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

    function flipCard() {
        cardElement.classList.toggle("is-flipped")
    }

    return (
        <div className={"card-wrapper"}>
            <div className={"movie-card"}>
                <div ref={ref} className={"card-inner"} >
                    <div className={"card-face card-front"}>
                        <button className={"card-image"} onClick={getMovieDetails}><img
                            src={props.movie.poster_path ? "https://image.tmdb.org/t/p/original" + props.movie.poster_path : altImage} alt={"movies"}
                            onError={(ev) => {
                                ev.currentTarget.onerror = null;
                                ev.currentTarget.src = altImage
                            }}/></button>
                        <div className={"card-title"}>
                            {props.movie.title}
                        </div>
                        {favoriteStatus ?
                            <button className={"favorites-button-card"} onClick={deleteFromFavorites}>Delete From Favorite</button> :
                            <button className={"favorites-button-card"} onClick={addToFavorites}>To Favorites</button>}
                    </div>
                    <div onClick={flipCard} className={"card-face card-back"}>
                        {spotifyStatus ? <div><div className={"card-status"}><a href={spotifyLink}>Link to Spotify</a></div>
                                <div><NavLink to={`/tracks/${spotifyAlbumId}`}>To Track List</NavLink></div></div>:
                            <div className={"card-status"}>Spotify not available</div>}
                        {deezerStatus ? <div className={"card-status"}><a href={deezerLink}>Link to Deezer</a></div> :
                            <div className={"card-status"}>Deezer not available</div>}
                        {favoriteStatus ?
                            <button className={"favorites-button-card"} onClick={deleteFromFavorites}>Delete From Favorite</button> :
                            <button className={"favorites-button-card"} onClick={addToFavorites}>To Favorites</button>}
                    </div>
                </div>
            </div>
        </div>


    )
}