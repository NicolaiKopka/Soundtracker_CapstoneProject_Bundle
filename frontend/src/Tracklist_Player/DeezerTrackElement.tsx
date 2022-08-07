import "./TrackElement.css"
import {DeezerTrack} from "../models";
import Iframe from "react-iframe";

interface TrackListProps {
    track: DeezerTrack
    index: number
}

export default function DeezerTrackElement(props: TrackListProps) {

    return (
        <div className={"track-element"}>
            <Iframe url={`https://widget.deezer.com/widget/dark/track/${props.track.id}?tracklist=false`}/>
            {/*<iframe title={`deezer-widget" src="https://widget.deezer.com/widget/dark/track/${props.track.id}?tracklist=false`}*/}
            {/*        width="410" height="120" frameBorder="0"*/}
            {/*        allow="encrypted-media; clipboard-write"></iframe>*/}
        </div>
    )
}