import React, {useState} from "react";
const AuthForm = () => {
    const [formData, setFormData] = useState({
        username: '',
        email: '',
        password: ''
    });

    return (
        <form>
            <h2>회원가입</h2>
            <input
                type="text"
                name="username"
                placeholder="username"
                onChange={handleChange}
                value={formData.username}
                required
            />
        </form>
    )
}

export default AuthForm;