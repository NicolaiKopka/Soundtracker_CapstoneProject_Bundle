import {useEffect, useState} from "react";
import {MovieItem} from "../models"
import {getStarterPageMovies} from "../api_methods";
import MainPageMovieCard from "./MainPageMovieCard";
import "./MainPageGallery.css"



export default function MainPageGallery() {

    const [movies, setMovies] = useState<Array<MovieItem>>()

    useEffect(() => {
        getStarterPageMovies().then((data: any) => setMovies(data))
        }, [])

    const tenMovies = movies?.slice(0, 10)

    const components = tenMovies?.map(movie => <MainPageMovieCard key={movie.id} movie={movie}/>)

    console.log(components)

    return (
        <div className={"gallery"}>
            {components}
            <br/>
        </div>
    )

}
