import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import '../style/NavbarStyles.css';
import { removeCookie } from '../utils/auth';
import { useUser } from '../context/UserContext';
import api from '../api/axiosInstance';
import { getProfileImage } from '../api/ProfileApi';

const NavBar = () => {
  const navigate = useNavigate();
  const { isLoggedIn, checkAuthStatus } = useUser();
  const [profileImageURL, setProfileImageURL] = useState(null);

  useEffect(() => {
    console.log(`🔄 Navbar 렌더링: isLoggedIn=${isLoggedIn}`);

    if (isLoggedIn) {
      fetchProfileImage();
    } else {
      setProfileImageURL(null);
    }
  }, [isLoggedIn]);

  const fetchProfileImage = async () => {
    try {
      const response = await api.get('/members/me'); // ✅ 실제 API에서 회원 정보 가져오기
      const imageUrl = getProfileImage(response.data.data.profileImageURL); // ✅ 프로필 이미지 변환
      setProfileImageURL(imageUrl);
    } catch (error) {
      console.warn('⚠ 프로필 이미지 가져오기 실패:', error);
    }
  };

  const handleLogout = async () => {
    try {
      await api.post('/auth/logout');
      removeCookie('accessToken');
      checkAuthStatus(); // ✅ 로그아웃 후 즉시 상태 업데이트
      navigate('/login'); // ✅ 로그인 페이지로 이동
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
                {profileImageURL ? (
                  <img
                    src={profileImageURL}
                    alt="Profile"
                    className="navbar-profile-img"
                  />
                ) : (
                  '프로필'
                )}
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
