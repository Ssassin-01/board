import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import SignUpPage from '../pages/SignUpPage';
import LoginPage from '../pages/LoginPage';
import ProfilePage from '../pages/Profile';
import UpdateProfilePage from '../pages/UpdateProfilePage';
import DeleteAccountPage from '../pages/DeleteAccountPage';
const AppRoutes = () => (
  <Router>
    <Routes>
      <Route path="/signup" element={<SignUpPage />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/profile" element={<ProfilePage />} />
      <Route path="/profile/update" element={<UpdateProfilePage />} />
      <Route path="/profile/delete" element={<DeleteAccountPage />} />
    </Routes>
  </Router>
);

export default AppRoutes;
