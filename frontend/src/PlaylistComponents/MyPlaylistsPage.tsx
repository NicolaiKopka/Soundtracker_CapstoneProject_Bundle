import {NavLink} from "react-router-dom";
import {useCallback, useEffect, useState} from "react";
import {getAllUserPlaylists} from "../api_methods";
import UserPlaylistElement from "./UserPlaylistElement";
import Header from "../Header/Header";
import "./MyPlaylistPage.css"
import {Switch} from "@mui/material";
import {UserFavoritesDTO, UserPlaylistMap} from "../models";

export default function MyPlaylistsPage() {
    const [userPlaylists, setUserPlaylists] = useState<UserPlaylistMap>({})
    const [editMode, setEditMode] = useState(false)
    const [spotifyMode, setSpotifyMode] = useState(false)
    const [newPlaylistName, setNewPlaylistName] = useState("")
    const [newPlaylistDescription, setNewPlaylistDescription] = useState("")
    const [newPlaylistIsPublicStatus, setNewPlaylistIsPublicStatus] = useState(false)
    const [publicStatusDisabled, setPublicStatusDisabled] = useState(false)
    const [newPlaylistIsCollaborativeStatus, setNewPlaylistIsCollaborativeStatus] = useState(false)
    const [currentProvider, setCurrentProvider] = useState("spotify")

    const refreshPlaylists = useCallback(() => {
        getAllUserPlaylists().then(data => setUserPlaylists(data.userPlaylists))
    }, [])

    useEffect(() => {
        if (newPlaylistIsCollaborativeStatus) {
            setNewPlaylistIsPublicStatus(false)
            setPublicStatusDisabled(true)
        } else {
            setNewPlaylistIsPublicStatus(false)
            setPublicStatusDisabled(false)
        }
    }, [newPlaylistIsCollaborativeStatus])

    useEffect(() => {
        refreshPlaylists()
    }, [refreshPlaylists])

    function toggleEditMode() {
        if (editMode) {
            setEditMode(false)
        } else {
            setEditMode(true)
        }
    }

    function toggleSpotifyMode() {
        if (spotifyMode) {
            setSpotifyMode(false)
        } else {
            setSpotifyMode(true)
            setEditMode(false)
        }
    }

    const playlists = Object.keys(userPlaylists)

    return (
        <div>
            <Header/>
            <h2>My Playlists</h2>
            {!spotifyMode && <span>
                <span>Edit: </span>
                <Switch name={"Edit Mode"} onChange={toggleEditMode} checked={editMode}/></span>}

            {(localStorage.getItem("spotify_jwt") || localStorage.getItem("deezer_jwt")) && <>
                <span>Export: </span>
                <Switch name={"SpotifyMode"} onChange={toggleSpotifyMode} checked={spotifyMode}/>
            </>
            }
            <div className={"playlist-form-wrapper"}>
                {spotifyMode && <>
                    <div className="radio-container">
                        <input type="radio" name="radio" disabled={localStorage.getItem("spotify_jwt") === null} onChange={ev => setCurrentProvider(ev.target.value)} value={"spotify"} checked={currentProvider === "spotify"}/>
                        <span>Spotify</span>
                        <input type="radio" name="radio" disabled={localStorage.getItem("deezer_jwt") === null} onChange={ev => setCurrentProvider(ev.target.value)} value={"deezer"} checked={currentProvider === "deezer"}/>
                        <span>Deezer</span>
                    </div>
                    <div>
                        {localStorage.getItem("spotify_jwt") ? <p>Spotify key available</p>:
                            <a>Get your spotify key</a>}
                        {localStorage.getItem("deezer_jwt") ? <p>Deezer key available</p>: <a>Get your deezer key</a>}
                    </div>
                <div className={"playlist-form-wrapper"}>
                    <input required={true} placeholder={"name"}  value={newPlaylistName}
                           onChange={ev => setNewPlaylistName(ev.target.value)}/>
                    <input placeholder={"description"} value={newPlaylistDescription}
                           onChange={ev => setNewPlaylistDescription(ev.target.value)}/>
                    {newPlaylistName !== "" &&
                        <div>
                            <label>Public: </label>
                            <input id={"publicStatus"} type={"checkbox"} checked={newPlaylistIsPublicStatus}
                                   disabled={publicStatusDisabled}
                                   onChange={ev => setNewPlaylistIsPublicStatus(ev.target.checked)}/>
                            <label>Collaborative: </label>
                            <input type={"checkbox"} checked={newPlaylistIsCollaborativeStatus}
                                   onChange={ev => setNewPlaylistIsCollaborativeStatus(ev.target.checked)}/>
                        </div>
                    }
                </div> </>}
                <div className={"playlist-wrapper"}>
                    {playlists.map(key => <UserPlaylistElement key={key} spotifyMode={spotifyMode} editMode={editMode}
                                                               refreshPlaylists={refreshPlaylists} playlistDescription={newPlaylistDescription}
                                                               playlistKey={key} isCollaborative={newPlaylistIsCollaborativeStatus}
                                                               isPublic={newPlaylistIsPublicStatus} setPlaylistDescription={setNewPlaylistDescription}
                                                               setPlaylistIsPublic={setNewPlaylistIsPublicStatus} setPlaylistCollab={setNewPlaylistIsCollaborativeStatus}
                                                               setPlaylistName={setNewPlaylistName} playlistName={newPlaylistName} currentProvider={currentProvider}
                                                               playlistMap={userPlaylists}/>)}
                </div>
                {localStorage.getItem("spotify_jwt") &&
                    <NavLink to={"/spotify-playlists"}>
                        <button>Show My Spotify Playlists</button>
                    </NavLink>}
            </div>
        </div>
    )
}