import {SpotifyTrackDTO, UserPlaylistMap} from "../models";
import Spotify from "react-spotify-embed";
import "./TrackElement.css"

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
        <div className={"track-element"}>
            <Spotify className={"spotify-player"} wide link={props.track.url}/>
            <div>
                {props.userPlaylists[props.currentKey] !== undefined && props.userPlaylists[props.currentKey].spotifyTrackIds.includes(props.track.id) ?
                    <button className="favorites-button delete-button" onClick={() => props.deleteFromPlaylist(props.index)}>delete from playlist</button> :
                    <button className="favorites-button" onClick={() => props.addToPlaylist(props.index)}>add to playlist</button>
                }
            </div>
        </div>
    )
}