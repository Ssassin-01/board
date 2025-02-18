import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../api/axiosInstance';
import '../../style/AuthStyles.css';
import { useUser } from '../../context/UserContext';

const LoginPage = () => {
  const navigate = useNavigate();
  const { setIsLoggedIn, checkAuthStatus } = useUser();

  const [formData, setFormData] = useState({
    username: '',
    password: '',
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.post('/auth/login', formData);
      setIsLoggedIn(true); // ✅ 상태 즉시 변경
      checkAuthStatus(); // ✅ 로그인 상태 즉시 확인
      alert('로그인 성공');
      navigate('/');
    } catch (error) {
      alert(error.response?.data?.message || '로그인 실패');
    }
  };

  return (
    <div className="auth-container">
      <h2 className="auth-title">로그인</h2>
      <form className="auth-card" onSubmit={handleSubmit}>
        <input
          className="auth-input"
          type="text"
          name="username"
          placeholder="아이디"
          onChange={handleChange}
        />
        <input
          className="auth-input"
          type="password"
          name="password"
          placeholder="비밀번호"
          onChange={handleChange}
        />
        <button className="auth-btn" type="submit">
          로그인
        </button>
      </form>
    </div>
  );
};

export default LoginPage;
