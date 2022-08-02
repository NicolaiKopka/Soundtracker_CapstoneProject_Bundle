import {useEffect} from "react";
import {useNavigate} from "react-router-dom";
import toast from "react-hot-toast";


export default function LogoutPage() {

    const nav =  useNavigate()

    useEffect(() => {
        localStorage.clear()
        toast.success("You are logged out")
        nav("/")
    }, [nav])

    return(
        <div>
            Logout.....
        </div>
    )
}