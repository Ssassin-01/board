import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import HelloPage from "../pages/HelloPage";
import SignupPage from "../pages/SignupPage";
import LoginPage from "../pages/LoginPage";


const AppRoutes = () => (
    <Router>
        <Routes>
            <Route path="/" element={<HelloPage />} />
            <Route path="/signup" element={<SignupPage />} />
            <Route path="/login" element={<LoginPage />} />
        </Routes>
    </Router>
);

export default AppRoutes;
