import React from 'react';
import {signup} from "../api/auth";
import {useNavigate} from "react-router-dom";
import AuthForm from "../components/AuthForm";

const SignUpPage = () => {
    const navigate = useNavigate();

    const handleSignup = async (formData) => {
        try {
            await signup(formData);
            alert('회원가입 성공!');
            navigate('/login');
        } catch (error) {
            console.error('회원가입 실패:', error.response?.data?.message || error.message);
            alert('회원가입 실패!');
        }
    };

    return <AuthForm onSubmit={handleSignup} title="Sign Up" showEmail={true} />;
};

export default SignUpPage;