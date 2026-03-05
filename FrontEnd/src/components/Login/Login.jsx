import React, { useState } from "react";
import API_URL from "../../api";
import "./Login.css";

function Login() {
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
                window.location.href = "/";
            } else {
                setError(data.error || "Login failed");
            }
        } catch (err) {
            setError("Unable to connect to server");
        }
    };

    const handleGoogleLogin = () => {
        window.location.href = `${API_URL}/oauth2/authorization/google`;
    };

    return (
        <div className="login-wrapper">
            {/* LEFT SIDE */}
            <div className="left-section">
                <h1 className="logo">
                    MotoRYX<span className="dot">.</span>
                </h1>
                <p className="tagline">
                    Every Model. Every Lap. Logged.
                </p>
            </div>

            {/* RIGHT SIDE */}
            <div className="right-section">
                <div className="login-box">
                    <h3 className="welcome-text">Welcome Back</h3>

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

                        <button type="submit">Log In</button>
                    </form>

                    <div className="divider">
                        <span>or</span>
                    </div>

                    <button className="google-btn" onClick={handleGoogleLogin}>
                        Sign in with Google
                    </button>

                    <p className="signup-text">
                        No account? <span>Sign up</span>
                    </p>
                </div>
            </div>
        </div>
      </div>
    </div>
  );
}

export default Login;
