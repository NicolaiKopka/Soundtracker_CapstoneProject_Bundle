import {NavLink} from "react-router-dom";
import {useCallback, useEffect, useState} from "react";
import {getAllUserPlaylists} from "../api_methods";
import UserPlaylistElement from "./UserPlaylistElement";
import Header from "../Header/Header";
import "./MyPlaylistPage.css"
import {Switch} from "@mui/material";

export default function MyPlaylistsPage() {
    const [userPlaylists, setUserPlaylists] = useState({})
    const [editMode, setEditMode] = useState(false)
    const [spotifyMode, setSpotifyMode] = useState(false)

    const refreshPlaylists = useCallback(() => {
        getAllUserPlaylists().then(data => setUserPlaylists(data.userPlaylists))
    }, [])

    useEffect(() => {
        refreshPlaylists()
    }, [refreshPlaylists])

    function toggleEditMode() {
        if(editMode) {
            setEditMode(false)
        }else {
            setEditMode(true)
        }
    }

    function toggleSpotifyMode() {
        if(spotifyMode) {
            setSpotifyMode(false)
        }else {
            setSpotifyMode(true)
        }
    }

    const playlists = Object.keys(userPlaylists)

    return (
        <div>
            <Header/>
            <h2>My Playlists</h2>
            <span>Edit Mode:</span>
            <Switch name={"Edit Mode"} onChange={toggleEditMode} checked={editMode}/>
            {localStorage.getItem("spotify_jwt") && <>
                <span>Spotify Mode:</span>
                <Switch name={"SpotifyMode"} onChange={toggleSpotifyMode} checked={spotifyMode}/>
            </>
            }

            <div className={"playlist-wrapper"}>
                {playlists.map(key => <UserPlaylistElement key={key} spotifyMode={spotifyMode} editMode={editMode} refreshPlaylists={refreshPlaylists} userPlaylists={userPlaylists} playlistKey={key}/>)}
            </div>

            {localStorage.getItem("spotify_jwt") &&
            <NavLink to={"/spotify-playlists"}><button>Show My Spotify Playlists</button></NavLink>}
        </div>
    )
}