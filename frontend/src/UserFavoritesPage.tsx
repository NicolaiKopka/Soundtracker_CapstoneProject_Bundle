import {useEffect, useState} from "react";
import {MovieItem} from "./models";
import MainPageMovieCard from "./MainPageGallery/MainPageMovieCard";
import Header from "./Header/Header";
import {getFavoriteUserMovies} from "./api_methods";


export default function UserFavoritesPage() {

    const [favoritesError, setFavoritesError] = useState("")
    const [userMovies, setUserMovies] = useState<Array<MovieItem>>([])

    useEffect(() => {
        getFavoriteMovies()
            .catch(() => setFavoritesError("Seems like you are not logged in")) //nav to login
    }, [])

    const getFavoriteMovies = () => {
        return getFavoriteUserMovies()
            .then(data => setUserMovies(data))
    }

    const userMoviesIds = userMovies.map(movie => movie.id)

    const components = userMovies.map(movie => <MainPageMovieCard key={movie.id} movie={movie} favoriteMovieIds={userMoviesIds} getUserMovies={getFavoriteMovies}/>)

    return(
        <div>
            <Header/>
            {favoritesError && <div>{favoritesError}</div>}
            {components}
        </div>
        )
}