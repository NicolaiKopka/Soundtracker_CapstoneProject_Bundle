import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {getSpotifyAlbumById} from "../api_methods";
import {SpotifyTrackDTO} from "../models";
import TrackElement from "./TrackElement";
import SpotifyPlayer from "react-spotify-web-playback";


export default function TrackList() {

    const [trackList, setTrackList] = useState<Array<SpotifyTrackDTO>>([])

    const {id} = useParams()

    useEffect(() => {
        if(id) {
            getSpotifyAlbumById(id).then((data) => setTrackList(data))
        }
    }, [])

    const trackElements = trackList.map(track => <TrackElement key={track.id} track={track}/>)

    // const trackDivs = trackList.map(track => <div>{track.name}</div>)

    return (
        <div>
            {trackElements}
        </div>
    )
}