import React, { useState } from "react";
import "../styles/SignUp.css";
import { FiArrowLeft } from "react-icons/fi";
import { Link, useNavigate } from "react-router-dom";

const SignUp = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [role, setRole] = useState("");
  const [message, setMessage] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage("");

    if (!role) {
      setMessage("Select a Role!");
      return;
    }

    if (password !== confirmPassword) {
      setMessage("Passwords do not match!");
      return;
    }

    const formattedUsername = username.toLowerCase();
    const formattedRole = role.toUpperCase().replace(" ", "_");

    try {
      const response = await fetch("http://localhost:8080/api/auth/register", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          userName: formattedUsername,
          password,
          role: formattedRole,
        }),
      });

      let data = null;
      if (response.headers.get("content-type")?.includes("application/json")) {
        data = await response.json();
      }
      if (!response.ok) {
        throw new Error("Registration failed. Username may be taken!");
      }

      localStorage.setItem("authToken", data.token); // Store token for persistence

      navigate("/dashboard");
    } catch (error) {
      setMessage(error.message);
    }
  };

  return (
    <div className="signup-container">
      <form className="signup-form" onSubmit={handleSubmit}>
        <Link to="/">
          <FiArrowLeft className="back-icon" />
        </Link>
        <div className="signup-title">Sign Up</div>
        <label>Role</label>
        <div className="role-selector">
          <button
            type="button"
            className={role === "Product Owner" ? "selected" : ""}
            onClick={() => setRole("Product Owner")}
          >
            <h2>Product Owner</h2>
          </button>
          <button
            type="button"
            className={role === "Scrum Master" ? "selected" : ""}
            onClick={() => setRole("Scrum Master")}
          >
            <h2>Scrum Master</h2>
          </button>
          <button
            type="button"
            className={role === "Developer" ? "selected" : ""}
            onClick={() => setRole("Developer")}
          >
            <h2>Developer</h2>
          </button>
        </div>
        <label>Username</label>
        <input
          type="text"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required
        />
        <label>Password</label>
        <input
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
        <label>Re-Type Password</label>
        <input
          type="password"
          value={confirmPassword}
          onChange={(e) => setConfirmPassword(e.target.value)}
          required
        />
        {message && <p className="message">{message}</p>}
        <button className="submit-btn" type="submit">
          Sign Up
        </button>
        <div className="signup-footer">
          Already have an account? <Link to="/signIn">Sign In</Link>
        </div>
      </form>
    </div>
  );
};

export default SignUp;
