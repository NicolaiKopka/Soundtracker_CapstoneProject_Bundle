import {NavLink} from "react-router-dom";

export default function Header(){

    return (
        <div>
            <NavLink to={"/"}><button>Main</button></NavLink>
            {window.location.pathname !== "/login" &&
                <NavLink to={"/login"}><button>Login</button></NavLink>
            }
            {window.location.pathname !== "/register" &&
                <NavLink to={"/register"}><button>Register</button></NavLink>
            }
            {localStorage.getItem("jwt") && <span>
                <NavLink to={"/logout"}><button>Logout</button></NavLink>
                <NavLink to={"/user"}><button>User</button></NavLink>
                <NavLink to={"/favorites"}><button>My Favorites</button></NavLink>
            </span>
            }
        </div>
    )

}