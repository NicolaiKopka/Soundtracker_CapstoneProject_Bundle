import {useEffect} from "react";
import {getSpotifyAccessTokenFromBackend} from "../../api_methods";
import {useNavigate} from "react-router-dom";


export default function SpotifyLoginRedirect() {

    const nav = useNavigate()

    useEffect(() => {
        getSpotifyAccessTokenFromBackend(window.location.search).then(data => {
            localStorage.setItem("jwt", data.jwtToken)
            localStorage.setItem("spotify_jwt", data.spotifyToken)
        }).then(() => nav("/"))
    },[nav])

    return (
        <div>
            ....Redirect....
        </div>
    )
}