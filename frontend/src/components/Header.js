import Head from "next/head";
import {useEffect, useState} from "react";

export default function Header() {


    let [loggedIn, setLoggedIn] = useState(false);

    useEffect(() => {

        let token = localStorage.getItem("token");
        if (token) {
            setLoggedIn(true);
        } else {
            setLoggedIn(false);
        }

    }, []);


    function logout() {

        // Remove token from local storage
        localStorage.removeItem("token");

        // Update loggedIn state
        setLoggedIn(false);

        // Redirect to login page
        window.location.href = "/login";
    }

    return (
        <div className="header">
            <div className="header-menu">
                <a href="/" className="menu-item">Home</a>
                <a href="/profile" className="menu-item">Profile</a>
                {loggedIn && (
                    <a href="#" className="menu-item" onClick={logout}>Logout</a>
                )}
            </div>
        </div>

    );
}