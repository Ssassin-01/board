import api from './axios';

// 회원가입 API
export const signup = async (userData) => {
    return await api.post('/api/members/signup', userData);
};

// 로그인 API
export const login = async (userData) => {
    return await api.post('/members/login', userData);
};