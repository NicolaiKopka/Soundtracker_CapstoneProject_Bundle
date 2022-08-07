import {MovieItem} from "../models";
import "./MainPageMovieCard.css"
import {addMovieToFavorites, deleteMoviesFromFavorites, getStreamingDetails} from "../api_methods";
import {useEffect, useRef, useState} from "react";
import 'react-slideshow-image/dist/styles.css';
import altImage from "../images/page-not-found-ge8454dec1_1280.jpg"
import spotifyImg from "../images/Spotify_Icon_RGB_Green.png"
import deezerImg from "../images/Colored_Equalizer@2x.png"
import {NavLink} from "react-router-dom";
import toast from "react-hot-toast";


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
    const [deezerAlbumId, setDeezerAlbumId] = useState("")
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
                setDeezerAlbumId(data.albumIds["deezer"])
            })
    }

    function addToFavorites() {
        addMovieToFavorites(props.movie.id)
            .then(() => setFavoriteStatus(true))
            .then(() => props.getUserMovies)
            .then(() => toast.success("Movie added to favorites"))
            .catch(error => error.response.data)

    }

    function deleteFromFavorites() {
        deleteMoviesFromFavorites(props.movie.id)
            .then(() => setFavoriteStatus(false))
            .then(() => props.getUserMovies())
            .then(() => toast.success("Movie deleted from favorites"))
            .catch(error => error.response.data)
    }

    function flipCard() {
        cardElement.classList.toggle("is-flipped")
    }

    return (
        <div className={"card-wrapper"}>
            <div className={"movie-card"}>
                <div ref={ref} className={"card-inner"}>
                    <div className={"card-face card-front"}>
                        <button className={"card-image"} onClick={getMovieDetails}><img
                            src={props.movie.poster_path ? "https://image.tmdb.org/t/p/original" + props.movie.poster_path : altImage}
                            alt={"movies"}
                            onError={(ev) => {
                                ev.currentTarget.onerror = null;
                                ev.currentTarget.src = altImage
                            }}/></button>
                        <div className={"card-title"}>
                            {props.movie.title}
                        </div>
                        {localStorage.getItem("jwt") &&
                            <div>
                                {favoriteStatus ?
                                    <button className={"favorites-button-card"} onClick={deleteFromFavorites}>Delete
                                        From Favorite</button> :
                                    <button className={"favorites-button-card"} onClick={addToFavorites}>To
                                        Favorites</button>}
                            </div>
                        }
                    </div>
                    <div onClick={flipCard} className={"card-face card-back back-card-body"}>
                        <div className={"card-title"}>
                            {props.movie.title}
                        </div>
                        {spotifyStatus ? <div>
                                <div className={"card-status"}><a rel={"noreferrer"} target={"_blank"} href={spotifyLink}>
                                    <img alt={"spotify"} id={"card-icon"} src={spotifyImg}/>
                                    <div>
                                        Go to Spotify
                                    </div>
                                </a></div>
                            </div> :
                            <div className={"card-status"}><img alt={"spotify"} id={"card-icon"} src={spotifyImg}/>
                                <div className={"not-available"}>
                                    Spotify not available
                                </div></div>}

                        {deezerStatus ?
                            <div className={"card-status"}><a rel={"noreferrer"} target={"_blank"} href={deezerLink}>
                                <img alt={"deezer"} id={"card-icon"} src={deezerImg}/>
                                <div>
                                    Go to Deezer
                                </div>
                            </a></div> :
                            <div className={"card-status"}><img alt={"deezer-icon"} id={"card-icon"} src={deezerImg}/>
                                <div className={"not-available"}>
                                    Deezer not available
                                </div></div>}

                        {localStorage.getItem("jwt") &&
                            <NavLink className={"back-nav-link"} to={`/tracks/${spotifyAlbumId}/${deezerAlbumId}`}>To Track List</NavLink>}

                    </div>
                </div>
            </div>
        </div>
    )
}