import {SpotifyTrackDTO} from "../models";
import SpotifyPlayer from "react-spotify-web-playback";
import {useState} from "react";

interface TrackListProps {
    track: SpotifyTrackDTO
}

export default function TrackElement(props: TrackListProps) {

    const [playerState, setPlayerState] = useState(false)

    function createPlayer() {
        setPlayerState(true)
    }


    return (
        <div>
            <div>
                <a onClick={createPlayer}>{props.track.name}</a>
            </div>
            {playerState &&
            <SpotifyPlayer
                token={localStorage.getItem("spotify_jwt")!.toString()}
                uris={[`spotify:track:${props.track.id}`]}/>}
        </div>
    )
}