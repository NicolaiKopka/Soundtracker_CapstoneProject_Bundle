import {useParams} from "react-router-dom";
import {ChangeEvent, useCallback, useEffect, useState} from "react";
import {
    addTrackToUserPlaylist,
    createNewUserPlaylist, deleteTrackFromUserPlaylist,
    getAllUserPlaylists,
    getDeezerAlbumById,
    getStreamingAlbumsById
} from "../api_methods";
import {DeezerTrack, SpotifyTrackDTO, StreamingTracks, UserPlaylistMap} from "../models";
import SpotifyTrackElement from "./SpotifyTrackElement";
import Header from "../Header/Header";
import "./TrackList.css"
import DeezerTrackElement from "./DeezerTrackElement";
import toast from "react-hot-toast";

export default function TrackList() {

    const [streamingDTO, setStreamingDTO] = useState<StreamingTracks>()
    const [spotifyTrackList, setSpotifyTrackList] = useState<Array<SpotifyTrackDTO>>([])
    const [deezerTrackList, setDeezerTrackList] = useState<Array<DeezerTrack>>([])
    const [userPlaylists, setUserPlaylists] = useState({} as UserPlaylistMap)
    const [currentPlaylistKey, setCurrentPlaylistKey] = useState("New Playlist")
    const [newPlaylistName, setNewPlaylistName] = useState("")
    const [streamingMode, setStreamingMode] = useState("spotify")

    let {spotifyId, deezerId} = useParams()

    const updateUserPlaylists = useCallback(() => {
        getAllUserPlaylists().then(data => setUserPlaylists(data.userPlaylists))
    }, [])

    useEffect(() => {
        if(!spotifyId){
            spotifyId = "0"
        }
        if(!deezerId){
            deezerId = "0"
        }
        getStreamingAlbumsById(spotifyId, deezerId).then((data) => {
            setSpotifyTrackList(data.spotifyTracks)
            setDeezerTrackList(data.deezerTracks)
        })
    }, [spotifyId, deezerId])

    useEffect(() => {
        updateUserPlaylists()
    }, [updateUserPlaylists])

    function addToPlaylist(index: number) {
        let deezerId = "0"
        let spotifyId = "0"
        if(!(deezerTrackList.length === 0)) {
            deezerId = deezerTrackList[index].id
        }
        if(!(spotifyTrackList.length === 0)) {
            spotifyId = spotifyTrackList[index].id
        }
        if(currentPlaylistKey === "New Playlist") {
            createNewUserPlaylist(newPlaylistName).then(
                () => addTrackToUserPlaylist(newPlaylistName, spotifyId, deezerId)
                    .then(() => updateUserPlaylists())
                    .then(() => setCurrentPlaylistKey(newPlaylistName))
                    .then(() => setNewPlaylistName("")))
                .then(() => toast.success("Added to playlist"))
                .catch(error => toast.error(error.response.data))
        } else {
            addTrackToUserPlaylist(currentPlaylistKey, spotifyId, deezerId)
                .then(() => updateUserPlaylists())
                .then(() => toast.success("Added to playlist"))
                .catch(error => error.response.data)
        }
    }

    function deleteFromPlaylist(index: number) {
        let deezerId = "0"
        let spotifyId = "0"
        if(!(deezerTrackList.length === 0)) {
            deezerId = deezerTrackList[index].id
        }
        if(!(spotifyTrackList.length === 0)) {
            spotifyId = spotifyTrackList[index].id
        }
        deleteTrackFromUserPlaylist(currentPlaylistKey, spotifyId, deezerId)
            .then(() => updateUserPlaylists())
            .then(() => toast.success("Deleted from playlist"))
            .catch(error => error.response.data)
    }

    const setKey = (event: ChangeEvent<HTMLSelectElement>) => {
        setCurrentPlaylistKey(event.target.value)
    }

    const trackElements = spotifyTrackList.map((track, index)=> <SpotifyTrackElement
        key={track.id}
        currentKey={currentPlaylistKey}
        userPlaylists={userPlaylists}
        track={track}
        index={index} addToPlaylist={addToPlaylist} deleteFromPlaylist={deleteFromPlaylist}/>)

    const deezerTrackElements = deezerTrackList.map((track, index) => <DeezerTrackElement key={track.id} track={track} index={index}/>)

    const playlists = Object.keys(userPlaylists)

    return (
        <div>
            <Header/>
            <div className="radio-container">
                <input type="radio" name="radio" disabled={spotifyTrackList.length === 0} onChange={ev => setStreamingMode(ev.target.value)} value={"spotify"} checked={streamingMode === "spotify"}/>
                <span>Spotify</span>
                <input type="radio" name="radio" disabled={deezerTrackList.length === 0} onChange={ev => setStreamingMode(ev.target.value)} value={"deezer"} checked={streamingMode === "deezer"}/>
                <span>Deezer</span>
            </div>
            <div className={"track-wrapper"}>
                {currentPlaylistKey === "New Playlist" &&
                    <input className={"form-spacer"} placeholder={"Enter new playlist name"} required={true}
                           value={newPlaylistName} onChange={ev => setNewPlaylistName(ev.target.value)}/>}
                <div>
                    <label>Choose Playlist: </label>
                    <select className={"track-selector form-spacer"} value={currentPlaylistKey} onChange={setKey}>
                        <option selected={currentPlaylistKey === "New Playlist"} value={"New Playlist"}>New Playlist
                        </option>
                        {playlists.map(key => <option key={key} selected={currentPlaylistKey === key}
                                                      value={key}>{key}</option>)}
                    </select>
                </div>
            </div>
            <div>
                {streamingMode === "spotify" && trackElements}
                {streamingMode === "deezer" && deezerTrackElements}
            </div>
        </div>
    )
}