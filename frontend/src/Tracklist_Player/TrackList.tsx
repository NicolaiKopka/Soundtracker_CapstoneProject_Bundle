import {useParams} from "react-router-dom";
import {ChangeEvent, useCallback, useEffect, useState} from "react";
import {getAllUserPlaylists, getSpotifyAlbumById} from "../api_methods";
import {SpotifyTrackDTO, UserPlaylistMap} from "../models";
import TrackElement from "./TrackElement";
import Header from "../Header/Header";
import "./TrackList.css"

export default function TrackList() {

    const [trackList, setTrackList] = useState<Array<SpotifyTrackDTO>>([])
    const [userPlaylists, setUserPlaylists] = useState({} as UserPlaylistMap)
    const [currentPlaylistKey, setCurrentPlaylistKey] = useState("New Playlist")
    const [newPlaylistName, setNewPlaylistName] = useState("")

    const {id} = useParams()

    const updateUserPlaylists = useCallback(() => {
        getAllUserPlaylists().then(data => setUserPlaylists(data.userPlaylists))
    }, [])

    useEffect(() => {
        if(id) {
            getSpotifyAlbumById(id).then((data) => setTrackList(data))
        }
    }, [id])

    useEffect(() => {
        updateUserPlaylists()
    }, [updateUserPlaylists])

    const setKey = (event: ChangeEvent<HTMLSelectElement>) => {
        setCurrentPlaylistKey(event.target.value)
        console.log(currentPlaylistKey)
    }

    const trackElements = trackList.map(track => <TrackElement
        key={track.id}
        newPlaylistName={newPlaylistName}
        currentKey={currentPlaylistKey}
        userPlaylists={userPlaylists}
        updatePlaylists={updateUserPlaylists}
        setCurrentPlaylistKey={setCurrentPlaylistKey}
        setNewPlaylistName={setNewPlaylistName}
        track={track}/>)

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
            </div>
        </div>
    )
}