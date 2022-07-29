import {NavLink} from "react-router-dom";
import {useCallback, useEffect, useState} from "react";
import {getAllUserPlaylists} from "../api_methods";
import UserPlaylistElement from "./UserPlaylistElement";

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
            My Playlists
            {playlists.map(key => <UserPlaylistElement refreshPlaylists={refreshPlaylists} userPlaylists={userPlaylists} playlistKey={key}/>)}

            {localStorage.getItem("spotify_jwt") &&
            <NavLink to={"/spotify-playlists"}><button>Show My Spotify Playlists</button></NavLink>}
        </div>
    )
}