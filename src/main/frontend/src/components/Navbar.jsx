import React, { useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import '../style/NavbarStyles.css';
import { removeCookie } from '../utils/auth';
import { useUser } from '../context/UserContext';
import api from '../api/axiosInstance';

const NavBar = () => {
  const navigate = useNavigate();
  const { isLoggedIn, checkAuthStatus } = useUser();

  useEffect(() => {
    console.log(`🔄 Navbar 렌더링: isLoggedIn=${isLoggedIn}`);
  }, [isLoggedIn]);

  const handleLogout = async () => {
    try {
      await api.post('/auth/logout');
      removeCookie('accessToken');
      checkAuthStatus(); // ✅ 로그아웃 후 서버에서 즉시 로그인 상태 확인
      navigate('/login');
    } catch (error) {
      console.error('로그아웃 실패', error);
    }
  };

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
          {isLoggedIn ? (
            <>
              <Link to="/profile" className="navbar-link">
                프로필
              </Link>
              <button onClick={handleLogout} className="navbar-logout-btn">
                로그아웃
              </button>
            </>
          ) : (
            <>
              <Link to="/login" className="navbar-link">
                로그인
              </Link>
              <Link to="/signup" className="navbar-link">
                회원가입
              </Link>
            </>
          )}
          <Link to="/posts/create" className="navbar-create-btn">
            ✚
          </Link>
        </div>
      </div>
    </nav>
  );
};

export default NavBar;
