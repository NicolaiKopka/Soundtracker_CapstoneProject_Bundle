import {useState} from "react";
import {SpotifyTrackDTO, UserPlaylistMap} from "../models";
import {deleteUserPlaylist, getAllSpotifyTracksInPlaylist} from "../api_methods";
import "./UserPlaylistElement.css"
import MyPlaylistTrackElement from "./MyPlaylistTrackElement";

interface MyPlaylistPageProps {
    userPlaylists: UserPlaylistMap
    playlistKey: string
    refreshPlaylists: () => void
}

export default function UserPlaylistElement(props: MyPlaylistPageProps) {

    const [showStatus, setShowStatus] = useState(false)
    const [allPlaylistTracks, setAllPlaylistTracks] = useState<Array<SpotifyTrackDTO>>()

    function switchShowStatus() {
        if(showStatus) {
            setShowStatus(false)
        }else{
            setShowStatus(true)
            getAllSpotifyTracksInPlaylist(props.playlistKey)
                .then(data => setAllPlaylistTracks(data))
                .then()
                .catch()
        }
    }

    function deletePlaylist() {
        deleteUserPlaylist(props.playlistKey)
            .then(() => props.refreshPlaylists())
            .catch()
    }

    const allPlaylistTrackElements = allPlaylistTracks?.map(track => <MyPlaylistTrackElement track={track}/>)

    return (
        <div>
            <button className={"playlist-button"} onClick={switchShowStatus}>{props.playlistKey}<i
                className="fa-solid fa-arrow-down spacer-left"></i></button>
            <button className={"playlist-button delete-button"} onClick={deletePlaylist}><i className="fa-solid fa-trash-can spacer"></i>Delete Playlist</button>
            {showStatus &&
                <div>
                    {allPlaylistTrackElements}
                    {/*{props.userPlaylists[props.playlistKey].spotifyTrackIds.map(track => <div>{track}</div>)}*/}
                </div>}
        </div>
    )
}