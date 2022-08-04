import React, {useEffect, useState} from 'react';
import MainPageGallery from "./MainPageGallery/MainPageGallery";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import LoginPage from "./Register_Login/LoginPage";
import RegisterPage from "./Register_Login/RegisterPage";
import LogoutPage from "./Register_Login/LogoutPage";
import UserFavoritesPage from "./UserFavoritesPage";
import "./App.css"
import SpotifyLoginRedirect from "./Register_Login/streamingLogin/SpotifyLoginRedirect";
import SpotifyPlaylistPage from "./SpotifyPlaylistComponents/SpotifyPlaylistPage";
import TrackList from "./Tracklist_Player/TrackList";
import MyPlaylistsPage from "./PlaylistComponents/MyPlaylistsPage";
import {Toaster} from "react-hot-toast";
import DeezerLoginRedirect from "./Register_Login/streamingLogin/DeezerLoginRedirect";

function App() {

    const [errorMessage, setErrorMessage] = useState("")

    useEffect(() => {
        setTimeout(() => setErrorMessage(""), 2000)

    }, [errorMessage])

    return (
        <div className="App">
            <Toaster/>
            {errorMessage && <div>{errorMessage}</div>}
            <h1>Soundtracker</h1>
            <BrowserRouter>
                <Routes>
                    <Route path={"/"} element={<MainPageGallery setErrorMessage={setErrorMessage}/>} />
                    <Route path={"/login"} element={<LoginPage setErrorMessage={setErrorMessage}/>} />
                    <Route path={"/register"} element={<RegisterPage setErrorMessage={setErrorMessage} />} />
                    <Route path={"/logout"} element={<LogoutPage/>}/>
                    <Route path={"/favorites"} element={<UserFavoritesPage/>}/>
                    <Route path={"/spotify-redirect"} element={<SpotifyLoginRedirect/>} />
                    <Route path={"/deezer-redirect"} element={<DeezerLoginRedirect/>} />
                    <Route path={"/my-playlists"} element={<MyPlaylistsPage/>} />
                    <Route path={"/spotify-playlists"} element={<SpotifyPlaylistPage/>} />
                    <Route path={"/tracks/:spotifyId/:deezerId"} element={<TrackList/>} />
                </Routes>
            </BrowserRouter>
        </div>
    );
}

export default App;
