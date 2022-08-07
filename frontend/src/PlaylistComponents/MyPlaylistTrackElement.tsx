// import {addTrackToUserPlaylist, createNewUserPlaylist, deleteTrackFromUserPlaylist} from "../api_methods";
import Spotify from "react-spotify-embed";
import {SpotifyTrackDTO} from "../models";
import {deleteTrackFromUserPlaylist} from "../api_methods";
import toast from "react-hot-toast";

interface TrackListProps {
    refreshTracks: Function
    playlistKey: string
    track: SpotifyTrackDTO
    editMode: boolean
    deezerId: string
}

export default function MyPlaylistTrackElement(props: TrackListProps) {

    function deleteFromPlaylist() {
        deleteTrackFromUserPlaylist(props.playlistKey, props.track.id, props.deezerId)
            .then(() => props.refreshTracks())
            .then(() => toast.success("Deleted from playlist"))
            .catch(error => error.response.data)
    }

    return (
        <div className={"track-element"}>
            <Spotify className={"spotify-player"} wide link={props.track.url}/>
            <div>
                {props.editMode &&
                    <button onClick={deleteFromPlaylist} className="favorites-button delete-button">delete from playlist</button>}
            </div>
        </div>
    )
}