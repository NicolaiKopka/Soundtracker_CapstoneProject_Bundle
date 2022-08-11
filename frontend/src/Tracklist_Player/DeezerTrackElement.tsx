import "./TrackElement.css"
import {DeezerTrack, UserPlaylistMap} from "../models";
import Iframe from "react-iframe";
import "./DeezerTrackElement.css"

interface TrackListProps {
    userPlaylists: UserPlaylistMap
    currentKey: string
    track: DeezerTrack
    index: number
    addToPlaylist: (index: number) => void
    deleteFromPlaylist: (index: number) => void
    newPlaylistName: string
}

export default function DeezerTrackElement(props: TrackListProps) {

    return (
        <div className={"my-playlist-track-element"}>
            <Iframe width={"300"} height={"120"} frameBorder={0} url={`https://widget.deezer.com/widget/dark/track/${props.track.id}?tracklist=false`}/>
            <div>
                {props.userPlaylists[props.currentKey] !== undefined && props.userPlaylists[props.currentKey].deezerTrackIds.toString().includes(props.track.id) ?
                    <button className={"playlist-delete-button"} onClick={() => props.deleteFromPlaylist(props.index)}><i className="fa-solid fa-circle-xmark fa-xl x-icon"></i></button> :
                    <button disabled={props.newPlaylistName.length === 0 && props.currentKey === "New Playlist"} className={"playlist-delete-button"} onClick={() => props.addToPlaylist(props.index)}><i
                        className="fa-solid fa-plus fa-xl plus-icon"></i></button>
                }
            </div>
        </div>
    )
}