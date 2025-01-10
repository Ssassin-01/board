import React, { useState, useEffect } from 'react';
import { updateMemberInfo } from '../api/auth';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import "../style/Auth.css";

const MemberUpdatePage = () => {
    const { user, setUser } = useAuth(); // AuthContext에서 user와 setUser 가져오기
    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        username: '',
        email: '',
        password: '',
    });

    const [loading, setLoading] = useState(false); // 로딩 상태 추가

    useEffect(() => {
        if (user) {
            setFormData({
                username: user.username,
                email: user.email,
                password: '', // 비밀번호는 초기화
            });
        }
    }, [user]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleUpdate = async () => {
        setLoading(true); // 로딩 시작
        try {
            const response = await updateMemberInfo(formData); // 회원 정보 수정 API 호출
            setUser(response.data.data); // 최신 회원 정보로 user 상태 업데이트
            alert('회원 정보가 성공적으로 수정되었습니다.');
            navigate('/me'); // /me 페이지로 이동
        } catch (error) {
            console.error('회원 정보 수정 실패:', error.response?.data?.message || error.message);
            alert('회원 정보 수정에 실패했습니다.');
        } finally {
            setLoading(false); // 로딩 종료
        }
    };

    if (loading) {
        return <div>수정 중입니다...</div>; // 로딩 중 표시
    }

    return (
        <div className="member-update">
            <h2>회원 정보 수정</h2>
            <input
                type="text"
                name="username"
                placeholder="새로운 사용자 이름"
                value={formData.username}
                onChange={handleChange}
            />
            <input
                type="email"
                name="email"
                placeholder="새로운 이메일"
                value={formData.email}
                onChange={handleChange}
            />
            <input
                type="password"
                name="password"
                placeholder="새로운 비밀번호"
                value={formData.password}
                onChange={handleChange}
            />
            <button onClick={handleUpdate}>수정하기</button>
            <button onClick={() => navigate('/me')}>취소</button>
        </div>
    );
};

export default MemberUpdatePage;
