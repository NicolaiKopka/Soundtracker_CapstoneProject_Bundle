import {NavLink} from "react-router-dom";
import "./Header.css"

export default function Header(){

    return (
        <div>
            {window.location.pathname !== "/" &&
                <NavLink to={"/"}><button className={"header-button"}>Main</button></NavLink>
            }
            {!localStorage.getItem("jwt") &&
                window.location.pathname !== "/login" &&
                    <NavLink to={"/login"}><button className={"header-button"}>Login</button></NavLink>
            }

            {localStorage.getItem("jwt") && <span>
                {window.location.pathname !== "/my-playlists" &&
                    <NavLink to={"/my-playlists"}><button className={"header-button"}>My Playlists</button></NavLink>
                }
                {window.location.pathname !== "/favorites" &&
                    <NavLink to={"/favorites"}><button className={"header-button"}>My Favorites</button></NavLink>
                }
                <NavLink to={"/logout"}><button className={"header-button"}>Logout</button></NavLink>
            </span>
            }
        </div>
    )

}