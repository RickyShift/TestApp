import React from "react";
import { Link } from "react-router-dom";
import "../styles/Header.css";

const Header = () => {
  return (
    <header className="header">
      <div className="header-logo">ScrumStream</div>
      <div className="header-buttons">
        <div className="header-nav">
          <Link to="/dashboard">Dashboard</Link>
          <Link to="/projects">Projects</Link>
          <Link to="/backlog">Backlog</Link>
          <Link to="/sprints">Sprints</Link>
          <Link to="/userStories">Board</Link>
        </div>
        <div className="header-auth-buttons">
          <Link
            to="/signIn"
            style={{ color: "inherit", textDecoration: "inherit" }}
          >
            <button className="header-login">Sign In</button>
          </Link>

          <Link
            to="/signUp"
            style={{ color: "inherit", textDecoration: "inherit" }}
          >
            <button className="header-signup">Sign Up</button>
          </Link>
        </div>
      </div>
    </header>
  );
};

export default Header;
