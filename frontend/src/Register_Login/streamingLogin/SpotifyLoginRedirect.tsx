import {useEffect} from "react";


export default function SpotifyLoginRedirect() {

    useEffect(() => {
        console.log(window.location.pathname)
    })

    return (
        <div>
            ....Redirect....
        </div>
    )
}