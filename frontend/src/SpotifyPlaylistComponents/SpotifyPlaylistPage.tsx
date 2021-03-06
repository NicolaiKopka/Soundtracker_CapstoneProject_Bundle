import {useEffect, useState} from "react";
import {SpotifyUserPlaylists} from "../models";
import {addSpotifyPlaylist, getAllUserPlaylists} from "../api_methods";
import SpotifyPlaylistComponent from "./SpotifyPlaylistComponent";


export default function SpotifyPlaylistPage() {

    const [newPlaylistName, setNewPlaylistName] = useState("")
    const [newPlaylistDescription, setNewPlaylistDescription] = useState("")
    const [newPlaylistIsPublicStatus, setNewPlaylistIsPublicStatus] = useState(false)
    const [publicStatusDisabled, setPublicStatusDisabled] = useState(false)
    const [newPlaylistIsCollaborativeStatus,setNewPlaylistIsCollaborativeStatus] = useState(false)
    const [allPlaylistsObject, setAllPlaylistsObject] = useState<SpotifyUserPlaylists>()

    useEffect(() => {
        getAllUserPlaylists()
            .then(data => setAllPlaylistsObject(data))
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
            .catch()
    }

    const playlists = allPlaylistsObject?.items.map(playlist => <SpotifyPlaylistComponent key={playlist.name} playlist={playlist}/>)

    return (
        <div>
            <form onSubmit={addPlaylist}>
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