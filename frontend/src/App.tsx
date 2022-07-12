import React from 'react';
import MainPageGallery from "./MainPageGallery/MainPageGallery";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import LoginPage from "./Register_Login/LoginPage";
import Header from "./Header/Header";

function App() {
    return (
        <div className="App">
            <h1>Soundtracker</h1>
            <BrowserRouter>
                <Routes>
                    <Route path={"/"} element={<MainPageGallery />} />
                    <Route path={"/login"} element={<LoginPage />} />
                </Routes>
            </BrowserRouter>

        </div>
    );
}

export default App;
