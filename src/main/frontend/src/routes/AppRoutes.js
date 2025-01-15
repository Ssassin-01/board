import React from 'react';
import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
} from 'react-router-dom';
import LoginPage from '../pages/LoginPage';
import { useAuth } from '../context/AuthContext';
import MyPage from '../pages/MyPage';
import SignUpPage from '../pages/SignupPage';
import MemberUpdatePage from '../pages/MemberUpdatePage';
import MemberDeletePage from '../pages/MemberDeletePage';

const ProtectedRoute = ({ children }) => {
  const { user, loading } = useAuth();

  if (loading) {
    // 로딩 중일 때는 빈 화면 또는 로딩 스피너 표시
    return <div>로딩 중...</div>;
  }

  if (!user) {
    // 인증된 사용자가 없으면 로그인 페이지로 이동
    return <Navigate to="/login" />;
  }

  return children;
};

const AppRoutes = () => (
  <Router>
    <Routes>
      <Route path="/signup" element={<SignUpPage />} />
      <Route path="/login" element={<LoginPage />} />
      <Route
        path="/me"
        element={
          <ProtectedRoute>
            <MyPage />
          </ProtectedRoute>
        }
      />
      <Route
        path="/me/update"
        element={
          <ProtectedRoute>
            <MemberUpdatePage />
          </ProtectedRoute>
        }
      />
      <Route
        path="/me/delete"
        element={
          <ProtectedRoute>
            <MemberDeletePage />
          </ProtectedRoute>
        }
      />
    </Routes>
  </Router>
);

export default AppRoutes;
