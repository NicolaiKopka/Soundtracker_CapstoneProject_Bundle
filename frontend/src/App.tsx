import React, {useEffect, useState} from 'react';
import MainPageGallery from "./MainPageGallery/MainPageGallery";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import LoginPage from "./Register_Login/LoginPage";
import RegisterPage from "./Register_Login/RegisterPage";
import LogoutPage from "./Register_Login/LogoutPage";
import UserFavoritesPage from "./UserFavoritesPage";
import {MovieItem} from "./models";
import {getFavoriteUserMovies} from "./api_methods";
import "./App.css"

function App() {

    const [errorMessage, setErrorMessage] = useState("")
    const [userMovies, setUserMovies] = useState<Array<MovieItem>>([])

    useEffect(() => {
        setTimeout(() => setErrorMessage(""), 2000)
    }, [errorMessage])

    const getFavoriteMovies = () => {
        if(localStorage.getItem("jwt") !== null) {
            getFavoriteUserMovies().then(data => setUserMovies(data))
        }
    }


    return (
        <div className="App">
            {errorMessage && <div>{errorMessage}</div>}
            <h1>Soundtracker</h1>
            <BrowserRouter>
                <Routes>
                    <Route path={"/"} element={<MainPageGallery setErrorMessage={setErrorMessage} userMovies={userMovies} getUserMoviesFunction={getFavoriteMovies}/>} />
                    <Route path={"/login"} element={<LoginPage setErrorMessage={setErrorMessage}/>} />
                    <Route path={"/register"} element={<RegisterPage setErrorMessage={setErrorMessage} />} />
                    <Route path={"/logout"} element={<LogoutPage/>}/>
                    <Route path={"/favorites"} element={<UserFavoritesPage userMovies={userMovies} getUserMovies={getFavoriteMovies}/>}/>
                </Routes>
            </BrowserRouter>
        </div>
    );
}

export default App;
