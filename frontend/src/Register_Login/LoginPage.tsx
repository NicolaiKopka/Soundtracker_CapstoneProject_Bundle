import Header from "../Header/Header";
import {FormEvent, useState} from "react";
import {loginUser} from "../api_methods";
import {useNavigate} from "react-router-dom";

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
            .then(data => localStorage.setItem("jwt", data.token))
            .then(() => nav("/"))
            .catch(() => props.setErrorMessage("Oops wrong credentials"))
    }

    return (
        <div>
            <Header />
            <form onSubmit={login}>
                <input value={username} placeholder={"username"} onChange={ev => setUsername(ev.target.value)}/>
                <input value={password} placeholder={"password"} onChange={ev => setPassword(ev.target.value)}/>
                <button type={"submit"}>Login</button>
            </form>
        </div>
    )
}