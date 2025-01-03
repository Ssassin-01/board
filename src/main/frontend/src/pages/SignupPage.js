import {signup} from "../api/authApi";
import {useNavigate} from "react-router-dom";
import AuthForm from "../components/AuthForm";

const SignupPage = () => {
    const navigate = useNavigate();
    const handleSignup = async (formData) => {
        try {
            await signup(formData);
            alert('회원가입 성공!');
            navigate('/login');
        } catch (error) {
            console.error('회원가입 실패:', error.response?.data || error.message);
            alert('회원가입 실패');
        }
    };
    return <AuthForm onSubmit={handleSignup} title="Sign Up" />;
}
export default SignupPage;