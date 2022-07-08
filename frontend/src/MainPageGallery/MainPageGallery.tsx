import {useEffect, useState} from "react";
import {MovieItem} from "../models"
import {getStarterPageMovies} from "../api_methods";
import MainPageMovieCard from "./MainPageMovieCard";



export default function MainPageGallery() {

    const [movies, setMovies] = useState<Array<MovieItem>>()

    useEffect(() => {
        getStarterPageMovies().then((data: any) => setMovies(data))
        }, [])

    const tenMovies = movies?.slice(0, 10)

    const components = tenMovies?.map(movie => <MainPageMovieCard movie={movie}/>)

    console.log(components)

    return (
        <div>
            {components}
            <br/>
        </div>
    )

}
