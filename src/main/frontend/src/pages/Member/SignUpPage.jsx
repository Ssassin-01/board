import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../api/axiosInstance';
import '../../style/AuthStyles.css';

const SignUpPage = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: '',
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.post('/members/signup', formData);
      alert('회원가입 성공! 로그인 페이지로 이동');
      navigate('/login');
    } catch (error) {
      alert(error.response?.data?.message || '회원가입 실패');
    }
  };

  return (
    <div className="container">
      <h2 className="auth-title">회원가입</h2>
      <form className="auth-card" onSubmit={handleSubmit}>
        <input
          className="auth-input"
          type="text"
          name="username"
          placeholder="아이디"
          onChange={handleChange}
          required
        />
        <input
          className="auth-input"
          type="email"
          name="email"
          placeholder="이메일"
          onChange={handleChange}
          required
        />
        <input
          className="auth-input"
          type="password"
          name="password"
          placeholder="비밀번호"
          onChange={handleChange}
          required
        />
        <button className="auth-btn" type="submit">
          회원가입
        </button>
      </form>
    </div>
  );
};

export default SignUpPage;
