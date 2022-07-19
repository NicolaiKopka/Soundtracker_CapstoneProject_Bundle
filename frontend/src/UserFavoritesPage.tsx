import {useEffect, useState} from "react";
import {MovieItem} from "./models";
import MainPageMovieCard from "./MainPageGallery/MainPageMovieCard";
import Header from "./Header/Header";

interface AppProps {
    userMovies: MovieItem[]
    getUserMovies: Function
}

export default function UserFavoritesPage(props: AppProps) {

    const [favoritesError, setFavoritesError] = useState("")

    useEffect(() => {
        props.getUserMovies()
        if(props.userMovies.length === 0) {
            setFavoritesError("Seems like you haven't saved any movies yet")
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [])

    const userMoviesIds = props.userMovies.map(movie => movie.id)

    const components = props.userMovies.map(movie => <MainPageMovieCard movie={movie} favoriteMovieIds={userMoviesIds} getUserMovies={props.getUserMovies}/>)

    return(
        <div>
            <Header/>
            {favoritesError && <div>{favoritesError}</div>}
            {components}
        </div>
        )
}