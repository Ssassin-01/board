import React from 'react';
import { deleteMemberInfo } from '../api/auth';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import "../style/Auth.css";

const MemberDeletePage = () => {
    const { logout } = useAuth();
    const navigate = useNavigate();

    const handleDelete = async () => {
        if (window.confirm('정말로 회원 탈퇴를 진행하시겠습니까?')) {
            try {
                await deleteMemberInfo();
                alert('회원 탈퇴가 완료되었습니다.');
                logout();
                navigate('/signup');
            } catch (error) {
                console.error('회원 탈퇴 실패:', error.response?.data?.message || error.message);
                alert('회원 탈퇴에 실패했습니다.');
            }
        }
    };

    return (
        <div className="member-delete">
            <h2>회원 탈퇴</h2>
            <p>정말로 탈퇴하시겠습니까? 이 작업은 되돌릴 수 없습니다.</p>
            <button onClick={handleDelete}>회원 탈퇴</button>
            <button onClick={() => navigate('/me')}>취소</button>
        </div>
    );
};

export default MemberDeletePage;
