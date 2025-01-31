import React from 'react';
import AuthForm from '../components/AuthForm';
import { login } from '../api/auth';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { getMemberInfo } from '../api/auth';

const LoginPage = () => {
  const navigate = useNavigate();
  const { setUser } = useAuth(); // 사용자 정보 업데이트를 위해 AuthContext 사용

  const handleLogin = async (formData) => {
    try {
      await login(formData); // 로그인 요청 (쿠키 자동 저장)
      const response = await getMemberInfo(); // 사용자 정보 가져오기
      setUser(response.data.data); // 사용자 정보 업데이트

      alert('로그인 성공!');
      navigate('/me'); // /me 페이지로 이동
    } catch (error) {
      console.error(
        '로그인 실패:',
        error.response?.data?.message || error.message
      );
      alert(error.response?.data?.message || '로그인에 실패했습니다.');
    }
  };

  return <AuthForm onSubmit={handleLogin} title="Login" />;
};

export default LoginPage;
