import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true, // JWT 쿠키 포함
});

// 요청 인터셉터: 모든 요청에 자동으로 실행
api.interceptors.request.use(
  (config) => {
    return config;
  },
  (error) => Promise.reject(error)
);

// 응답 인터셉터: Access Token이 만료된 경우 자동으로 Refresh 요청 수행
api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    // Access Token이 만료된 경우 (401 Unauthorized)
    if (
      error.response &&
      error.response.status === 401 &&
      !originalRequest._retry
    ) {
      originalRequest._retry = true; // 무한 루프 방지

      try {
        console.log('🔄 Access Token 만료: 자동으로 재발급 요청을 보냄...');

        // Refresh Token을 이용하여 새로운 Access Token 요청
        await axios.get('http://localhost:8080/api/auth/refresh', {
          withCredentials: true,
        });

        console.log('✅ Access Token 재발급 성공!');

        // 새로운 토큰이 설정되었으므로 원래 요청을 다시 실행
        return api(originalRequest);
      } catch (refreshError) {
        console.error('⛔ Refresh Token도 만료됨: 재로그인 필요', refreshError);
        return Promise.reject(refreshError);
      }
    }

    return Promise.reject(error);
  }
);

export default api;
