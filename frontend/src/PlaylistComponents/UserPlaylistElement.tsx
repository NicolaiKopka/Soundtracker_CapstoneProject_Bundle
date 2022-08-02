import {useState} from "react";
import {SpotifyTrackDTO, UserPlaylistMap} from "../models";
import {createSpotifyPlaylistAndAddTracks, deleteUserPlaylist, getAllSpotifyTracksInPlaylist} from "../api_methods";
import "./UserPlaylistElement.css"
import MyPlaylistTrackElement from "./MyPlaylistTrackElement";
import toast from "react-hot-toast";

interface MyPlaylistPageProps {
    userPlaylists: UserPlaylistMap
    playlistKey: string
    refreshPlaylists: () => void
    editMode: boolean
    spotifyMode: boolean
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
            .then(() => toast.success("Playlist deleted"))
            .catch(error => error.response.data)
    }

    function sendPlaylistToSpotify() {
        createSpotifyPlaylistAndAddTracks(props.playlistKey).catch()
    }

    const allPlaylistTrackElements = allPlaylistTracks?.map(track => <MyPlaylistTrackElement track={track}/>)

    return (
        <div>
            <button className={"playlist-button"} onClick={switchShowStatus}>{props.playlistKey}<i
                className="fa-solid fa-arrow-down spacer-left"></i></button>
            {props.editMode &&
                <button className={"playlist-button delete-button"} onClick={deletePlaylist}><i className="fa-solid fa-trash-can spacer"></i>Delete Playlist</button>}
            {props.spotifyMode &&
            <button onClick={sendPlaylistToSpotify}>Send to Spotify</button>}
            {showStatus &&
                <div>
                    {allPlaylistTrackElements}
                    {/*{props.userPlaylists[props.playlistKey].spotifyTrackIds.map(track => <div>{track}</div>)}*/}
                </div>}
        </div>
    )
}