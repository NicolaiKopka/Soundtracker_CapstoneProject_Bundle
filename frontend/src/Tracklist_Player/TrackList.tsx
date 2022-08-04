import {useParams} from "react-router-dom";
import {ChangeEvent, useCallback, useEffect, useState} from "react";
import {getAllUserPlaylists, getDeezerAlbumById, getSpotifyAlbumById} from "../api_methods";
import {DeezerTrack, SpotifyTrackDTO, UserPlaylistMap} from "../models";
import SpotifyTrackElement from "./SpotifyTrackElement";
import Header from "../Header/Header";
import "./TrackList.css"
import DeezerTrackElement from "./DeezerTrackElement";

export default function TrackList() {

    const [spotifyTrackList, setSpotifyTrackList] = useState<Array<SpotifyTrackDTO>>([])
    const [deezerTrackList, setDeezerTrackList] = useState<Array<DeezerTrack>>([])
    const [userPlaylists, setUserPlaylists] = useState({} as UserPlaylistMap)
    const [currentPlaylistKey, setCurrentPlaylistKey] = useState("New Playlist")
    const [newPlaylistName, setNewPlaylistName] = useState("")

    const {spotifyId, deezerId} = useParams()

    const updateUserPlaylists = useCallback(() => {
        getAllUserPlaylists().then(data => setUserPlaylists(data.userPlaylists))
    }, [])

    useEffect(() => {
        if(spotifyId) {
            getSpotifyAlbumById(spotifyId).then((data) => setSpotifyTrackList(data))
        }
        if(deezerId) {
            getDeezerAlbumById(deezerId).then(data => setDeezerTrackList(data))
        }
    }, [spotifyId, deezerId])

    useEffect(() => {
        updateUserPlaylists()
    }, [updateUserPlaylists])

    const setKey = (event: ChangeEvent<HTMLSelectElement>) => {
        setCurrentPlaylistKey(event.target.value)
    }

    const trackElements = spotifyTrackList.map(track => <SpotifyTrackElement
        key={track.id}
        newPlaylistName={newPlaylistName}
        currentKey={currentPlaylistKey}
        userPlaylists={userPlaylists}
        updatePlaylists={updateUserPlaylists}
        setCurrentPlaylistKey={setCurrentPlaylistKey}
        setNewPlaylistName={setNewPlaylistName}
        track={track}/>)

    const deezerTrackElements = deezerTrackList.map(track => <DeezerTrackElement key={track.id} track={track}/>)

    const playlists = Object.keys(userPlaylists)

    return (
        <div>
            <Header/>
            <div className={"track-wrapper"}>
                {currentPlaylistKey === "New Playlist" && <input className={"form-spacer"} placeholder={"Enter new playlist name"} required={true} value={newPlaylistName} onChange={ev => setNewPlaylistName(ev.target.value)}/>}
                <div>
                    <label>Choose Playlist: </label>
                    <select className={"track-selector form-spacer"} value={currentPlaylistKey} onChange={setKey}>
                        <option selected={currentPlaylistKey === "New Playlist"} value={"New Playlist"}>New Playlist</option>
                        {playlists.map(key => <option key={key} selected={currentPlaylistKey === key} value={key}>{key}</option>)}
                    </select>
                </div>
            </div>
            <div>
                {trackElements}
                {deezerTrackElements}
            </div>
        </div>
    )
}