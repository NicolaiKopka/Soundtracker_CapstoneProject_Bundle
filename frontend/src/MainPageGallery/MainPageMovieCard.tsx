import {MovieItem} from "../models";
import "./MainPageMovieCard.css"
interface MainPageGalleryProps {
    movie: MovieItem;
}

export default function MainPageMovieCard(props: MainPageGalleryProps) {

    return (
        <div>
            <div className={"movie-card"}>
                <img src={"https://image.tmdb.org/t/p/original" + props.movie.poster_path} alt={"image"}/>
                <br/>
                {props.movie.title}
                <br/>
                {props.movie.backdrop_path}
                <br/>
                {props.movie.id}
            </div>
        </div>
    )
}