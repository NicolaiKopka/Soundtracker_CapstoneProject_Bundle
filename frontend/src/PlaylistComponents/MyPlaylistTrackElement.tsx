// import {addTrackToUserPlaylist, createNewUserPlaylist, deleteTrackFromUserPlaylist} from "../api_methods";
import Spotify from "react-spotify-embed";
import {SpotifyTrackDTO} from "../models";

interface TrackListProps {
    // userPlaylists: UserPlaylistMap
    // currentKey: string
    track: SpotifyTrackDTO
    // updatePlaylists: Function
}

export default function MyPlaylistTrackElement(props: TrackListProps) {

    // function deleteFromPlaylist() {
    //     deleteTrackFromUserPlaylist(props.currentKey, props.track.id, props.track.id)
    //         .then(() => props.updatePlaylists())
    //         .catch()
    // }

    return (
        <div className={"track-element"}>
            <Spotify className={"spotify-player"} wide link={props.track.url}/>
            <div>
                {/*<button className="favorites-button delete-button" onClick={deleteFromPlaylist}>delete from playlist</button>*/}
            </div>
        </div>
    )
}