import {FormEvent, useState} from "react";
import {loginUser, registerUser} from "../api_methods";
import Header from "../Header/Header";
import {useNavigate} from "react-router-dom";
import "./RegisterPage.css"
import toast from "react-hot-toast";

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
            .catch((error) => toast.error(error.response.data))
    }

    return (
        <div className={"register-form-wrapper"}>
            <Header />
            <div className={"register-form"}>
                <form  onSubmit={register}>
                    <input className={"form-items"} required={true} value={username} placeholder={"username"} onChange={ev => setUsername(ev.target.value)}/>
                    <input className={"form-items"} required={true} type={"password"} value={password} placeholder={"password"} onChange={ev => setPassword(ev.target.value)}/>
                    <input className={"form-items"} required={true} type={"password"} value={checkPassword} placeholder={"password"} onChange={ev => setCheckPassword(ev.target.value)}/>
                    <button className={"register-button"} type={"submit"}>Register</button>
                </form>
            </div>
        </div>
    )
}