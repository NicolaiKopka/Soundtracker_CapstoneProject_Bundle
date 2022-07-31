import {NavLink} from "react-router-dom";
import {useCallback, useEffect, useState} from "react";
import {getAllUserPlaylists} from "../api_methods";
import UserPlaylistElement from "./UserPlaylistElement";
import Header from "../Header/Header";
import "./MyPlaylistPage.css"

export default function MyPlaylistsPage() {
    const [userPlaylists, setUserPlaylists] = useState({})

    const refreshPlaylists = useCallback(() => {
        getAllUserPlaylists().then(data => setUserPlaylists(data.userPlaylists))
    }, [])

    useEffect(() => {
        refreshPlaylists()
    }, [refreshPlaylists])

    const playlists = Object.keys(userPlaylists)

    return (
        <div>
            <Header/>
            <h2>My Playlists</h2>
            <div className={"playlist-wrapper"}>
                {playlists.map(key => <UserPlaylistElement refreshPlaylists={refreshPlaylists} userPlaylists={userPlaylists} playlistKey={key}/>)}
            </div>

            {localStorage.getItem("spotify_jwt") &&
            <NavLink to={"/spotify-playlists"}><button>Show My Spotify Playlists</button></NavLink>}
        </div>
    )
}