import {useEffect} from "react";
import {getSpotifyAccessTokenFromBackend} from "../../api_methods";
import {useNavigate} from "react-router-dom";
import toast from "react-hot-toast";


export default function SpotifyLoginRedirect() {

    const nav = useNavigate()

    useEffect(() => {
        const toastId = toast.loading("You are being redirected")
        getSpotifyAccessTokenFromBackend(window.location.search).then(data => {
            localStorage.setItem("jwt", data.jwtToken)
            localStorage.setItem("spotify_jwt", data.spotifyToken)
        }).then(() => {
            toast.success("You are logged in", {
                id: toastId
            })
            nav("/")
        })
            .catch(() => toast("Oops, that didn't work"))
    },[nav])

    return (
        <div>
            ....Redirect....
        </div>
    )
}