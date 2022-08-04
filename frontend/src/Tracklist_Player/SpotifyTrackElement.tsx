import {SpotifyTrackDTO, UserPlaylistMap} from "../models";
import Spotify from "react-spotify-embed";
import {addTrackToUserPlaylist, createNewUserPlaylist, deleteTrackFromUserPlaylist} from "../api_methods";
import "./TrackElement.css"
import toast from "react-hot-toast";

interface TrackListProps {
    userPlaylists: UserPlaylistMap
    currentKey: string
    track: SpotifyTrackDTO
    newPlaylistName: string
    updatePlaylists: Function
    setCurrentPlaylistKey: Function
    setNewPlaylistName: Function
}

export default function SpotifyTrackElement(props: TrackListProps) {

    function addToPlaylist() {
        if(props.currentKey === "New Playlist") {
            createNewUserPlaylist(props.newPlaylistName).then(
                () => addTrackToUserPlaylist(props.newPlaylistName, props.track.id, props.track.id)
                    .then(() => props.updatePlaylists())
                    .then(() => props.setCurrentPlaylistKey(props.newPlaylistName))
                    .then(() => props.setNewPlaylistName("")))
                .then(() => toast.success("Added to playlist"))
                .catch(error => toast.error(error.response.data))
        } else {
            addTrackToUserPlaylist(props.currentKey, props.track.id, props.track.id)
                .then(() => props.updatePlaylists())
                .then(() => toast.success("Added to playlist"))
                .catch(error => error.response.data)
        }
    }

    function deleteFromPlaylist() {
        deleteTrackFromUserPlaylist(props.currentKey, props.track.id, props.track.id)
            .then(() => props.updatePlaylists())
            .then(() => toast.success("Deleted from playlist"))
            .catch(error => error.response.data)
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