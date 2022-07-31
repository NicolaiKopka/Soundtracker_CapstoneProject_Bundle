import {useEffect, useState} from "react";
import {SpotifyUserPlaylists} from "../models";
import {addSpotifyPlaylist, getAllUserSpotifyPlaylists} from "../api_methods";
import SpotifyPlaylistComponent from "./SpotifyPlaylistComponent";
import "./SpotifyPlaylistPage.css"


export default function SpotifyPlaylistPage() {

    const [newPlaylistName, setNewPlaylistName] = useState("")
    const [newPlaylistDescription, setNewPlaylistDescription] = useState("")
    const [newPlaylistIsPublicStatus, setNewPlaylistIsPublicStatus] = useState(false)
    const [publicStatusDisabled, setPublicStatusDisabled] = useState(false)
    const [newPlaylistIsCollaborativeStatus,setNewPlaylistIsCollaborativeStatus] = useState(false)
    const [allPlaylistsObject, setAllPlaylistsObject] = useState<SpotifyUserPlaylists>()

    useEffect(() => {
        updateUserPlaylists()
    }, [])

    useEffect(() => {
        if(newPlaylistIsCollaborativeStatus) {
            setNewPlaylistIsPublicStatus(false)
            setPublicStatusDisabled(true)
        } else {
            setNewPlaylistIsPublicStatus(false)
            setPublicStatusDisabled(false)
        }
    }, [newPlaylistIsCollaborativeStatus])

    function addPlaylist() {
        addSpotifyPlaylist(newPlaylistName, newPlaylistDescription, newPlaylistIsPublicStatus, newPlaylistIsCollaborativeStatus)
            .then(updateUserPlaylists)
            .catch()
    }

    function updateUserPlaylists() {
        getAllUserSpotifyPlaylists()
            .then(data => setAllPlaylistsObject(data))
    }

    const playlists = allPlaylistsObject?.items.map(playlist => <SpotifyPlaylistComponent key={playlist.name} playlist={playlist}/>)

    return (
        <div className={"playlist-form-wrapper"}>
            <form className={"playlist-form-wrapper"} onSubmit={addPlaylist}>
                <input required={true} placeholder={"name"} value={newPlaylistName} onChange={ev => setNewPlaylistName(ev.target.value)}/>
                <input placeholder={"description"} value={newPlaylistDescription} onChange={ev => setNewPlaylistDescription(ev.target.value)}/>
                {newPlaylistName !== "" &&
                    <div>
                        <label>Public: </label>
                        <input id={"publicStatus"} type={"checkbox"} checked={newPlaylistIsPublicStatus} disabled={publicStatusDisabled} onChange={ev => setNewPlaylistIsPublicStatus(ev.target.checked)}/>
                        <label>Collaborative: </label>
                        <input type={"checkbox"} checked={newPlaylistIsCollaborativeStatus} onChange={ev => setNewPlaylistIsCollaborativeStatus(ev.target.checked)}/>
                    </div>
                }
                <button type={"submit"}>Add New Playlist</button>
            </form>
            <div>
                {playlists}
            </div>
        </div>
    )
}