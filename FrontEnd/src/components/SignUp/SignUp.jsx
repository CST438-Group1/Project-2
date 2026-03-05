import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./SignUp.css";

function SignUp() {
  const navigate = useNavigate();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const handleSubmit = (e) => {
    e.preventDefault();
    if (password !== confirmPassword) {
      alert("Passwords do not match");
      return;
    }
    console.log("Signing up:", username, password);
    navigate("/continents");
  };

  return (
    <div className="signup-wrapper">
      <div className="left-section">
        <h1 className="logo">
          MotoRYX<span className="dot">.</span>
        </h1>
        <p className="tagline">Every Model. Every Lap. Logged.</p>
      </div>

      <div className="right-section">
        <div className="signup-box">
          <h3 className="welcome-text">Create An Account</h3>

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
            <input
              type="password"
              placeholder="Confirm Password"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              required
            />
            <button type="submit">Sign Up</button>
          </form>

          <p className="login-text">
            Have an account? <span onClick={() => navigate("/")}>Log in</span>
          </p>
        </div>
      </div>
    </div>
  );
}

export default SignUp;
