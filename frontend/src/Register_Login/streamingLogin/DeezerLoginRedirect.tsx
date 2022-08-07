import {useNavigate} from "react-router-dom";
import {useEffect} from "react";
import toast from "react-hot-toast";
import {getDeezerAccessTokenFromBackend, getDeezerAccessWithoutLogin} from "../../api_methods";


export default function DeezerLoginRedirect() {
    const nav = useNavigate()

    useEffect(() => {
        const toastId = toast.loading("You are being redirected")
        if(localStorage.getItem("jwt") !== null) {
            getDeezerAccessWithoutLogin(window.location.search).then(data => {
                localStorage.setItem("deezer_jwt", data.deezerToken)
            }).then(() => {
                toast.success("Got the token", {
                    id: toastId
                })
                nav("/my-playlists")
            })
                .catch(() => toast("Oops, that didn't work"))
        } else {
            getDeezerAccessTokenFromBackend(window.location.search).then(data => {
                localStorage.setItem("jwt", data.jwtToken)
                localStorage.setItem("deezer_jwt", data.deezerToken)
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