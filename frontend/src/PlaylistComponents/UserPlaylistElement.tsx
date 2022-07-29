import {useState} from "react";
import {UserPlaylistMap} from "../models";
import {deleteUserPlaylist} from "../api_methods";

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
            <button onClick={switchShowStatus}>{props.playlistKey}</button>
            <button onClick={deletePlaylist}>Delete Playlist</button>
            {showStatus &&
                <div>
                    {props.userPlaylists[props.playlistKey].spotifyTrackIds.map(track => <div>{track}</div>)}
                </div>}
        </div>
    )
}