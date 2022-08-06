import Header from "../Header/Header";
import {FormEvent, useState} from "react";
import {loginUser} from "../api_methods";
import {useNavigate} from "react-router-dom";
import "./LoginPage.css"
import toast from "react-hot-toast";

interface AppProps {
    setErrorMessage: Function
}

export default function LoginPage(props: AppProps) {

    const [username, setUsername] = useState("")
    const [password, setPassword] = useState("")
    const nav = useNavigate()

    function login(ev: FormEvent) {
        ev.preventDefault()
        loginUser(username, password)
            .then(data => {
                localStorage.setItem("jwt", data.token)
            })
            .then(() => nav("/"))
            .then(() => toast.success("You are logged in"))
            .catch(() => {
                toast.error("Oops, wrong credentials")
                // props.setErrorMessage("Oops, wrong credentials")
            })
    }

    function throwAlert() {
        alert("Spotify is currently in dev mode for this app. In order to login, your spotify email will have to be deposited in the current project. Contact for more information. If your mail is already deposited you can login.")
    }

    return (
        <div className={"login-form-wrapper"}>
            <Header />
            <div className={"login-form"}>
                <form onSubmit={login}>
                    <input className={"form-items"} required={true} value={username} placeholder={"username"} onChange={ev => setUsername(ev.target.value)}/>
                    <input type={"password"} className={"form-items"} required={true} value={password} placeholder={"password"} onChange={ev => setPassword(ev.target.value)}/>
                    <button className={"login-button"} type={"submit"}>Login</button>
                </form>
                <a href={"/register"}>No account? Click to register</a>
                <a onClick={throwAlert} href={`https://accounts.spotify.com/authorize?response_type=code&client_id=${process.env.REACT_APP_SPOTIFY_CLIENT_ID}&scope=streaming user-read-playback-state user-read-private user-modify-playback-state user-read-email playlist-read-private playlist-read-collaborative playlist-modify-public playlist-modify-private&redirect_uri=${process.env.REACT_APP_SPOTIFY_CALLBACK_URI}`}>Login With Spotify</a>
                <a href={`https://connect.deezer.com/oauth/auth.php?app_id=${process.env.REACT_APP_DEEZER_CLIENT_ID}&redirect_uri=${process.env.REACT_APP_DEEZER_CALLBACK_URI}&perms=basic_access,email,manage_library`}>Login With Deezer</a>

            </div>
        </div>
    )
}
