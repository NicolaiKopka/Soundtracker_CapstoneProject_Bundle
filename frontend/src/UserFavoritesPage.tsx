import {useEffect, useState} from "react";
import {getFavoriteUserMovies} from "./api_methods";
import {MovieItem} from "./models";
import MainPageMovieCard from "./MainPageGallery/MainPageMovieCard";
import Header from "./Header/Header";


export default function UserFavoritesPage() {

    const [favoritesError, setFavoritesError] = useState("")

    const [movies, setMovies] = useState<Array<MovieItem>>()

    useEffect(() => {
        getFavoriteUserMovies().then(data => setMovies(data))
            .catch(() => setFavoritesError("Seems like you haven't saved any movies yet"))
    }, [])

    const components = movies?.map(movie => <MainPageMovieCard movie={movie}/>)

    return(
        <div>
            <Header/>
            {favoritesError && <div>{favoritesError}</div>}
            {components}
        </div>
        )

}