import React, {useEffect, useState} from "react";
import axios from "axios";
import userService from "@/services/userService";


export default function Login() {


    const [tab, setTab] = useState("login");
    const [busy, setBusy] = useState(false);
    const [msg, setMsg] = useState("");


    /* ----- form state ----- */
    const [login, setLogin] = useState({username: "", password: ""});
    const [signup, setSignup] = useState({
        name: "", email: "", username: "", password: "", confirm: ""
    });

    useEffect(() => {

        // Check if user is already logged in
        const token = localStorage.getItem("token");
        if (token) {
            // Redirect to home page if token exists
            window.location.href = "/";
        }

    }, []);



    async function handleLogin(e) {
        e.preventDefault();

        console.log("Logging in with:", login);

        console.log("UserService: ", userService)
        let token = await userService.loginApi(login.username, login.password);

        console.log(`Token received: ${token}`);

        // store token in localStorage
        localStorage.setItem("token", token);

        // redirect to home page
        window.location.href = "/";


    }

  async function  handleSignup (e) {
        e.preventDefault();
        console.log("Signing up with:", signup);

        if (signup.password !== signup.confirm) {
            alert("Passwords do not match.");
            return;
        }
        if (signup.password.length < 8) {
            alert("Password must be at least 8 characters.");
            return;
        }

        setBusy(true);

        try {
   //  const response = await axios.post(`http://localhost:8080/users`, signup);

            const response = await axios.post("http://localhost:8080/users",signup,
                { headers: { "Content-Type": "application/json" } }
            );

            const user = response.data;

            setBusy(false);
            {}
            if (response.status === 201) {
                console.log("User returned:", user);
                alert(`Welcome, ${user.name}!`);
                //setTab("login");
                window.location.href = "/login";

                useEffect(() => {
                    setMsg("Account Created Successfully!!!");
                    const timer = setTimeout(() => setMsg(""), 6000);

                    return () => clearTimeout(timer);

                }, []);

            } else {
                console.error("Unexpected response status:", response.status);
                alert("Signup failed. Please try again.");
            }
        } catch (error) {
            setBusy(false);

            if (error.response) {
                console.error("Error during signup:", error.response.data);
                alert(`Signup failed: ${error.response.data.message || 'Please try again.'}`);
            } else {
                console.error("Network error:", error);
                alert("Signup failed. Please check your internet connection and try again.");
            }
        }
    };



    return (
        <div className="auth-shell">
            {/* ---------- tabs ---------- */}
            <div className="tabs">
                <button
                    className={tab === "login" ? "active" : ""}
                    onClick={() => setTab("login")}
                >
                    Log In
                </button>
                <button
                    className={tab === "signup" ? "active" : ""}
                    onClick={() => setTab("signup")}
                >
                    Sign Up
                </button>
            </div>

            {/* ---------- forms ---------- */}
            {tab === "login" && (
                <form className="form" onSubmit={handleLogin}>
                    <input
                        placeholder="Username"
                        required
                        value={login.username}
                        onChange={e => setLogin({...login, username: e.target.value})}
                    />
                    <input
                        type="password"
                        placeholder="Password"
                        required
                        value={login.password}
                        onChange={e => setLogin({...login, password: e.target.value})}
                    />
                    <button disabled={busy}>Log In</button>
                </form>
            )}

            {tab === "signup" && (
                <form className="form" onSubmit={handleSignup}>
                    <input
                        placeholder="Name"
                        required
                        value={signup.name}
                        onChange={e => setSignup({...signup, name: e.target.value})}
                    />
                    <input
                        type="email"
                        placeholder="Email"
                        required
                        value={signup.email}
                        onChange={e => setSignup({...signup, email: e.target.value})}
                    />
                    <input
                        placeholder="Username"
                        required
                        value={signup.username}
                        onChange={e => setSignup({...signup, username: e.target.value})}
                    />
                    <input
                        type="password"
                        placeholder="Password"
                        required
                        value={signup.password}
                        onChange={e => setSignup({...signup, password: e.target.value})}
                    />
                    <input
                        type="password"
                        placeholder="Confirm password"
                        required
                        value={signup.confirm}
                        onChange={e => setSignup({...signup, confirm: e.target.value})}
                    />
                   <button disabled={busy}>Create Account</button>

                </form>
            )}

            {/* ---------- status message ---------- */}
            {msg && <p className="msg">{msg}</p>}
        </div>
    );
}