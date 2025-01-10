import React, { useEffect, useState } from 'react';
import { getMemberInfo } from '../api/auth';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';

const MyPage = () => {
    const { user } = useAuth();
    const [memberInfo, setMemberInfo] = useState(null); // 회원 정보 상태
    const [loading, setLoading] = useState(true); // 로딩 상태
    const [error, setError] = useState(null); // 오류 상태
    const navigate = useNavigate();

    useEffect(() => {
        const fetchMemberInfo = async () => {
            setLoading(true); // 로딩 시작
            try {
                const response = await getMemberInfo({ params: { t: new Date().getTime() } });
                console.log('회원 정보 응답:', response.data); // 응답 데이터 확인
                setMemberInfo(response.data.data); // 데이터 저장
                setError(null); // 오류 초기화
            } catch (error) {
                console.error('회원 정보 불러오기 실패:', error.response?.data?.message || error.message);
                setError('회원 정보를 불러오는 중 오류가 발생했습니다. 다시 시도해주세요.');
            } finally {
                setLoading(false); // 로딩 종료
            }
        };

        fetchMemberInfo();
    }, []);

    if (loading) {
        return <div>로딩 중...</div>;
    }

    if (error) {
        return <div>{error}</div>;
    }

    return (
        <div className="my-page">
            <h2>안녕하세요, {user?.username}님!</h2>
            <div className="info-box">
                <p><strong>사용자명:</strong> {memberInfo?.username}</p>
                <p><strong>이메일:</strong> {memberInfo?.email}</p>
            </div>
            <div className="actions">
                <button onClick={() => navigate('/me/update')}>회원 정보 수정</button>
                <button onClick={() => navigate('/me/delete')}>회원 탈퇴</button>
            </div>
        </div>
    );
};

export default MyPage;