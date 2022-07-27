import {NavLink} from "react-router-dom";
import {useEffect, useState} from "react";
import {getAllUserPlaylists} from "../api_methods";
import {UserPlaylist} from "../models";

export default function MyPlaylistsPage() {
    const [userPlaylists, setUserPlaylists] = useState({})

    useEffect(() => {
        getAllUserPlaylists().then(data => setUserPlaylists(data.userPlaylists))
    }, [])

    const playlists = Object.keys(userPlaylists)

    return (
        <div>
            My Playlists
            <select>
                {playlists.map(key => <option value={key}>{key}</option>)}
            </select>

            {localStorage.getItem("spotify_jwt") &&
            <NavLink to={"/spotify-playlists"}><button>Show My Spotify Playlists</button></NavLink>}
        </div>
    )
}