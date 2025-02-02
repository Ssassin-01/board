import React, { useState } from 'react';

import { useNavigate } from 'react-router-dom';
import '../../style/AuthStyles.css';
import api from '../../api/axiosInstance';

const UpdateProfilePage = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
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
      await api.put('/members/update', formData);
      alert('회원정보 수정 완료!');
      navigate('/profile');
    } catch (error) {
      alert(error.response?.data?.message || '회원정보 실패');
    }
  };
  return (
    <div className="container">
      <h2 className="auth-title">회원정보 수정</h2>
      <form className="auth-card" onSubmit={handleSubmit}>
        <input
          className="auth-input"
          type="email"
          name="email"
          placeholder="새로운 이메일"
          onChange={handleChange}
          required
        />
        <input
          className="auth-input"
          type="password"
          name="password"
          placeholder="새로운 비밀번호"
          onChange={handleChange}
          required
        />
        <button className="auth-btn" type="submit">
          수정하기
        </button>
      </form>
    </div>
  );
};

export default UpdateProfilePage;
