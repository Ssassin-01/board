import React, { useEffect, useState } from 'react';
import api from '../api/axios';

const HelloPage = () => {
    const [message, setMessage] = useState('');

    useEffect(() => {
        const fetchHelloWorld = async () => {
            try {
                const response = await api.get('/hello');
                setMessage(response.data);
            } catch (error) {
                console.error('Error fetching Hello World:', error);
            }
        };

        fetchHelloWorld();
    }, []);

    return (
        <div>
            <h1>Spring Boot + React</h1>
            <h2>{message}</h2>
        </div>
    );
};

export default HelloPage;
