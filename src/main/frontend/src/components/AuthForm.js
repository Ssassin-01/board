import React, { useState } from 'react';
import '../style/AuthForm.css';

const AuthForm = ({ onSubmit, title }) => {
    const [formData, setFormData] = useState({
        username: '',
        email: '',
        password: ''
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value
        });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        onSubmit(formData);
    };

    return (
        <form onSubmit={handleSubmit} className="auth-form">
            <h2>{title}</h2>
            <input
                type="text"
                name="username"
                placeholder="Username"
                onChange={handleChange}
                value={formData.username}
                required
            />
            {title === 'Sign Up' && (
                <input
                    type="email"
                    name="email"
                    placeholder="Email"
                    onChange={handleChange}
                    value={formData.email}
                    required
                />
            )}
            <input
                type="password"
                name="password"
                placeholder="Password"
                onChange={handleChange}
                value={formData.password}
                required
            />
            <button type="submit">{title}</button>
        </form>
    );
};

export default AuthForm;