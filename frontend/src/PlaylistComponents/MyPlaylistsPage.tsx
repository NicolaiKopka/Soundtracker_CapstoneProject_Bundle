import {NavLink} from "react-router-dom";

export default function MyPlaylistsPage() {

    return (
        <div>
            My Playlists
            {localStorage.getItem("spotify_jwt") &&
            <NavLink to={"/spotify-playlists"}><button>Show My Spotify Playlists</button></NavLink>}
        </div>
    )
}