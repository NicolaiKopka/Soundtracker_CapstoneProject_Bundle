import Header from "../Header/Header";
import {FormEvent, useState} from "react";
import {loginUser} from "../api_methods";
import {useNavigate} from "react-router-dom";
import "./LoginPage.css"

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
            .catch(() => props.setErrorMessage("Oops, wrong credentials"))
    }

    // function spotifyLogin() {
    //     authorizeWithSpotify().catch()
    // }

    return (
        <div className={"login-form-wrapper"}>
            <Header />
            <div className={"login-form"}>
                <form onSubmit={login}>
                    <input className={"form-items"} value={username} placeholder={"username"} onChange={ev => setUsername(ev.target.value)}/>
                    <input className={"form-items"} value={password} placeholder={"password"} onChange={ev => setPassword(ev.target.value)}/>
                    <button className={"login-button"} type={"submit"}>Login</button>
                </form>
                {/*<button onClick={spotifyLogin}>Login with Spotify</button>*/}
                <a href={`https://accounts.spotify.com/authorize?response_type=code&client_id=3ed8e5d98a3b469db405d1bb01652723&scope=streaming user-read-playback-state user-read-private user-modify-playback-state user-read-email playlist-read-private playlist-read-collaborative playlist-modify-public playlist-modify-private&redirect_uri=${process.env.REACT_APP_SPOTIFY_CALLBACK_URI}`}>Login With Spotify</a>
            </div>
        </div>
    )
}
