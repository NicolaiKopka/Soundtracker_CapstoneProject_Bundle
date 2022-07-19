import {NavLink} from "react-router-dom";
import "./Header.css"

export default function Header(){

    return (
        <div>
            <NavLink to={"/"}><button className={"header-button"}>Main</button></NavLink>
            {window.location.pathname !== "/login" &&
                <NavLink to={"/login"}><button className={"header-button"}>Login</button></NavLink>
            }
            {window.location.pathname !== "/register" &&
                <NavLink to={"/register"}><button className={"header-button"}>Register</button></NavLink>
            }
            {localStorage.getItem("jwt") && <span>
                <NavLink to={"/logout"}><button className={"header-button"}>Logout</button></NavLink>
                {window.location.pathname !== "/favorites" &&
                    <NavLink to={"/favorites"}>
                        <button className={"header-button"}>My Favorites</button>
                    </NavLink>
                }
            </span>
            }
        </div>
    )

}