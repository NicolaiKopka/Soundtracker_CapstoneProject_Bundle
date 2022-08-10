// import {addTrackToUserPlaylist, createNewUserPlaylist, deleteTrackFromUserPlaylist} from "../api_methods";
import Spotify from "react-spotify-embed";
import {SpotifyTrackDTO} from "../models";
import {deleteTrackFromUserPlaylist} from "../api_methods";
import toast from "react-hot-toast";
import "./MyPlaylistTrackElement.css"
import {useRef} from "react";
import Iframe from "react-iframe";

interface TrackListProps {
    refreshTracks: Function
    refreshPlaylist: Function
    playlistKey: string
    spotifyTrack: SpotifyTrackDTO
    editMode: boolean
    deezerId: string
    streamingMode: boolean
    currentProvider: string
}

export default function MyPlaylistTrackElement(props: TrackListProps) {

    const ref = useRef({} as HTMLIFrameElement)

    // useEffect(() => {
    //     ref.current.classList.remove("attention-border")
    //     if(props.streamingMode) {
    //         if(props.playlistMap[props.playlistKey].deezerTrackIds.includes("0") && props.currentProvider === "deezer") {
    //             ref.current.classList.add("attention-border")
    //         }
    //         if(props.playlistMap[props.playlistKey].spotifyTrackIds.includes("0") && props.currentProvider === "spotify") {
    //             ref.current.classList.add("attention-border")
    //         }
    //     }
    // }, [props.streamingMode, props.currentProvider])

    function deleteFromPlaylist() {
        deleteTrackFromUserPlaylist(props.playlistKey, props.spotifyTrack.id, props.deezerId)
            .then(() => props.refreshTracks())
            .then(() => props.refreshPlaylist())
            .then(() => toast.success("Deleted from playlist"))
            .catch(error => error.response.data)
    }

    return (
        <div className={"my-playlist-track-element"}>
            {(props.currentProvider === "spotify" && props.spotifyTrack.id !== "0") ? <>
                <Spotify ref={ref} className={"my-playlist-spotify"} wide link={props.spotifyTrack.url}/>
                {props.editMode &&
                    <button className={"playlist-delete-button"} onClick={deleteFromPlaylist}><i className="fa-solid fa-circle-xmark fa-xl x-icon"></i></button>}
            </> : <>
                <Iframe width={"300"} height={"120"} frameBorder={0} url={`https://widget.deezer.com/widget/dark/track/${props.deezerId}?tracklist=false`}/>
                {props.editMode &&
                    <button className={"playlist-delete-button"} onClick={deleteFromPlaylist}><i className="fa-solid fa-circle-xmark fa-xl x-icon"></i></button>}
            </>}

        </div>
    )
}