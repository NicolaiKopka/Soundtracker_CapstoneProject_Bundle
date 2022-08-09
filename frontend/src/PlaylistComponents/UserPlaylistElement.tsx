import {useEffect, useRef, useState} from "react";
import {SpotifyTrackDTO, UserPlaylistMap} from "../models";
import {
    addDeezerPlaylist,
    addSpotifyPlaylist, addTracksToDeezerPlaylist,
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
    currentProvider: string
    playlistMap: UserPlaylistMap
}

export default function UserPlaylistElement(props: MyPlaylistPageProps) {

    const [showStatus, setShowStatus] = useState(false)
    const [streamingMode, setStreamingMode] = useState<boolean>()
    const [spotifyTracks, setSpotifyTracks] = useState<Array<SpotifyTrackDTO>>([])
    const [deezerTrackIds, setDeezerTrackIds] = useState<Array<string>>([])

    const ref = useRef({} as HTMLButtonElement)

    useEffect(() => {
        setStreamingMode(props.spotifyMode)
    }, [props.spotifyMode])

    useEffect(() => {
        ref.current.classList.remove("attention-border")
        if(streamingMode) {
            if(props.playlistMap[props.playlistKey].deezerTrackIds.includes("0") && props.currentProvider === "deezer") {
                ref.current.classList.add("attention-border")
            }
            if(props.playlistMap[props.playlistKey].spotifyTrackIds.includes("0") && props.currentProvider === "spotify") {
                ref.current.classList.add("attention-border")
            }
        }
    }, [streamingMode, props.currentProvider])

    function switchShowStatus() {
        if(showStatus) {
            setShowStatus(false)
        }else {
            setShowStatus(true)
            getAllPlaylistTracks()
        }
    }

    function getAllPlaylistTracks() {
        getAllSpotifyTracksInPlaylist(props.playlistKey)
            .then(data => setSpotifyTracks(data))
            .then(() => setDeezerTrackIds(props.playlistMap[props.playlistKey].deezerTrackIds))
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

    function sendPlaylistToDeezer() {
        addDeezerPlaylist(props.playlistName)
            .then(data => {
                addTracksToDeezerPlaylist(data.id, props.playlistKey)
                    .then(() => {
                        toast.success("Sent playlist to deezer")
                        props.setPlaylistName("")
                    })
            })
    }

    const allPlaylistTrackElements = spotifyTracks?.map((track, index) =>
        <MyPlaylistTrackElement refreshPlaylist={props.refreshPlaylists}
                                refreshTracks={getAllPlaylistTracks}
                                playlistKey={props.playlistKey}
                                editMode={props.editMode} spotifyTrack={track}
                                deezerId={deezerTrackIds[index]} currentProvider={props.currentProvider}
                                streamingMode={props.spotifyMode}/>)

    return (
        <div>
            <button ref={ref} className={"playlist-button"} onClick={switchShowStatus}>{props.playlistKey}<i
                className="fa-solid fa-arrow-down spacer-left"></i></button>
            {props.editMode &&
                <button className={"playlist-button delete-button"} onClick={deletePlaylist}><i className="fa-solid fa-trash-can spacer"></i>Delete Playlist</button>}
            {props.spotifyMode && props.currentProvider === "spotify" &&
            <button disabled={props.playlistName === ""} className={"playlist-button"} onClick={sendPlaylistToSpotify}>Send to Spotify</button>}
            {props.spotifyMode && props.currentProvider === "deezer" &&
                <button disabled={props.playlistName === ""} className={"playlist-button"} onClick={sendPlaylistToDeezer}>Send to Deezer</button>}
            {showStatus &&
                <div>
                    {allPlaylistTrackElements}
                </div>}
        </div>
    )
}