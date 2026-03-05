import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import API_URL from "../../api";
import "./SignUp.css";

function SignUp() {
    const navigate = useNavigate();
    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [error, setError] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError("");

        if (password !== confirmPassword) {
            setError("Passwords do not match");
            return;
        }

        try {
            const res = await fetch(`${API_URL}/auth/register`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                credentials: "include",
                body: JSON.stringify({ username, email, password }),
            });

            const data = await res.json();

            if (res.ok) {
                navigate("/");
            } else {
                setError(data.error || "Registration failed");
            }
        } catch (err) {
            setError("Unable to connect to server");
        }
    };

    const handleGoogleSignUp = () => {
        window.location.href = `${API_URL}/oauth2/authorization/google`;
    };

    return (
        <div className="signup-wrapper">
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
                <div className="signup-box">
                    <h3 className="welcome-text">Create An Account</h3>

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
                            type="email"
                            placeholder="Email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />

                        <input
                            type="password"
                            placeholder="Password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />

                        <input
                            type="password"
                            placeholder="Confirm Password"
                            value={confirmPassword}
                            onChange={(e) => setConfirmPassword(e.target.value)}
                            required
                        />

                        <button type="submit">Sign Up</button>
                    </form>

                    <div className="divider">
                        <span>or</span>
                    </div>

                    <button className="google-btn" onClick={handleGoogleSignUp}>
                        Sign up with Google
                    </button>

                    <p className="login-text">
                        Have an account? <span onClick={() => navigate("/")}>Log in</span>
                    </p>
                </div>
            </div>
        </div>
    );
}

export default SignUp;
