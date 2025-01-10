import React, { createContext, useContext, useState, useEffect } from 'react';
import { getMemberInfo } from '../api/auth';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchUser = async () => {
            const token = localStorage.getItem('token');
            if (token) {
                try {
                    const response = await getMemberInfo();
                    setUser(response.data.data); // 유저 정보 설정
                } catch (error) {
                    console.error('인증 정보 불러오기 실패:', error.message);
                    localStorage.removeItem('token');
                    setUser(null);
                }
            }
            setLoading(false); // 로딩 상태 종료
        };

        fetchUser();
    }, [setUser]); // setUser 의존성 추가

    const logout = () => {
        localStorage.removeItem('token');
        setUser(null);
    };

    return (
        <AuthContext.Provider value={{ user, setUser, logout, loading }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);