import {FormEvent, useEffect, useState} from "react";
import {MovieItem} from "../models"
import {getFavoriteUserMovies, getStarterPageMovies, searchForMovie} from "../api_methods";
import MainPageMovieCard from "./MainPageMovieCard";
import "./MainPageGallery.css"
import Header from "../Header/Header";
import {Slide} from 'react-slideshow-image';

interface AppProps {
    setErrorMessage: Function
}

export default function MainPageGallery(props: AppProps) {

    const [movies, setMovies] = useState<Array<MovieItem>>()
    const [userMovies, setUserMovies] = useState<Array<MovieItem>>([])
    const [searchMovies, setSearchMovies] = useState<Array<MovieItem>>([])
    const [searchQuery, setSearchQuery] = useState("")
    const [favoritesError, setFavoritesError] = useState("")

    useEffect(() => {
        getFavoriteMovies()
            .catch(() => setFavoritesError(""))
        getStarterPageMovies().then((data: any) => setMovies(data))
    }, [])

    function search(ev: FormEvent) {
        ev.preventDefault()
        searchForMovie(searchQuery).then(data => setSearchMovies(data))
            .catch(() => props.setErrorMessage("No Movies Found"))
    }

    const getFavoriteMovies = () => {
        return getFavoriteUserMovies()
            .then(data => setUserMovies(data))
    }

    const userMoviesIds = userMovies.map(movie => movie.id)

    const tenMovies = movies?.slice(0, 10)

    const components = tenMovies?.map(movie => <MainPageMovieCard key={movie.id} movie={movie}
                                                                  favoriteMovieIds={userMoviesIds}
                                                                  getUserMovies={getFavoriteMovies}/>)

    const searchComponents = searchMovies?.map(movie => <MainPageMovieCard key={movie.id} movie={movie}
                                                                           favoriteMovieIds={userMoviesIds}
                                                                           getUserMovies={getFavoriteMovies}/>)

    return (
        <div className={"gallery-main"}>
            <Header/>
            {favoritesError && <div>{favoritesError}</div>}
            <div className={"input-wrapper"}>
                <h2>Movie Search</h2>
                <form className={"main-form"} onSubmit={search}>
                    <input type={"text"} placeholder={"search for movie"} value={searchQuery}
                           onChange={ev => setSearchQuery(ev.target.value)}/>
                    <button className={"send-button"} type={"submit"}>Search</button>
                </form>
            </div>
            {searchMovies.length > 0 && <Slide>{searchComponents}</Slide>}
            <h2 className={"top-movies-header"}>Top 10 Movies</h2>
            {components && components.length > 0 && <Slide>{components}</Slide>}
        </div>
    )

}
