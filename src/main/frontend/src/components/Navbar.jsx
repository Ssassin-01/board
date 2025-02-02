import React from 'react';
import { Link } from 'react-router-dom';
import '../style/NavbarStyles.css';

const NavBar = () => {
  return (
    <nav className="navbar">
      <div className="navbar-container">
        <div className="navbar-left">
          <img
            src="https://img.gqkorea.co.kr/gq/2018/09/style_5bacbbcf9d2f1-930x587.jpg"
            alt="Logo"
            className="navbar-logo"
          />

          <Link to="/" className="navbar-link">
            <span className="navbar-title">MySocial</span>
          </Link>
        </div>
        <div className="navbar-right">
          <Link to="/profile" className="navbar-link">
            프로필
          </Link>
          <Link to="/posts/create" className="navbar-create-btn">
            ✚
          </Link>
        </div>
      </div>
    </nav>
  );
};

export default NavBar;
