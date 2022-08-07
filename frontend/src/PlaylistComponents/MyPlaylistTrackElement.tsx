// import {addTrackToUserPlaylist, createNewUserPlaylist, deleteTrackFromUserPlaylist} from "../api_methods";
import Spotify from "react-spotify-embed";
import {SpotifyTrackDTO} from "../models";
import {deleteTrackFromUserPlaylist} from "../api_methods";
import toast from "react-hot-toast";
import "./MyPlaylistTrackElement.css"

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
        <div className={"my-playlist-track-element"}>
            <Spotify className={"my-playlist-spotify"} wide link={props.track.url}/>
                {props.editMode &&
                    <button className={"playlist-delete-button"} onClick={deleteFromPlaylist}><i className="fa-solid fa-circle-xmark fa-xl x-icon"></i></button>}
        </div>
    )
}