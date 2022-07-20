import {SpotifyPlaylist} from "../models";

interface SpotifyPlaylistPageProps {
    playlist: SpotifyPlaylist
}

export default function SpotifyPlaylistComponent(props: SpotifyPlaylistPageProps) {
    return (
        <div>
            {props.playlist.name}
            <br/>
            {props.playlist.description}
            <br/>
            {props.playlist.href}
        </div>
    )
}