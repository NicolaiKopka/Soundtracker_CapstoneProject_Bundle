import {useEffect, useState} from "react";
import {SpotifyUserPlaylists} from "../models";
import {getAllUserPlaylists} from "../api_methods";
import SpotifyPlaylistComponent from "./SpotifyPlaylistComponent";


export default function SpotifyPlaylistPage() {

    const [allPlaylistsObject, setAllPlaylistsObject] = useState<SpotifyUserPlaylists>()

    useEffect(() => {
        getAllUserPlaylists()
            .then(data => setAllPlaylistsObject(data))
    }, [])

    console.log(allPlaylistsObject)
    const playlists = allPlaylistsObject?.items.map(playlist => <SpotifyPlaylistComponent key={playlist.name} playlist={playlist}/>)


    return (
        <div>
            {playlists}
        </div>
    )
}