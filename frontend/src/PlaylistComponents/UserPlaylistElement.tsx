import {useState} from "react";
import {UserPlaylistMap} from "../models";
import {deleteUserPlaylist} from "../api_methods";
import "./UserPlaylistElement.css"

interface MyPlaylistPageProps {
    userPlaylists: UserPlaylistMap
    playlistKey: string
    refreshPlaylists: () => void
}

export default function UserPlaylistElement(props: MyPlaylistPageProps) {

    const [showStatus, setShowStatus] = useState(false)

    function switchShowStatus() {
        if(showStatus) {
            setShowStatus(false)
        }else{
            setShowStatus(true)
        }
    }

    function deletePlaylist() {
        deleteUserPlaylist(props.playlistKey)
            .then(() => props.refreshPlaylists())
            .catch()
    }

    return (
        <div>
            <button className={"playlist-button"} onClick={switchShowStatus}>{props.playlistKey}<i
                className="fa-solid fa-arrow-down spacer-left"></i></button>
            <button className={"playlist-button delete-button"} onClick={deletePlaylist}><i className="fa-solid fa-trash-can spacer"></i>Delete Playlist</button>
            {showStatus &&
                <div>
                    {props.userPlaylists[props.playlistKey].spotifyTrackIds.map(track => <div>{track}</div>)}
                </div>}
        </div>
    )
}