import {SpotifyTrackDTO, UserPlaylistMap} from "../models";
import Spotify from "react-spotify-embed";
import {addTrackToUserPlaylist, createNewUserPlaylist, deleteTrackFromUserPlaylist} from "../api_methods";
import "./TrackElement.css"

interface TrackListProps {
    userPlaylists: UserPlaylistMap
    currentKey: string
    track: SpotifyTrackDTO
    newPlaylistName: string
    updatePlaylists: Function
    setCurrentPlaylistKey: Function
    setNewPlaylistName: Function
}

export default function TrackElement(props: TrackListProps) {

    function addToPlaylist() {
        if(props.currentKey === "New Playlist") {
            createNewUserPlaylist(props.newPlaylistName).then(
                () => addTrackToUserPlaylist(props.newPlaylistName, props.track.id, props.track.id)
                    .then(() => props.updatePlaylists())
                    .then(() => props.setCurrentPlaylistKey(props.newPlaylistName))
                    .then(() => props.setNewPlaylistName("")))
                    .catch()
        } else {
            addTrackToUserPlaylist(props.currentKey, props.track.id, props.track.id)
                .then(() => props.updatePlaylists())
                .catch()
        }
    }


    function deleteFromPlaylist() {
        deleteTrackFromUserPlaylist(props.currentKey, props.track.id, props.track.id)
            .then(() => props.updatePlaylists())
            .catch()
    }

    return (
        <div className={"track-element"}>
            <Spotify className={"spotify-player"} wide link={props.track.url}/>
            <div>
                {props.userPlaylists[props.currentKey] !== undefined && props.userPlaylists[props.currentKey].spotifyTrackIds.includes(props.track.id) ?
                    <button className="favorites-button delete-button" onClick={deleteFromPlaylist}>delete from playlist</button> :
                    <button className="favorites-button" onClick={addToPlaylist}>add to playlist</button>
                }
            </div>
        </div>
    )
}