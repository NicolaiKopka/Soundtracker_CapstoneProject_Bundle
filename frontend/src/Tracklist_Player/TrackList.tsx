import {useParams} from "react-router-dom";
import {ChangeEvent, useCallback, useEffect, useState} from "react";
import {getAllUserPlaylists, getSpotifyAlbumById} from "../api_methods";
import {SpotifyTrackDTO, UserPlaylistMap} from "../models";
import TrackElement from "./TrackElement";

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
        track={track}/>)
    const playlists = Object.keys(userPlaylists)

    return (
        <div>
            {currentPlaylistKey === "New Playlist" && <input value={newPlaylistName} onChange={ev => setNewPlaylistName(ev.target.value)}/>}
            <select value={currentPlaylistKey} onChange={setKey}>
                <option selected={true} value={"New Playlist"}>New Playlist</option>
                {playlists.map(key => <option key={key} value={key}>{key}</option>)}
            </select>
            {trackElements}
        </div>
    )
}