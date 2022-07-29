import {SpotifyTrackDTO, UserPlaylistMap} from "../models";
import Spotify from "react-spotify-embed";
import {useEffect, useState} from "react";
import {addTrackToUserPlaylist, createNewUserPlaylist, deleteTrackFromUserPlaylist} from "../api_methods";

interface TrackListProps {
    userPlaylists: UserPlaylistMap
    currentKey: string
    track: SpotifyTrackDTO
    newPlaylistName: string
}

export default function TrackElement(props: TrackListProps) {

    const [currentKey, setCurrentKey] = useState("")
    // const [playerState, setPlayerState] = useState(false)
    // const [userPlaylists, setUserPlaylists] = useState({} as UserPlaylistMap)
    // const [alreadyAdded, setAlreadyAdded] = useState(false)

    useEffect(() => {
        // setUserPlaylists(props.userPlaylists)
        setCurrentKey(props.currentKey)
    }, [props.currentKey, props.userPlaylists])

    // useEffect(() => {
    //     if(props.track.id in userPlaylists[props.currentKey].spotifyTrackIds) {
    //         setAlreadyAdded(true)
    //     } else {
    //         setAlreadyAdded(false)
    //     }
    // },[])

    // function createPlayer() {
    //     setPlayerState(true)
    // }

    function addToPlaylist() {
        if(currentKey === "New Playlist") {
            createNewUserPlaylist(props.newPlaylistName).then(
                () => addTrackToUserPlaylist(props.newPlaylistName, props.track.id, props.track.id)
                .catch())
        }
        addTrackToUserPlaylist(currentKey, props.track.id, props.track.id)
            .catch()
    }

    function deleteFromPlaylist() {
        deleteTrackFromUserPlaylist(currentKey, props.track.id, props.track.id)
            .catch()
    }


    return (
        <div>
            <div>
                {/*<button onClick={createPlayer}>{props.track.name}</button>*/}
                <button onClick={addToPlaylist}>add</button>
                <button onClick={deleteFromPlaylist}>delete</button>
            </div>
            <Spotify wide link={props.track.url}/>
            {/*{playerState &&*/}
            {/*<SpotifyPlayer*/}
            {/*    token={localStorage.getItem("spotify_jwt")!.toString()}*/}
            {/*    uris={[`spotify:track:${props.track.id}`]}/>}*/}
        </div>
    )
}