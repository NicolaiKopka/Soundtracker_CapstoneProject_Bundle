import {NavLink} from "react-router-dom";
import {useCallback, useEffect, useState} from "react";
import {getAllUserPlaylists} from "../api_methods";
import UserPlaylistElement from "./UserPlaylistElement";
import Header from "../Header/Header";
import "./MyPlaylistPage.css"
import {Switch} from "@mui/material";
import {UserPlaylistMap} from "../models";
import spotifyImg from "../images/Spotify_Icon_RGB_Green.png";
import deezerImg from "../images/Colored_Equalizer@2x.png";

export default function MyPlaylistsPage() {
    const [userPlaylists, setUserPlaylists] = useState<UserPlaylistMap>({})
    const [editMode, setEditMode] = useState(false)
    const [streamingMode, setStreamingMode] = useState(false)
    const [newPlaylistName, setNewPlaylistName] = useState("")
    const [newPlaylistDescription, setNewPlaylistDescription] = useState("")
    const [newPlaylistIsPublicStatus, setNewPlaylistIsPublicStatus] = useState(false)
    const [publicStatusDisabled, setPublicStatusDisabled] = useState(false)
    const [newPlaylistIsCollaborativeStatus, setNewPlaylistIsCollaborativeStatus] = useState(false)
    const [currentProvider, setCurrentProvider] = useState("")

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
        if(localStorage.getItem("spotify_jwt") !== null) {
            setCurrentProvider("spotify")
        }
        if(localStorage.getItem("deezer_jwt") !== null) {
            setCurrentProvider("deezer")
        }
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
        if (streamingMode) {
            setStreamingMode(false)
        } else {
            setStreamingMode(true)
            setEditMode(false)
        }
    }

    function throwAlert() {
        alert("Spotify is currently in dev mode for this app. In order to login, your spotify email will have to be deposited in the current project. Contact for more information. If your mail is already deposited you can login.")
    }

    const playlists = Object.keys(userPlaylists)

    return (
        <div>
            <Header/>

            <div className={"toggle-menu"}>
                {!streamingMode && <><span>
                <span>Edit: </span>
                <Switch name={"Edit Mode"} onChange={toggleEditMode} checked={editMode}/></span>
                </>
                }
                <span>Export: </span>
                <Switch name={"SpotifyMode"} onChange={toggleSpotifyMode} checked={streamingMode}/>
            </div>
            <div className={"playlist-form-wrapper"}>
                {streamingMode && <>
                    <table className={"streaming-table"}>
                        <tr className={"provider-section"}>
                            <td>
                                <div className={"spotify-div"}>
                                    <input className={"radio"} type="radio" name="radio"
                                           disabled={localStorage.getItem("spotify_jwt") === null}
                                           onChange={ev => setCurrentProvider(ev.target.value)} value={"spotify"}
                                           checked={currentProvider === "spotify"}/>
                                    <img src={spotifyImg}/>
                                </div>
                            </td>
                            <td>
                                <div className={"spotify-div"}>
                                    <input className={"radio"} type="radio" name="radio"
                                           disabled={localStorage.getItem("deezer_jwt") === null}
                                           onChange={ev => setCurrentProvider(ev.target.value)} value={"deezer"}
                                           checked={currentProvider === "deezer"}/>
                                    <img id={"deezer-img"} src={deezerImg}/>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                {localStorage.getItem("spotify_jwt") ? <span className={"available"}>Spotify key available</span> :
                                    <a onClick={throwAlert}
                                       href={`https://accounts.spotify.com/authorize?response_type=code&client_id=${process.env.REACT_APP_SPOTIFY_CLIENT_ID}&scope=streaming user-read-playback-state user-read-private user-modify-playback-state user-read-email playlist-read-private playlist-read-collaborative playlist-modify-public playlist-modify-private&redirect_uri=${process.env.REACT_APP_SPOTIFY_CALLBACK_URI}`}>Get
                                        your Spotify key</a>}

                            </td>
                            <td>
                                {localStorage.getItem("deezer_jwt") ? <span className={"available"}>Deezer key available</span> :
                                    <a href={`https://connect.deezer.com/oauth/auth.php?app_id=${process.env.REACT_APP_DEEZER_CLIENT_ID}&redirect_uri=${process.env.REACT_APP_DEEZER_CALLBACK_URI}&perms=basic_access,email,manage_library`}>Get
                                        your Deezer key</a>}

                            </td>
                        </tr>
                    </table>
                </>}
                {((localStorage.getItem("spotify_jwt") !== null || localStorage.getItem("deezer_jwt") !== null)) && streamingMode &&
                    <div className={"playlist-inputs"}>
                        <input placeholder={`${currentProvider} playlist name`} value={newPlaylistName}
                               onChange={ev => setNewPlaylistName(ev.target.value)}/>
                        {currentProvider === "spotify" && <>
                            <input placeholder={`${currentProvider} playlist description`} value={newPlaylistDescription}
                                   onChange={ev => setNewPlaylistDescription(ev.target.value)}/>
                            {newPlaylistName !== "" &&
                                <div className={"spotify-radios"}>
                                    <label>Public: </label>
                                    <input id={"publicStatus"} type={"checkbox"} checked={newPlaylistIsPublicStatus}
                                           disabled={publicStatusDisabled}
                                           onChange={ev => setNewPlaylistIsPublicStatus(ev.target.checked)}/>
                                    <label>Collaborative: </label>
                                    <input type={"checkbox"} checked={newPlaylistIsCollaborativeStatus}
                                           onChange={ev => setNewPlaylistIsCollaborativeStatus(ev.target.checked)}/>
                                </div>}
                        </>}
                    </div>}
                <h2 className={"playlist-heading"}>My Playlists</h2>
                <div className={"playlist-wrapper"}>
                    {playlists.map(key => <UserPlaylistElement key={key} spotifyMode={streamingMode} editMode={editMode}
                                                               refreshPlaylists={refreshPlaylists}
                                                               playlistDescription={newPlaylistDescription}
                                                               playlistKey={key}
                                                               isCollaborative={newPlaylistIsCollaborativeStatus}
                                                               isPublic={newPlaylistIsPublicStatus}
                                                               setPlaylistDescription={setNewPlaylistDescription}
                                                               setPlaylistIsPublic={setNewPlaylistIsPublicStatus}
                                                               setPlaylistCollab={setNewPlaylistIsCollaborativeStatus}
                                                               setPlaylistName={setNewPlaylistName}
                                                               playlistName={newPlaylistName}
                                                               currentProvider={currentProvider}
                                                               playlistMap={userPlaylists}/>)}
                </div>
            </div>
        </div>
    )
}