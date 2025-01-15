import api from './axios';

// 회원가입 API
export const signup = async (userData) => {
  return await api.post('/members/signup', userData);
};

// 로그인 API
export const login = async (userData) => {
  return await api.post('/members/login', userData);
};

// 회원 정보 조회 API
export const getMemberInfo = async () => {
  return await api.get('/members/me', {
    headers: {
      Authorization: `Bearer ${localStorage.getItem('token')}`,
    },
  });
};

// 회원 정보 수정 API
export const updateMemberInfo = async (userData) => {
  return await api.put('/members/update', userData, {
    headers: {
      Authorization: `Bearer ${localStorage.getItem('token')}`,
    },
  });
};

// 회원 탈퇴 API
export const deleteMemberInfo = async () => {
  return await api.delete('/members/delete', {
    headers: {
      Authorization: `Bearer ${localStorage.getItem('token')}`,
    },
  });
};
