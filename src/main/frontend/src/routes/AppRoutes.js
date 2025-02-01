import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

import SignUpPage from './../pages/Member/SignUpPage';
import LoginPage from './../pages/Member/LoginPage';
import ProfilePage from './../pages/Member/Profile';
import UpdateProfilePage from './../pages/Member/UpdateProfilePage';
import DeleteAccountPage from './../pages/Member/DeleteAccountPage';

import PostListPage from '../pages/Post/PostListPage';
import PostDetailPage from '../pages/Post/PostDetailPage';
import PostCreatePage from '../pages/Post/PostCreatePage';
import PostEditPage from '../pages/Post/PostEditPage';

const AppRoutes = () => (
  <Router>
    <Routes>
      <Route path="/signup" element={<SignUpPage />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/profile" element={<ProfilePage />} />
      <Route path="/profile/update" element={<UpdateProfilePage />} />
      <Route path="/profile/delete" element={<DeleteAccountPage />} />

      <Route path="/posts" element={<PostListPage />} />
      <Route path="/posts/:id" element={<PostDetailPage />} />
      <Route path="/posts/create" element={<PostCreatePage />} />
      <Route path="/posts/edit/:id" element={<PostEditPage />} />

      <Route path="/" element={<PostListPage />} />
    </Routes>
  </Router>
);

export default AppRoutes;
