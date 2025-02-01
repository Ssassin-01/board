import React from 'react';
import api from '../../api/axiosInstance';
import { useNavigate } from 'react-router-dom';
import '../../style/AuthStyles.css';

const DeleteAccountPage = () => {
  const navigate = useNavigate();

  const handleDelete = async () => {
    if (!window.confirm('정말 회원 탈퇴하겠습니까?')) return;

    try {
      await api.delete('/members/delete');
      alert('회원탈퇴 완료!');
      navigate('/login');
    } catch (error) {
      alert(error.response?.data?.message || '회원탈퇴 실패');
    }
  };
  return (
    <div>
      <h2 className="auth-title">회원탈퇴</h2>
      <button className="auth-btn" onClick={handleDelete}>
        탈퇴하기
      </button>
    </div>
  );
};

export default DeleteAccountPage;
