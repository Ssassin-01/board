import React from 'react';
import AuthForm from '../components/AuthForm';
import { login } from '../api/auth';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const LoginPage = () => {
    const navigate = useNavigate();
    const { setUser } = useAuth(); // 사용자 정보 업데이트를 위해 AuthContext 사용

    const handleLogin = async (formData) => {
        try {
            const response = await login(formData); // 로그인 API 호출
            const { token, username } = response.data.data;

            // JWT 토큰 저장
            localStorage.setItem('token', token);

            // 사용자 정보 업데이트
            setUser({ username });

            alert('로그인 성공!');
            navigate('/me'); // /me 페이지로 이동
        } catch (error) {
            console.error('로그인 실패:', error.response?.data?.message || error.message);
            alert(error.response?.data?.message || '로그인에 실패했습니다.');
        }
    };

    return <AuthForm onSubmit={handleLogin} title="Login" />;
};

export default LoginPage;
