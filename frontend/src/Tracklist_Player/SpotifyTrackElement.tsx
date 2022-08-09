import {SpotifyTrackDTO, UserPlaylistMap} from "../models";
import Spotify from "react-spotify-embed";
import "./TrackElement.css"
import "./SpotifyTrackElement.css"

interface TrackListProps {
    userPlaylists: UserPlaylistMap
    currentKey: string
    track: SpotifyTrackDTO
    index: number
    addToPlaylist: (index: number) => void
    deleteFromPlaylist: (index: number) => void
}

export default function SpotifyTrackElement(props: TrackListProps) {





    return (
        <div className={"my-playlist-track-element"}>
            <Spotify className={"spotify-player"} wide link={props.track.url}/>
            <div>
                {props.userPlaylists[props.currentKey] !== undefined && props.userPlaylists[props.currentKey].spotifyTrackIds.includes(props.track.id) ?
                    <button className={"playlist-delete-button"} onClick={() => props.deleteFromPlaylist(props.index)}><i className="fa-solid fa-circle-xmark fa-xl x-icon"></i></button> :
                    <button className={"playlist-delete-button"} onClick={() => props.addToPlaylist(props.index)}><i
                        className="fa-solid fa-plus fa-xl plus-icon"></i></button>
                }
            </div>
        </div>
    )
}