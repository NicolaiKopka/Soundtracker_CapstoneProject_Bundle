import {FormEvent, useState} from "react";
import {loginUser, registerUser} from "../api_methods";
import Header from "../Header/Header";
import {useNavigate} from "react-router-dom";

interface AppProps {
    setErrorMessage: Function
}

export default function RegisterPage(props: AppProps) {

    const [username, setUsername] = useState("")
    const [password, setPassword] = useState("")
    const [checkPassword, setCheckPassword] = useState("")
    const nav = useNavigate()

    function register(ev: FormEvent) {
        ev.preventDefault()
        registerUser(username, password, checkPassword)
            .then(() => loginUser(username, password).then(data => localStorage.setItem("jwt", data.token)))
            .then(() => nav("/"))
            .catch(() => props.setErrorMessage("Something Went Wrong ;)"))
    }

    return (
        <div>
            <Header />
            <form onSubmit={register}>
                <input value={username} placeholder={"username"} onChange={ev => setUsername(ev.target.value)}/>
                <input type={"password"} value={password} placeholder={"password"} onChange={ev => setPassword(ev.target.value)}/>
                <input type={"password"} value={checkPassword} placeholder={"password"} onChange={ev => setCheckPassword(ev.target.value)}/>
                <button type={"submit"}>Login</button>
            </form>
        </div>
    )
}