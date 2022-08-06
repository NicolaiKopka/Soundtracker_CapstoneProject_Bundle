import {useEffect} from "react";
import {
    getSpotifyAccessTokenFromBackend,
    getSpotifyAccessWithoutLogin
} from "../../api_methods";
import {useNavigate} from "react-router-dom";
import toast from "react-hot-toast";


export default function SpotifyLoginRedirect() {

    const nav = useNavigate()

    useEffect(() => {
        if(localStorage.getItem("jwt") !== null) {
            const toastId = toast.loading("You are being redirected")
            getSpotifyAccessWithoutLogin(window.location.search).then(data => {
                localStorage.setItem("spotify_jwt", data.spotifyToken)
            }).then(() => {
                toast.success("Got the token", {
                    id: toastId
                })
                nav("/my-playlists")
            })
                .catch(() => toast("Oops, that didn't work"))
        }else {
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
        }
    },[nav])

    return (
        <div>
            ....Redirect....
        </div>
    )
}