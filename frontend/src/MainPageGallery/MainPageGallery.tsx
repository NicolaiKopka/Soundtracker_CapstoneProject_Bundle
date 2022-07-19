import {FormEvent, useEffect, useState} from "react";
import {MovieItem} from "../models"
import {getStarterPageMovies, searchForMovie} from "../api_methods";
import MainPageMovieCard from "./MainPageMovieCard";
import "./MainPageGallery.css"
import Header from "../Header/Header";

interface AppProps {
    setErrorMessage: Function
    userMovies: MovieItem[]
    getUserMoviesFunction: Function
}

export default function MainPageGallery(props: AppProps) {

    const [movies, setMovies] = useState<Array<MovieItem>>()
    const [searchMovies, setSearchMovies] = useState<Array<MovieItem>>([])
    const [searchQuery, setSearchQuery] = useState("")


    useEffect(() => {
        props.getUserMoviesFunction()
        getStarterPageMovies().then((data: any) => setMovies(data))
        // eslint-disable-next-line react-hooks/exhaustive-deps
        }, [])

    function search(ev: FormEvent) {
        ev.preventDefault()
        searchForMovie(searchQuery).then(data => setSearchMovies(data))
            .catch(() => props.setErrorMessage("No Movies Found"))
    }

    const userMoviesIds = props.userMovies.map(movie => movie.id)

    const tenMovies = movies?.slice(0, 10)

    const components = tenMovies?.map(movie => <MainPageMovieCard key={movie.id} movie={movie} favoriteMovieIds={userMoviesIds} getUserMovies={props.getUserMoviesFunction}/>)

    const searchComponents = searchMovies?.map(movie => <MainPageMovieCard key={movie.id} movie={movie} favoriteMovieIds={userMoviesIds} getUserMovies={props.getUserMoviesFunction}/>)

    return (
        <div>
            <Header />
            <h2>Movie Search</h2>
            <div>
                <form onSubmit={search}>
                    <input type={"text"} placeholder={"search for movie"} value={searchQuery} onChange={ev => setSearchQuery(ev.target.value)}/>
                    <button type={"submit"}>send</button>
                </form>
            </div>
            {searchMovies && <div className={"gallery"}>{searchComponents}</div>}
            <h2>Top 10 Movies</h2>
            <div className={"gallery"}>
                {components}
            </div>
        </div>
    )

}
