import {useEffect, useState} from "react";
import {MovieItem} from "../models"
import {getStarterPageMovies, searchForMovie} from "../api_methods";
import MainPageMovieCard from "./MainPageMovieCard";
import "./MainPageGallery.css"



export default function MainPageGallery() {

    const [movies, setMovies] = useState<Array<MovieItem>>()
    const [searchMovies, setSearchMovies] = useState<Array<MovieItem>>([])
    const [searchQuery, setSearchQuery] = useState("")

    useEffect(() => {
        getStarterPageMovies().then((data: any) => setMovies(data))
        }, [])

    function search() {
        searchForMovie(searchQuery).then(data => setSearchMovies(data))
    }

    const tenMovies = movies?.slice(0, 10)

    const components = tenMovies?.map(movie => <MainPageMovieCard key={movie.id} movie={movie}/>)
    const searchComponents = searchMovies?.map(movie => <MainPageMovieCard key={movie.id} movie={movie}/>)

    console.log(components)

    return (
        <div >
            <h2>Movie Search</h2>
            <div>
                <input type={"text"} placeholder={"search for movie"} value={searchQuery} onChange={ev => setSearchQuery(ev.target.value)}/>
                <button onClick={search}>send</button>
            </div>
            {searchMovies && <div className={"gallery"}>{searchComponents}</div>}
            <h2>Top 10 Movies</h2>
            <div className={"gallery"}>
                {components}
            </div>
        </div>
    )

}
