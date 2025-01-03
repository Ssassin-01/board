import React from 'react';
import AuthForm from '../components/AuthForm';
import { login } from '../api/authApi';
import { useNavigate } from 'react-router-dom';

const LoginPage = () => {
    const navigate = useNavigate();

    const handleLogin = async (formData) => {
        try {
            const response = await login(formData);
            localStorage.setItem('token', response.data);
            alert('로그인 성공!');
            navigate('/');
        } catch (error) {
            console.error('로그인 실패:', error.response?.data || error.message);
            alert('로그인 실패');
        }
    };

    return <AuthForm onSubmit={handleLogin} title="Login" />;
};

export default LoginPage;