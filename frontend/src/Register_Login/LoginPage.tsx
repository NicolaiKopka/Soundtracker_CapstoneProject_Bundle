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

    return (
        <div className={"login-form-wrapper"}>
            <Header />
            <div className={"login-form"}>
                <form onSubmit={login}>
                    <input className={"form-items"} value={username} placeholder={"username"} onChange={ev => setUsername(ev.target.value)}/>
                    <input className={"form-items"} value={password} placeholder={"password"} onChange={ev => setPassword(ev.target.value)}/>
                    <button className={"login-button"} type={"submit"}>Login</button>
                </form>
            </div>
        </div>
    )
}