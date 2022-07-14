import {useEffect} from "react";
import {useNavigate} from "react-router-dom";


export default function LogoutPage() {

    const nav =  useNavigate()

    useEffect(() => {
        localStorage.clear()
        nav("/")
    }, [nav])

    return(
        <div>
            Logout.....
        </div>
    )
}