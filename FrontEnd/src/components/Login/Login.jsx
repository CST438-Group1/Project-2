import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import API_URL from "../../api";
import "./Login.css";

function Login() {
    const navigate = useNavigate();
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError("");

        try {
            const res = await fetch(`${API_URL}/auth/login`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                credentials: "include",
                body: JSON.stringify({ username, password }),
            });

            const data = await res.json();

            if (res.ok) {
                navigate("/continents");
            } else {
                setError(data.error || "Login failed");
            }
        } catch {
            setError("Unable to connect to server");
        }
    };

    const handleGoogleLogin = () => {
        window.location.href = `${API_URL}/oauth2/authorization/google`;
    };

    return (
        <div className="auth-wrapper">

            {/* LEFT SIDE */}
            <div className="auth-left">
                <div className="overlay"></div>

                <div className="auth-left-content">
                    <h1 className="logo">
                        MotoRYX<span className="dot">.</span>
                    </h1>
                    <p className="tagline">
                        Every Model. Every Lap. Logged.
                    </p>
                </div>
            </div>

            {/* RIGHT SIDE */}
            <div className="auth-right">
                <div className="auth-box">

                    <h2 className="auth-title">Welcome Back</h2>

                    {error && <p className="error-text">{error}</p>}

                    <form onSubmit={handleSubmit}>
                        <input
                            type="text"
                            placeholder="Username"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                        />

                        <input
                            type="password"
                            placeholder="Password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />

                        <button type="submit" className="auth-button">
                            Log In
                        </button>
                    </form>

                    <div className="divider">
                        <span>or</span>
                    </div>

                    <div className="google-text">
                        Sign in with <span onClick={handleGoogleLogin}>Google</span>
                    </div>

                    <div className="auth-switch">
                        No account?{" "}
                        <span onClick={() => navigate("/signup")}>
                            Sign up
                        </span>
                    </div>

                </div>
            </div>
        </div>
    );
}

export default Login;