import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./Login.css";

function Login() {
  const navigate = useNavigate();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("Logging in:", username, password);
    navigate("/continents");
  };

  return (
    <div className="login-wrapper">
      <div className="left-section">
        <h1 className="logo">
          MotoRYX<span className="dot">.</span>
        </h1>
        <p className="tagline">Every Model. Every Lap. Logged.</p>
      </div>

      <div className="right-section">
        <div className="login-box">
          <h3 className="welcome-text">Welcome Back</h3>

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

          <p className="signup-text">
            No account? <span onClick={() => navigate("/signup")}>Sign up</span>
          </p>
        </div>
      </div>
    </div>
  );
}

export default Login;
