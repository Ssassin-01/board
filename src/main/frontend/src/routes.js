import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import HelloPage from "./pages/HelloPage";


const AppRoutes = () => (
    <Router>
        <Routes>
            <Route path="/" element={<HelloPage />} />
        </Routes>
    </Router>
);

export default AppRoutes;
