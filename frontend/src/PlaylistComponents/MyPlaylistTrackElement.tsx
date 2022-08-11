// import {addTrackToUserPlaylist, createNewUserPlaylist, deleteTrackFromUserPlaylist} from "../api_methods";
import Spotify from "react-spotify-embed";
import {SpotifyTrackDTO} from "../models";
import {deleteTrackFromUserPlaylist} from "../api_methods";
import toast from "react-hot-toast";
import "./MyPlaylistTrackElement.css"
import {useEffect, useRef} from "react";
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

    const ref = useRef({} as HTMLDivElement)

    useEffect(() => {
        ref.current.classList.remove("attention-border")
        if(props.streamingMode) {
            if(props.deezerId === "0" && props.currentProvider === "deezer") {
                ref.current.classList.add("attention-border")
            }
            if(props.spotifyTrack.id === "0" && props.currentProvider === "spotify") {
                ref.current.classList.add("attention-border")
            }
        }
    }, [props.streamingMode, props.currentProvider, props.spotifyTrack.id, props.deezerId])

    function deleteFromPlaylist() {
        deleteTrackFromUserPlaylist(props.playlistKey, props.spotifyTrack.id, props.deezerId)
            .then(() => props.refreshPlaylist())
            // .then(() => props.refreshTracks())
            .then(() => toast.success("Deleted from playlist"))
            .catch(error => error.response.data)
    }

    return (
        <div className={"my-playlist-track-element"}>
            <div ref={ref} className={"track-element-wrapper"}>
                {props.currentProvider === "none" &&
                    (props.spotifyTrack.id !== "0" ? <>
                        <Spotify className={"my-playlist-spotify"} wide link={props.spotifyTrack.url}/>
                        {props.editMode &&
                            <button className={"playlist-delete-button"} onClick={deleteFromPlaylist}><i
                                className="fa-solid fa-circle-xmark fa-xl x-icon"></i></button>}
                    </> : <>
                        <Iframe width={"300"} height={"120"} frameBorder={0}
                                url={`https://widget.deezer.com/widget/dark/track/${props.deezerId}?tracklist=false`}/>
                        {props.editMode &&
                            <button className={"playlist-delete-button"} onClick={deleteFromPlaylist}><i
                                className="fa-solid fa-circle-xmark fa-xl x-icon"></i></button>}
                    </>)}
                {props.currentProvider === "spotify" &&
                    (props.spotifyTrack.id !== "0" ? <>
                        <Spotify className={"my-playlist-spotify"} wide link={props.spotifyTrack.url}/>
                        {props.editMode &&
                            <button className={"playlist-delete-button"} onClick={deleteFromPlaylist}><i
                                className="fa-solid fa-circle-xmark fa-xl x-icon"></i></button>}
                    </> : <>
                        <Iframe width={"300"} height={"120"} frameBorder={0}
                                url={`https://widget.deezer.com/widget/dark/track/${props.deezerId}?tracklist=false`}/>
                        {props.editMode &&
                            <button className={"playlist-delete-button"} onClick={deleteFromPlaylist}><i
                                className="fa-solid fa-circle-xmark fa-xl x-icon"></i></button>}
                    </>)}
                {props.currentProvider === "deezer" &&
                    (props.deezerId !== "0" ? <>
                        <Iframe width={"300"} height={"120"} frameBorder={0}
                                url={`https://widget.deezer.com/widget/dark/track/${props.deezerId}?tracklist=false`}/>
                        {props.editMode &&
                            <button className={"playlist-delete-button"} onClick={deleteFromPlaylist}><i
                                className="fa-solid fa-circle-xmark fa-xl x-icon"></i></button>}
                    </> : <>
                        <Spotify ref={ref} className={"my-playlist-spotify"} wide link={props.spotifyTrack.url}/>
                        {props.editMode &&
                            <button className={"playlist-delete-button"} onClick={deleteFromPlaylist}><i
                                className="fa-solid fa-circle-xmark fa-xl x-icon"></i></button>}
                    </>)
                }
            </div>
        </div>
    )
}