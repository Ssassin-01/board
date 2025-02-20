import React, { useEffect, useState } from 'react';
import api from '../../api/axiosInstance';
import { useNavigate } from 'react-router-dom';
import { getProfileImage } from '../../api/ProfileApi';
import '../../style/ProfileStyles.css';

const Profile = () => {
  const [user, setUser] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const response = await api.get('/members/me');
        setUser(response.data.data);
      } catch (error) {
        alert(
          error.response?.data?.message ||
            '회원정보를 불러오는데 실패하였습니다'
        );
      }
    };
    fetchProfile();
  }, []);

  return (
    <div className="container">
      <h2 className="auth-title">내 프로필</h2>
      {user ? (
        <div className="profile-card">
          {/* ✅ 프로필 이미지 표시 */}
          <div className="profile-image-container">
            <img
              src={getProfileImage(user.profileImageURL)}
              alt="프로필"
              className="profile-image"
            />
          </div>

          <p>
            <strong>아이디:</strong> {user.username}
          </p>
          <p>
            <strong>이메일:</strong> {user.email}
          </p>

          <div className="auth-buttons">
            <button
              className="auth-btn"
              onClick={() => navigate('/profile/update')}
            >
              회원 정보 수정
            </button>
            <button
              className="auth-btn delete-btn"
              onClick={() => navigate('/profile/delete')}
              style={{ background: '#ff4d4d' }}
            >
              회원 탈퇴
            </button>
          </div>
        </div>
      ) : (
        <p className="loading-text">로딩 중...</p>
      )}
    </div>
  );
};

export default Profile;
