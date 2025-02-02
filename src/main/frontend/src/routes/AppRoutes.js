import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import FeedPage from '../pages/FeedPage';

import Navbar from '../components/Navbar';
import UpdateProfilePage from './../pages/Member/UpdateProfilePage';
import DeleteAccountPage from './../pages/Member/DeleteAccountPage';
import LoginPage from './../pages/Member/LoginPage';
import SignUpPage from './../pages/Member/SignUpPage';
import '../style/GlobalStyles.css';
import Profile from './../pages/Member/Profile';
import PostCreatePage from './../pages/Post/PostCreatePage';
import PostEditPage from './../pages/Post/PostEditPage';

const AppRoutes = () => (
  <Router>
    <Navbar />
    <Routes>
      <Route path="/signup" element={<SignUpPage />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/profile/update" element={<UpdateProfilePage />} />
      <Route path="/profile/delete" element={<DeleteAccountPage />} />
      <Route path="/" element={<FeedPage />} /> {/* ✅ 피드 페이지 */}
      <Route path="/posts/create" element={<PostCreatePage />} />
      <Route path="/posts/:id/edit" element={<PostEditPage />} />
      {/* ✅ 게시글 상세 */}
      <Route path="/profile" element={<Profile />} /> {/* ✅ 프로필 페이지 */}
    </Routes>
  </Router>
);

export default AppRoutes;
