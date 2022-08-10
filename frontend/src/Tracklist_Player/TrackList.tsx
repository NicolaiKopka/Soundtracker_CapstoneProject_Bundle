import {useParams} from "react-router-dom";
import {ChangeEvent, useCallback, useEffect, useState} from "react";
import {
    addTrackToUserPlaylist,
    createNewUserPlaylist, deleteTrackFromUserPlaylist,
    getAllUserPlaylists,
    getStreamingAlbumsById
} from "../api_methods";
import {DeezerTrack, SpotifyTrackDTO, UserPlaylistMap} from "../models";
import SpotifyTrackElement from "./SpotifyTrackElement";
import Header from "../Header/Header";
import "./TrackList.css"
import DeezerTrackElement from "./DeezerTrackElement";
import toast from "react-hot-toast";
import spotifyImg from "../images/Spotify_Icon_RGB_Green.png";
import deezerImg from "../images/Colored_Equalizer@2x.png";

export default function TrackList() {
    const [spotifyTrackList, setSpotifyTrackList] = useState<Array<SpotifyTrackDTO>>([])
    const [deezerTrackList, setDeezerTrackList] = useState<Array<DeezerTrack>>([])
    const [userPlaylists, setUserPlaylists] = useState({} as UserPlaylistMap)
    const [currentPlaylistKey, setCurrentPlaylistKey] = useState("New Playlist")
    const [newPlaylistName, setNewPlaylistName] = useState("")
    const [streamingMode, setStreamingMode] = useState("")

    let {spotifyId, deezerId} = useParams()

    const updateUserPlaylists = useCallback(() => {
        getAllUserPlaylists().then(data => setUserPlaylists(data.userPlaylists))
    }, [])

    useEffect(() => {
        getStreamingAlbumsById(spotifyId!, deezerId!).then((data) => {
            setSpotifyTrackList(data.spotifyTracks)
            setDeezerTrackList(data.deezerTracks)
        })
    }, [spotifyId, deezerId])

    useEffect(() => {
        updateUserPlaylists()
        if(localStorage.getItem("spotify_jwt") === null && localStorage.getItem("deezer_jwt") === null) {
            setStreamingMode("spotify")
        } else if(localStorage.getItem("spotify_jwt") === null) {
            if(deezerId === "undefined") {
                setStreamingMode("spotify")
            } else {
                setStreamingMode("deezer")
            }
        } else if(localStorage.getItem("deezer_jwt") === null) {
            if(spotifyId === "undefined") {
                setStreamingMode("deezer")
            } else {
                setStreamingMode("spotify")
            }
        }
    }, [updateUserPlaylists, deezerId, spotifyId])

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

    const deezerTrackElements = deezerTrackList.map((track, index) => <DeezerTrackElement
        key={track.id}
        currentKey={currentPlaylistKey}
        userPlaylists={userPlaylists}
        track={track}
        index={index} addToPlaylist={addToPlaylist} deleteFromPlaylist={deleteFromPlaylist}/>)

    const playlists = Object.keys(userPlaylists)

    return (
        <div>
            <Header/>
            <div className={"streaming-wrapper"}>
                <table className={"streaming-table"}>
                    <tr className={"provider-section"}>
                        <td>
                            <div className={"spotify-div"}>
                                <input className={"radio"} type="radio" name="radio"
                                       onChange={ev => setStreamingMode(ev.target.value)} value={"spotify"}
                                       disabled={spotifyId === "undefined"}
                                       checked={(streamingMode === "spotify" && spotifyId !== "undefined")}/>
                                <img alt={"spotify"} src={spotifyImg}/>
                            </div>
                        </td>
                        <td>
                            <div className={"spotify-div"}>
                                <input className={"radio"} type="radio" name="radio"
                                       onChange={ev => setStreamingMode(ev.target.value)} value={"deezer"}
                                       disabled={deezerId === "undefined"}
                                       checked={streamingMode === "deezer" && deezerId !== "undefined"}/>
                                <img alt={"deezer"} id={"deezer-img"} src={deezerImg}/>
                            </div>
                        </td>
                    </tr>
                </table>
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