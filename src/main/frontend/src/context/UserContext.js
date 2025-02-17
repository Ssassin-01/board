import React, { createContext, useContext, useState, useEffect } from 'react';
import api from '../api/axiosInstance';

const UserContext = createContext();

export const UserProvider = ({ children }) => {
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  const checkAuthStatus = async () => {
    console.log('🔍 로그인 상태 확인 요청 시작');

    try {
      const response = await api.get('/auth/status');
      console.log(`✅ 로그인 상태 응답: ${response.data.isLoggedIn}`);
      setIsLoggedIn(response.data.isLoggedIn);
    } catch (error) {
      console.warn('🔴 로그인 상태 확인 실패: 403 (Unauthorized)');
      setIsLoggedIn(false);
    }
  };

  useEffect(() => {
    checkAuthStatus(); // ✅ 최초 실행
    window.addEventListener('focus', checkAuthStatus); // ✅ 브라우저 포커스 시 로그인 상태 확인

    return () => {
      window.removeEventListener('focus', checkAuthStatus);
    };
  }, []);

  return (
    <UserContext.Provider
      value={{ isLoggedIn, setIsLoggedIn, checkAuthStatus }}
    >
      {children}
    </UserContext.Provider>
  );
};

export const useUser = () => {
  return useContext(UserContext);
};
