import React, { createContext, useContext, useState, useEffect } from 'react';
import { getMemberInfo } from '../api/auth';
import api from 'axios';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchUser = async () => {
      try {
        const response = await getMemberInfo(); // 쿠키를 통해 인증 정보 확인
        setUser(response.data.data); // 사용자 정보 설정
      } catch (error) {
        console.error('인증 정보 불러오기 실패:', error.message);
        setUser(null);
      } finally {
        setLoading(false); // 로딩 상태 종료
      }
    };

    fetchUser();
  }, []);

  const logout = async () => {
    try {
      await api.post('/logout'); // 서버에서 쿠키 삭제
      setUser(null);
      alert('로그아웃 성공!');
    } catch (error) {
      console.error('로그아웃 실패:', error.message);
      alert('로그아웃에 실패했습니다.');
    }
  };

  return (
    <AuthContext.Provider value={{ user, setUser, logout, loading }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
