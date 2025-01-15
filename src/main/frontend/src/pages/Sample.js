import React from 'react';
import { useAuth } from '../context/AuthContext';
import {newNavigate} from "react-router-dom";
import AuthForm from '../components/AuthForm';
import { login } from '../api/auth';

const Sample = () => {
    const navigate = newNavigate();
    const {setUser} = useAuth();

    const handleSubmit = async (formData) => {
        try {
            const response = await login(formData);
            const {token, username} = response.data.data;

            localStorage.setItem("token", token);

            setUser({username});
            navigate("/me");
        } catch (error) {
            console.error("로그인 실패", error.response?.data?.message || error.message);
            alert()
        }        
    }

    return (
        <AuthForm onSubmit={handleSubmit} title="login" />                    
    );
};

export default Sample;