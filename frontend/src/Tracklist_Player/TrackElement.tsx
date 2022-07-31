import {SpotifyTrackDTO, UserPlaylistMap} from "../models";
import Spotify from "react-spotify-embed";
import {addTrackToUserPlaylist, createNewUserPlaylist, deleteTrackFromUserPlaylist} from "../api_methods";

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
        <div>
            <div>
                {props.userPlaylists[props.currentKey] !== undefined && props.userPlaylists[props.currentKey].spotifyTrackIds.includes(props.track.id) ?
                    <button onClick={deleteFromPlaylist}>delete</button> :
                    <button onClick={addToPlaylist}>add</button>
                }
            </div>
            <Spotify wide link={props.track.url}/>
        </div>
    )
}