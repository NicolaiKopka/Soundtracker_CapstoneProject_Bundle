import {NavLink} from "react-router-dom";

export default function Header(){

    return (
        <div>
            <NavLink to={"/"}><a href={"#"}>Main</a></NavLink>
            {window.location.pathname != "/login" &&
                <NavLink to={"/login"}><a href={"#"}>Login</a></NavLink>
            }
            {window.location.pathname != "/register" &&
                <NavLink to={"/register"}><a href={"#"}>Register</a></NavLink>
            }
            {localStorage.getItem("jwt") && <span>
                <NavLink to={"/logout"}><a href={"#"}>Logout</a></NavLink>
                <NavLink to={"/user"}><a href={"#"}>User</a></NavLink>
                <NavLink to={"/favorites"}><a href={"#"}>My Favorites</a></NavLink>
            </span>
            }
        </div>
    )

}