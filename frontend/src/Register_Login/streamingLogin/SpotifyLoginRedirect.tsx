import {useEffect} from "react";
import {getSpotifyAccessTokenFromBackend} from "../../api_methods";


export default function SpotifyLoginRedirect() {

    useEffect(() => {
        getSpotifyAccessTokenFromBackend(window.location.search).catch()
    })

    return (
        <div>
            ....Redirect....
        </div>
    )
}