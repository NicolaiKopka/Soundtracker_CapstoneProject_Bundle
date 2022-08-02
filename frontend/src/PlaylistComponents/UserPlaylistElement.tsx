import {useState} from "react";
import {SpotifyTrackDTO} from "../models";
import {
    addSpotifyPlaylist,
    addTracksToSpotifyPlaylist,
    deleteUserPlaylist,
    getAllSpotifyTracksInPlaylist
} from "../api_methods";
import "./UserPlaylistElement.css"
import MyPlaylistTrackElement from "./MyPlaylistTrackElement";
import toast from "react-hot-toast";

interface MyPlaylistPageProps {
    playlistKey: string
    refreshPlaylists: () => void
    editMode: boolean
    spotifyMode: boolean
    playlistDescription: string
    playlistName: string
    isPublic: boolean
    isCollaborative: boolean
    setPlaylistName: Function
    setPlaylistDescription: Function
    setPlaylistIsPublic: Function
    setPlaylistCollab: Function
}

export default function UserPlaylistElement(props: MyPlaylistPageProps) {

    const [showStatus, setShowStatus] = useState(false)
    const [allPlaylistTracks, setAllPlaylistTracks] = useState<Array<SpotifyTrackDTO>>()

    function switchShowStatus() {
        if(showStatus) {
            setShowStatus(false)
        }else{
            setShowStatus(true)
            getAllPlaylistTracks()
        }
    }

    function getAllPlaylistTracks() {
        getAllSpotifyTracksInPlaylist(props.playlistKey)
            .then(data => setAllPlaylistTracks(data))
            .catch(error => error.response.data)
    }

    function deletePlaylist() {
        deleteUserPlaylist(props.playlistKey)
            .then(() => props.refreshPlaylists())
            .then(() => toast.success("Playlist deleted"))
            .catch(error => error.response.data)
    }

    function sendPlaylistToSpotify() {
        addSpotifyPlaylist(props.playlistName, props.playlistDescription, props.isPublic, props.isCollaborative)
            .then(data => {
                return data
            }).then(data => {
                addTracksToSpotifyPlaylist(data.id, props.playlistKey)
                    .then(() => {
                        toast.success("Sent playlist to spotify")
                        props.setPlaylistName("")
                        props.setPlaylistDescription("")
                        props.setPlaylistIsPublic(false)
                        props.setPlaylistCollab(false)
                    })
                    .catch()
            }).catch(error => toast.error(error.response.data))
    }

    const allPlaylistTrackElements = allPlaylistTracks?.map(track => <MyPlaylistTrackElement refreshTracks={getAllPlaylistTracks} playlistKey={props.playlistKey} editMode={props.editMode} track={track}/>)


    return (
        <div>
            <button className={"playlist-button"} onClick={switchShowStatus}>{props.playlistKey}<i
                className="fa-solid fa-arrow-down spacer-left"></i></button>
            {props.editMode &&
                <button className={"playlist-button delete-button"} onClick={deletePlaylist}><i className="fa-solid fa-trash-can spacer"></i>Delete Playlist</button>}
            {props.spotifyMode &&
            <button className={"playlist-button"} onClick={sendPlaylistToSpotify}>Send to Spotify</button>}
            {showStatus &&
                <div>
                    {allPlaylistTrackElements}
                </div>}
        </div>
    )
}