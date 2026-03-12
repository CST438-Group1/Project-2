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
        } catch {
            setError("Unable to connect to server");
        }
    };

    const handleGoogleSignUp = () => {
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

                    <h2 className="auth-title">Create An Account</h2>

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

                        <button type="submit" className="auth-button">
                            Sign Up
                        </button>
                    </form>

                    <div className="divider">
                        <span>or</span>
                    </div>

                    <div className="google-text">
                        Sign up with{" "}
                        <span onClick={handleGoogleSignUp}>
                            Google
                        </span>
                    </div>

                    <div className="auth-switch">
                        Have an account?{" "}
                        <span onClick={() => navigate("/")}>
                            Log in
                        </span>
                    </div>

                </div>
            </div>
        </div>
    );
}

export default SignUp;